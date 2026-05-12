package com.projeto1cc.grevia.care.service;

import com.projeto1cc.grevia.care.dto.CarePlanRequestDTO;
import com.projeto1cc.grevia.care.dto.CarePlanResponseDTO;
import com.projeto1cc.grevia.care.enums.CareType;
import com.projeto1cc.grevia.care.enums.FrequencyType;
import com.projeto1cc.grevia.care.mapper.CarePlanMapper;
import com.projeto1cc.grevia.care.model.CarePlan;
import com.projeto1cc.grevia.care.model.CareRecord;
import com.projeto1cc.grevia.care.repository.CarePlanRepository;
import com.projeto1cc.grevia.care.repository.CareRecordRepository;
import com.projeto1cc.grevia.plant.model.Plant;
import com.projeto1cc.grevia.plant.repository.PlantRepository;
import com.projeto1cc.grevia.user.model.User;
import com.projeto1cc.grevia.user.model.enums.GardenerLevel;
import com.projeto1cc.grevia.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarePlanServiceTest {

    @Mock private CarePlanRepository carePlanRepository;
    @Mock private PlantRepository plantRepository;
    @Mock private CareRecordRepository careRecordRepository;
    @Mock private CarePlanMapper carePlanMapper;
    @Mock private UserRepository userRepository;

    @InjectMocks
    private CarePlanService carePlanService;

    private User user;
    private Plant plant;
    private CarePlan carePlan;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");
        user.setTotalPoints(0);
        user.setTotalCareActions(0);
        user.setCurrentStreak(0);

        plant = new Plant();
        plant.setId(10L);
        plant.setUser(user);

        carePlan = new CarePlan();
        carePlan.setId(100L);
        carePlan.setCareType(CareType.REGA);
        carePlan.setFrequencyType(FrequencyType.SEMANAL);
        carePlan.setPlant(plant);
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Bloqueio de adiantamento
    // ──────────────────────────────────────────────────────────────────────────

    @Test
    void completeCarePlan_ShouldBlockFutureCare() {
        carePlan.setNextCareDate(LocalDate.now().plusDays(3));
        when(carePlanRepository.findById(100L)).thenReturn(Optional.of(carePlan));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
            () -> carePlanService.completeCarePlan(10L, 100L, null, "test@test.com"));

        assertEquals(400, ex.getStatusCode().value());
        assertTrue(ex.getReason().contains("agendado para"));
        verify(careRecordRepository, never()).save(any());
    }

    @Test
    void completeCarePlan_ShouldAllowTodayCare() {
        carePlan.setNextCareDate(LocalDate.now());
        when(carePlanRepository.findById(100L)).thenReturn(Optional.of(carePlan));
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(carePlanRepository.save(any())).thenReturn(carePlan);
        when(carePlanMapper.toResponseDTO(any(), any(), any()))
            .thenReturn(new CarePlanResponseDTO(100L, CareType.REGA, FrequencyType.SEMANAL,
                LocalDate.now().plusWeeks(1), LocalDate.now(), 10L, null, null));

        CarePlanResponseDTO result = carePlanService.completeCarePlan(10L, 100L, null, "test@test.com");

        assertNotNull(result);
        verify(careRecordRepository, times(1)).save(any(CareRecord.class));
    }

    @Test
    void completeCarePlan_ShouldAllowOverdueCare() {
        carePlan.setNextCareDate(LocalDate.now().minusDays(2));
        when(carePlanRepository.findById(100L)).thenReturn(Optional.of(carePlan));
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(carePlanRepository.save(any())).thenReturn(carePlan);
        when(carePlanMapper.toResponseDTO(any(), any(), any()))
            .thenReturn(new CarePlanResponseDTO(100L, CareType.REGA, FrequencyType.SEMANAL,
                LocalDate.now().plusWeeks(1), LocalDate.now(), 10L, null, null));

        assertDoesNotThrow(
            () -> carePlanService.completeCarePlan(10L, 100L, null, "test@test.com"));
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Gamificação: pontos
    // ──────────────────────────────────────────────────────────────────────────

    @Test
    void completeCarePlan_ShouldAward10Points() {
        carePlan.setNextCareDate(LocalDate.now());
        user.setTotalPoints(30);
        user.setTotalCareActions(5);

        when(carePlanRepository.findById(100L)).thenReturn(Optional.of(carePlan));
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(carePlanRepository.save(any())).thenReturn(carePlan);
        when(carePlanMapper.toResponseDTO(any(), any(), any()))
            .thenReturn(new CarePlanResponseDTO(100L, CareType.REGA, FrequencyType.SEMANAL,
                LocalDate.now().plusWeeks(1), LocalDate.now(), 10L, null, null));

        carePlanService.completeCarePlan(10L, 100L, null, "test@test.com");

        assertEquals(40, user.getTotalPoints());
        assertEquals(6, user.getTotalCareActions());
        verify(userRepository, times(1)).save(user);
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Gamificação: level up
    // ──────────────────────────────────────────────────────────────────────────

    @Test
    void completeCarePlan_ShouldDetectLevelUp() {
        carePlan.setNextCareDate(LocalDate.now());
        // 40 pontos → com +10 vai para 50 → Aprendiz (level up!)
        user.setTotalPoints(40);
        user.setTotalCareActions(4);

        when(carePlanRepository.findById(100L)).thenReturn(Optional.of(carePlan));
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(carePlanRepository.save(any())).thenReturn(carePlan);
        when(carePlanMapper.toResponseDTO(any(), eq(true), eq("🌿 Jardineiro Aprendiz")))
            .thenReturn(new CarePlanResponseDTO(100L, CareType.REGA, FrequencyType.SEMANAL,
                LocalDate.now().plusWeeks(1), LocalDate.now(), 10L, true, "🌿 Jardineiro Aprendiz"));

        CarePlanResponseDTO result = carePlanService.completeCarePlan(10L, 100L, null, "test@test.com");

        assertEquals(50, user.getTotalPoints());
        verify(carePlanMapper).toResponseDTO(any(), eq(true), eq("🌿 Jardineiro Aprendiz"));
    }

    @Test
    void completeCarePlan_ShouldNotFlagLevelUp_WhenLevelStaysSame() {
        carePlan.setNextCareDate(LocalDate.now());
        // 10 pontos → com +10 vai para 20 → ainda Iniciante
        user.setTotalPoints(10);
        user.setTotalCareActions(1);

        when(carePlanRepository.findById(100L)).thenReturn(Optional.of(carePlan));
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(carePlanRepository.save(any())).thenReturn(carePlan);
        when(carePlanMapper.toResponseDTO(any(), isNull(), isNull()))
            .thenReturn(new CarePlanResponseDTO(100L, CareType.REGA, FrequencyType.SEMANAL,
                LocalDate.now().plusWeeks(1), LocalDate.now(), 10L, null, null));

        carePlanService.completeCarePlan(10L, 100L, null, "test@test.com");

        assertEquals(20, user.getTotalPoints());
        verify(carePlanMapper).toResponseDTO(any(), isNull(), isNull());
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Gamificação: streak
    // ──────────────────────────────────────────────────────────────────────────

    @Test
    void completeCarePlan_ShouldIncrementStreak_WhenCaredYesterday() {
        carePlan.setNextCareDate(LocalDate.now());
        user.setLastCareDate(LocalDate.now().minusDays(1));
        user.setCurrentStreak(3);

        when(carePlanRepository.findById(100L)).thenReturn(Optional.of(carePlan));
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(carePlanRepository.save(any())).thenReturn(carePlan);
        when(carePlanMapper.toResponseDTO(any(), any(), any()))
            .thenReturn(new CarePlanResponseDTO(100L, CareType.REGA, FrequencyType.SEMANAL,
                LocalDate.now().plusWeeks(1), LocalDate.now(), 10L, null, null));

        carePlanService.completeCarePlan(10L, 100L, null, "test@test.com");

        assertEquals(4, user.getCurrentStreak());
        assertEquals(LocalDate.now(), user.getLastCareDate());
    }

    @Test
    void completeCarePlan_ShouldResetStreak_WhenSkippedDays() {
        carePlan.setNextCareDate(LocalDate.now());
        user.setLastCareDate(LocalDate.now().minusDays(5));
        user.setCurrentStreak(10);

        when(carePlanRepository.findById(100L)).thenReturn(Optional.of(carePlan));
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(carePlanRepository.save(any())).thenReturn(carePlan);
        when(carePlanMapper.toResponseDTO(any(), any(), any()))
            .thenReturn(new CarePlanResponseDTO(100L, CareType.REGA, FrequencyType.SEMANAL,
                LocalDate.now().plusWeeks(1), LocalDate.now(), 10L, null, null));

        carePlanService.completeCarePlan(10L, 100L, null, "test@test.com");

        assertEquals(1, user.getCurrentStreak());
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Ownership validation
    // ──────────────────────────────────────────────────────────────────────────

    @Test
    void completeCarePlan_ShouldThrow_WhenPlantIdDoesNotMatch() {
        carePlan.setNextCareDate(LocalDate.now());
        when(carePlanRepository.findById(100L)).thenReturn(Optional.of(carePlan));

        assertThrows(RuntimeException.class,
            () -> carePlanService.completeCarePlan(999L, 100L, null, "test@test.com"));
    }

    @Test
    void completeCarePlan_ShouldThrow_WhenUserIsNotOwner() {
        carePlan.setNextCareDate(LocalDate.now());
        when(carePlanRepository.findById(100L)).thenReturn(Optional.of(carePlan));

        assertThrows(RuntimeException.class,
            () -> carePlanService.completeCarePlan(10L, 100L, null, "hacker@evil.com"));
    }

    // ──────────────────────────────────────────────────────────────────────────
    // calculateNextDate
    // ──────────────────────────────────────────────────────────────────────────

    @Test
    void calculateNextDate_ShouldReturnCorrectDates() {
        LocalDate base = LocalDate.of(2026, 1, 1);

        assertEquals(LocalDate.of(2026, 1, 2), carePlanService.calculateNextDate(base, FrequencyType.DIARIO));
        assertEquals(LocalDate.of(2026, 1, 8), carePlanService.calculateNextDate(base, FrequencyType.SEMANAL));
        assertEquals(LocalDate.of(2026, 1, 15), carePlanService.calculateNextDate(base, FrequencyType.QUINZENAL));
        assertEquals(LocalDate.of(2026, 2, 1), carePlanService.calculateNextDate(base, FrequencyType.MENSAL));
        assertEquals(LocalDate.of(2026, 3, 1), carePlanService.calculateNextDate(base, FrequencyType.BIMESTRAL));
    }
}
