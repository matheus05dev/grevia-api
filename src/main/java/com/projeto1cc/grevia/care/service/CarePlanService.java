package com.projeto1cc.grevia.care.service;

import com.projeto1cc.grevia.care.model.CarePlan;
import com.projeto1cc.grevia.care.dto.CarePlanRequestDTO;
import com.projeto1cc.grevia.care.dto.CarePlanResponseDTO;
import com.projeto1cc.grevia.care.enums.FrequencyType;
import com.projeto1cc.grevia.care.mapper.CarePlanMapper;
import com.projeto1cc.grevia.care.repository.CarePlanRepository;
import com.projeto1cc.grevia.care.repository.CareRecordRepository;
import com.projeto1cc.grevia.plant.model.Plant;
import com.projeto1cc.grevia.plant.repository.PlantRepository;
import com.projeto1cc.grevia.user.model.User;
import com.projeto1cc.grevia.user.model.enums.GardenerLevel;
import com.projeto1cc.grevia.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarePlanService {

    private final CarePlanRepository carePlanRepository;
    private final PlantRepository plantRepository;
    private final CareRecordRepository careRecordRepository;
    private final CarePlanMapper carePlanMapper;
    private final UserRepository userRepository;

    @Transactional
    public CarePlanResponseDTO createCarePlan(Long plantId, CarePlanRequestDTO requestDTO, String userEmail) {
        Plant plant = plantRepository.findById(plantId)
                .orElseThrow(() -> new RuntimeException("Planta não encontrada"));

        if (!plant.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Você não tem permissão para gerenciar os planos de cuidado desta planta");
        }

        CarePlan carePlan = carePlanMapper.toEntity(requestDTO);
        carePlan.setPlant(plant);
        
        LocalDate initialDate = requestDTO.startDate() != null ? requestDTO.startDate() : LocalDate.now();
        carePlan.setNextCareDate(initialDate);

        CarePlan savedPlan = carePlanRepository.save(carePlan);
        return carePlanMapper.toResponseDTO(savedPlan);
    }

    @Transactional(readOnly = true)
    public List<CarePlanResponseDTO> getCarePlansByPlantId(Long plantId, String userEmail) {
        Plant plant = plantRepository.findById(plantId)
                .orElseThrow(() -> new RuntimeException("Planta não encontrada"));

        if (!plant.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Você não tem permissão para visualizar os planos de cuidado desta planta");
        }

        return carePlanRepository.findByPlantId(plantId).stream()
                .map(carePlanMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public CarePlanResponseDTO updateCarePlan(Long plantId, Long careId, CarePlanRequestDTO requestDTO, String userEmail) {
        CarePlan carePlan = carePlanRepository.findById(careId)
                .orElseThrow(() -> new RuntimeException("Plano de cuidado não encontrado"));

        if (!carePlan.getPlant().getId().equals(plantId)) {
            throw new RuntimeException("O plano de cuidado não pertence a planta especificada");
        }

        if (!carePlan.getPlant().getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Você não tem permissão para atualizar esse plano de cuidado");
        }

        carePlanMapper.updateEntityFromDto(requestDTO, carePlan);
        
        if (requestDTO.startDate() != null) {
            carePlan.setNextCareDate(requestDTO.startDate());
        } else if (carePlan.getNextCareDate() == null) {
            carePlan.setNextCareDate(LocalDate.now());
        }

        CarePlan updatedPlan = carePlanRepository.save(carePlan);
        return carePlanMapper.toResponseDTO(updatedPlan);
    }

    @Transactional
    public void deleteCarePlan(Long plantId, Long careId, String userEmail) {
        CarePlan carePlan = carePlanRepository.findById(careId)
                .orElseThrow(() -> new RuntimeException("Plano de cuidado não encontrado"));

        if (!carePlan.getPlant().getId().equals(plantId)) {
            throw new RuntimeException("O plano de cuidado não pertence a planta especificada");
        }

        if (!carePlan.getPlant().getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Você não tem permissão para deletar esse plano de cuidado");
        }

        carePlanRepository.delete(carePlan);
    }

    @Transactional
    public CarePlanResponseDTO completeCarePlan(Long plantId, Long careId, String notes, String userEmail) {
        CarePlan carePlan = carePlanRepository.findById(careId)
                .orElseThrow(() -> new RuntimeException("Plano de cuidado não encontrado"));

        if (!carePlan.getPlant().getId().equals(plantId)) {
            throw new RuntimeException("O plano de cuidado não pertence a planta especificada");
        }

        if (!carePlan.getPlant().getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Você não tem permissão para gerenciar esse plano de cuidado");
        }

        // ── Bloqueio de adiantamento: só permite hoje ou atrasados ──
        if (carePlan.getNextCareDate() != null && carePlan.getNextCareDate().isAfter(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Este cuidado está agendado para " + carePlan.getNextCareDate()
                + ". Só é possível registrar cuidados para hoje ou atrasados.");
        }

        // Create the record
        com.projeto1cc.grevia.care.model.CareRecord record = new com.projeto1cc.grevia.care.model.CareRecord();
        record.setCarePlan(carePlan);
        record.setCareDate(LocalDate.now());
        record.setNotes(notes);
        careRecordRepository.save(record);

        // Update the plan
        carePlan.setLastCareDate(LocalDate.now());
        carePlan.setNextCareDate(calculateNextDate(LocalDate.now(), carePlan.getFrequencyType()));
        
        CarePlan updatedPlan = carePlanRepository.save(carePlan);

        // ── Gamificação: pontos, streak e level up ──
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Nível ANTES de ganhar pontos
        GardenerLevel levelBefore = GardenerLevel.fromPoints(
            user.getTotalPoints() != null ? user.getTotalPoints() : 0
        );

        // Incrementar pontos e ações
        user.setTotalCareActions((user.getTotalCareActions() != null ? user.getTotalCareActions() : 0) + 1);
        user.setTotalPoints((user.getTotalPoints() != null ? user.getTotalPoints() : 0) + 10);

        // Streak logic
        LocalDate lastCare = user.getLastCareDate();
        if (lastCare == null || lastCare.isBefore(LocalDate.now().minusDays(1))) {
            user.setCurrentStreak(1);
        } else if (lastCare.equals(LocalDate.now().minusDays(1))) {
            user.setCurrentStreak((user.getCurrentStreak() != null ? user.getCurrentStreak() : 0) + 1);
        }
        // If lastCare == today, don't change streak (already cared today)
        user.setLastCareDate(LocalDate.now());
        userRepository.save(user);

        // Nível DEPOIS de ganhar pontos
        GardenerLevel levelAfter = GardenerLevel.fromPoints(user.getTotalPoints());
        boolean didLevelUp = levelAfter.getLevel() > levelBefore.getLevel();

        return carePlanMapper.toResponseDTO(
            updatedPlan,
            didLevelUp ? true : null,
            didLevelUp ? levelAfter.getFullTitle() : null
        );
    }

    public LocalDate calculateNextDate(LocalDate baseDate, FrequencyType frequencyType) {
        if (baseDate == null) baseDate = LocalDate.now();
        
        return switch (frequencyType) {
            case DIARIO -> baseDate.plusDays(1);
            case DUAS_VEZES_SEMANA -> baseDate.plusDays(3);
            case TRES_VEZES_SEMANA -> baseDate.plusDays(2);
            case SEMANAL -> baseDate.plusWeeks(1);
            case QUINZENAL -> baseDate.plusWeeks(2);
            case MENSAL -> baseDate.plusMonths(1);
            case BIMESTRAL -> baseDate.plusMonths(2);
            case SOB_DEMANDA -> baseDate.plusYears(100); // effectively "never" auto-scheduled
        };
    }
}
