package com.snr_roko.susu_group_tracker.services;

import java.time.LocalDate;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.snr_roko.susu_group_tracker.configs.exceptions.ResourceNotFoundException;
import com.snr_roko.susu_group_tracker.dtos.CollectionPeriodRequestDto;
import com.snr_roko.susu_group_tracker.dtos.CollectionPeriodResponseDto;
import com.snr_roko.susu_group_tracker.entities.CollectionPeriodEntity;
import com.snr_roko.susu_group_tracker.entities.GroupEntity;
import com.snr_roko.susu_group_tracker.repositories.CollectionPeriodRepository;
import com.snr_roko.susu_group_tracker.repositories.GroupRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class CollectionPeriodService {


    private final ModelMapper modelMapper;
    private final CollectionPeriodRepository collectionPeriodRepository;
    private final GroupRepository groupRepository;

    public CollectionPeriodResponseDto startCollectionPeriod(CollectionPeriodRequestDto collectionPeriodDetails) {
        GroupEntity group = groupRepository.findById(collectionPeriodDetails.getGroupId()).orElseThrow(() -> new ResourceNotFoundException("Group not found"));
        
        CollectionPeriodEntity newCollectionPeriod = new CollectionPeriodEntity();
        newCollectionPeriod.setGroup(group);

        newCollectionPeriod.setPeriodStart(collectionPeriodDetails.getPeriodStart());
        newCollectionPeriod.setPeriodEnd(collectionPeriodDetails.getPeriodEnd());
        newCollectionPeriod.setWeeklyAmount(collectionPeriodDetails.getWeeklyAmount());

        newCollectionPeriod.setNumberOfWeeks(calculateNumberOfWeeks(collectionPeriodDetails.getPeriodStart(), collectionPeriodDetails.getPeriodEnd()));
        
        return modelMapper.map(collectionPeriodRepository.save(newCollectionPeriod), CollectionPeriodResponseDto.class);
    }

    private Integer calculateNumberOfWeeks(LocalDate periodStart, LocalDate periodEnd) {
        
        return (int) (periodEnd.toEpochDay() - periodStart.toEpochDay()) / 7;
    }
}
