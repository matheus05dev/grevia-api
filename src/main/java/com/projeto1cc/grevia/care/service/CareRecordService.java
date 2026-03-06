package com.projeto1cc.grevia.care.service;

import com.projeto1cc.grevia.care.CarePlan;
import com.projeto1cc.grevia.care.CareRecord;
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
                .orElseThrow(() -> new RuntimeException("Care Plan not found"));

        User user = carePlan.getPlant().getUser();
        if (!user.getEmail().equals(userEmail)) {
            throw new RuntimeException("You do not have permission to add records to this care plan");
        }

        CareRecord careRecord = careRecordMapper.toEntity(requestDTO);
        careRecord.setCarePlan(carePlan);
        
        LocalDate recordDate = requestDTO.getCareDate() != null ? requestDTO.getCareDate() : LocalDate.now();
        careRecord.setCareDate(recordDate);

        // Save Record
        CareRecord savedRecord = careRecordRepository.save(careRecord);

        // Reschedule Next Care Date on the CarePlan
        carePlan.setNextCareDate(carePlanService.calculateNextDate(recordDate, carePlan.getFrequencyType()));
        carePlanRepository.save(carePlan);

        // Update User Metrics (Streak and Total Actions)
        updateUserMetrics(user, recordDate);

        return careRecordMapper.toResponseDTO(savedRecord);
    }

    @Transactional(readOnly = true)
    public List<CareRecordResponseDTO> getCareRecordsByPlanId(Long carePlanId, String userEmail) {
        CarePlan carePlan = carePlanRepository.findById(carePlanId)
                .orElseThrow(() -> new RuntimeException("Care Plan not found"));

        if (!carePlan.getPlant().getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("You do not have permission to view this care plan's records");
        }

        return careRecordRepository.findByCarePlanId(carePlanId).stream()
                .map(careRecordMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    private void updateUserMetrics(User user, LocalDate recordDate) {
        LocalDate lastDate = user.getLastCareDate();

        // Total Actions always increment
        user.setTotalCareActions(user.getTotalCareActions() != null ? user.getTotalCareActions() + 1 : 1);

        // Streak logic
        if (lastDate == null) {
            user.setCurrentStreak(1);
        } else if (recordDate.isEqual(lastDate.plusDays(1))) {
            user.setCurrentStreak(user.getCurrentStreak() + 1);
        } else if (recordDate.isAfter(lastDate.plusDays(1))) {
            user.setCurrentStreak(1); // Reset streak if missed a day
        }
        // If same day or before, keep the current streak

        user.setLastCareDate(recordDate);
        userRepository.save(user);
    }
}
