package com.snr_roko.susu_group_tracker.services;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.snr_roko.susu_group_tracker.configs.exceptions.ResourceNotFoundException;
import com.snr_roko.susu_group_tracker.dtos.CollectionResponseDto;
import com.snr_roko.susu_group_tracker.dtos.CollectionWeekRequestDto;
import com.snr_roko.susu_group_tracker.entities.CollectionPeriodEntity;
import com.snr_roko.susu_group_tracker.entities.CollectionWeekEntity;
import com.snr_roko.susu_group_tracker.entities.MemberEntity;
import com.snr_roko.susu_group_tracker.repositories.CollectionPeriodRepository;
import com.snr_roko.susu_group_tracker.repositories.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class CollectionService {

    private final ModelMapper modelMapper;
    private final CollectionPeriodRepository collectionPeriodRepository;
    private final MemberRepository memberRepository;

    public CollectionResponseDto startCollectionWeek(CollectionWeekRequestDto collectionWeekCreationDetails) {
        CollectionPeriodEntity collectionPeriod = collectionPeriodRepository.findById(collectionWeekCreationDetails.getCollectionPeriodId()).orElseThrow(() -> new ResourceNotFoundException("Period Not found"));
        MemberEntity beneficiary = memberRepository.findById(collectionWeekCreationDetails.getBeneficiaryId()).orElseThrow(() -> new ResourceNotFoundException("Beneficiary Not found"));
        
        CollectionWeekEntity collectionWeek = new CollectionWeekEntity();
        collectionWeek.setCollectionPeriod(collectionPeriod);
        Integer previousCollectionWeek = collectionPeriod.getActiveWeek();
        collectionWeek.setWeekNumber(previousCollectionWeek + 1);
        collectionPeriod.setActiveWeek(previousCollectionWeek + 1);
        collectionWeek.setBeneficiary(beneficiary);
        collectionPeriodRepository.save(collectionPeriod);
        
        return modelMapper.map(collectionWeek, CollectionResponseDto.class);
    }

}
