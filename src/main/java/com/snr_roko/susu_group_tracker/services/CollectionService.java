package com.snr_roko.susu_group_tracker.services;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.snr_roko.susu_group_tracker.configs.exceptions.ResourceNotFoundException;
import com.snr_roko.susu_group_tracker.dtos.CollectionResponseDto;
import com.snr_roko.susu_group_tracker.entities.CollectionPeriodEntity;
import com.snr_roko.susu_group_tracker.entities.CollectionWeekEntity;
import com.snr_roko.susu_group_tracker.repositories.CollectionPeriodRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class CollectionService {

    private final ModelMapper modelMapper;
    private final CollectionPeriodRepository collectionPeriodRepository;

    public CollectionResponseDto startCollectionWeek(UUID collectionPeriodId) {
        CollectionPeriodEntity collectionPeriod = collectionPeriodRepository.findById(collectionPeriodId).orElseThrow(() -> new ResourceNotFoundException("Period Not found"));
        
        CollectionWeekEntity collectionWeek = new CollectionWeekEntity();
        collectionWeek.setCollectionPeriod(collectionPeriod);
        Integer previousCollectionWeek = collectionPeriod.getActiveWeek();
        collectionWeek.setWeekNumber(previousCollectionWeek + 1);
        collectionPeriod.setActiveWeek(previousCollectionWeek + 1);
        collectionPeriodRepository.save(collectionPeriod);
        
        return modelMapper.map(collectionWeek, CollectionResponseDto.class);
    }

}
