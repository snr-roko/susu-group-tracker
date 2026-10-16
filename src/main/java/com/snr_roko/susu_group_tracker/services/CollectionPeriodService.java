package com.snr_roko.susu_group_tracker.services;

import java.time.LocalDate;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.snr_roko.susu_group_tracker.dtos.CollectionPeriodRequestDto;
import com.snr_roko.susu_group_tracker.dtos.CollectionPeriodResponseDto;
import com.snr_roko.susu_group_tracker.entities.CollectionPeriodEntity;
import com.snr_roko.susu_group_tracker.repositories.CollectionPeriodRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class CollectionPeriodService {


    private final ModelMapper modelMapper;
    private final CollectionPeriodRepository collectionPeriodRepository;

    public CollectionPeriodResponseDto startCollectionPeriod(CollectionPeriodRequestDto collectionPeriodDetails) {
        CollectionPeriodEntity newCollectionPeriod = new CollectionPeriodEntity();

        newCollectionPeriod.setPeriodStart(collectionPeriodDetails.getPeriodStart());
        newCollectionPeriod.setPeriodEnd(collectionPeriodDetails.getPeriodEnd());

        newCollectionPeriod.setNumberOfWeeks(calculateNumberOfWeeks(collectionPeriodDetails.getPeriodStart(), collectionPeriodDetails.getPeriodEnd()));
        
        return modelMapper.map(collectionPeriodRepository.save(newCollectionPeriod), CollectionPeriodResponseDto.class);
    }

    private Integer calculateNumberOfWeeks(LocalDate periodStart, LocalDate periodEnd) {
        
        return (int) (periodEnd.toEpochDay() - periodStart.toEpochDay()) / 7;
    }
}
