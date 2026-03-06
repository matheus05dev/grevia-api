package com.projeto1cc.grevia.care.service;

import com.projeto1cc.grevia.care.model.CarePlan;
import com.projeto1cc.grevia.care.model.CareRecord;
import com.projeto1cc.grevia.care.dto.CareRecordRequestDTO;
import com.projeto1cc.grevia.care.dto.CareRecordResponseDTO;
import com.projeto1cc.grevia.care.mapper.CareRecordMapper;
import com.projeto1cc.grevia.care.repository.CarePlanRepository;
import com.projeto1cc.grevia.care.repository.CareRecordRepository;
import com.projeto1cc.grevia.user.model.User;
import com.projeto1cc.grevia.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CareRecordService {

    private final CareRecordRepository careRecordRepository;
    private final CarePlanRepository carePlanRepository;
    private final CareRecordMapper careRecordMapper;
    private final CarePlanService carePlanService;
    private final UserRepository userRepository;

    @Transactional
    public CareRecordResponseDTO createCareRecord(Long carePlanId, CareRecordRequestDTO requestDTO, String userEmail) {
        CarePlan carePlan = carePlanRepository.findById(carePlanId)
                .orElseThrow(() -> new RuntimeException("Plano de cuidado não encontrado"));

        User user = carePlan.getPlant().getUser();
        if (!user.getEmail().equals(userEmail)) {
            throw new RuntimeException("Você não tem permissão para adicionar registros a este plano de cuidados");
        }

        CareRecord careRecord = careRecordMapper.toEntity(requestDTO);
        careRecord.setCarePlan(carePlan);
        
        LocalDate recordDate = requestDTO.careDate() != null ? requestDTO.careDate() : LocalDate.now();
        careRecord.setCareDate(recordDate);

        // Save Record
        CareRecord savedRecord = careRecordRepository.save(careRecord);

        // Calculate basic difference in days to determine if it was "on time"
        LocalDate scheduledDate = carePlan.getNextCareDate();
        int daysDifference = (scheduledDate != null) ?
                Math.abs((int) java.time.temporal.ChronoUnit.DAYS.between(scheduledDate, recordDate)) : 0;

        // Reschedule Next Care Date on the CarePlan
        carePlan.setNextCareDate(carePlanService.calculateNextDate(recordDate, carePlan.getFrequencyType()));
        carePlanRepository.save(carePlan);

        // Update User Metrics (Streak and Gamification Points)
        updateUserMetrics(user, recordDate, daysDifference);

        return careRecordMapper.toResponseDTO(savedRecord);
    }

    @Transactional(readOnly = true)
    public List<CareRecordResponseDTO> getCareRecordsByPlanId(Long carePlanId, String userEmail) {
        CarePlan carePlan = carePlanRepository.findById(carePlanId)
                .orElseThrow(() -> new RuntimeException("Plano de cuidado não encontrado"));

        if (!carePlan.getPlant().getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Você não tem permissão para ver os registros deste plano de cuidado");
        }

        return careRecordRepository.findByCarePlanId(carePlanId).stream()
                .map(careRecordMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    private void updateUserMetrics(User user, LocalDate recordDate, int daysDifferenceFromSchedule) {
        LocalDate lastDate = user.getLastCareDate();

        // Total Actions always increment
        user.setTotalCareActions(user.getTotalCareActions() != null ? user.getTotalCareActions() + 1 : 1);

        // Advanced Gamification points system:
        int earnedPoints = 10; // Base points for caring
        if (daysDifferenceFromSchedule == 0) {
            earnedPoints += 15; // +15 Bonus for exactly on the right day!
        } else if (daysDifferenceFromSchedule <= 2) {
            earnedPoints += 5; // +5 Bonus for being close (1-2 days off)
        }

        // Streak logic (daily care)
        if (lastDate == null) {
            user.setCurrentStreak(1);
        } else if (recordDate.isEqual(lastDate.plusDays(1))) {
            user.setCurrentStreak(user.getCurrentStreak() + 1);
            earnedPoints += (user.getCurrentStreak() * 2); // Multiplier bonus for high streaks!
        } else if (recordDate.isAfter(lastDate.plusDays(1))) {
            user.setCurrentStreak(1); // Reset streak if missed a day of overall care
        }
        
        user.setTotalPoints((user.getTotalPoints() != null ? user.getTotalPoints() : 0) + earnedPoints);
        user.setLastCareDate(recordDate);
        userRepository.save(user);
    }
}
