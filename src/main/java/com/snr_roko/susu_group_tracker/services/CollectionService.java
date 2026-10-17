package com.snr_roko.susu_group_tracker.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.snr_roko.susu_group_tracker.configs.exceptions.ResourceNotFoundException;
import com.snr_roko.susu_group_tracker.dtos.CollectionResponseDto;
import com.snr_roko.susu_group_tracker.dtos.CollectionWeekRequestDto;
import com.snr_roko.susu_group_tracker.dtos.MemberCollectionPaymentReport;
import com.snr_roko.susu_group_tracker.entities.CollectionPayments;
import com.snr_roko.susu_group_tracker.entities.CollectionPeriodEntity;
import com.snr_roko.susu_group_tracker.entities.CollectionWeekEntity;
import com.snr_roko.susu_group_tracker.entities.GroupEntity;
import com.snr_roko.susu_group_tracker.entities.MemberEntity;
import com.snr_roko.susu_group_tracker.repositories.CollectionPaymentRepository;
import com.snr_roko.susu_group_tracker.repositories.CollectionPeriodRepository;
import com.snr_roko.susu_group_tracker.repositories.CollectionWeekRepository;
import com.snr_roko.susu_group_tracker.repositories.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class CollectionService {

    private final ModelMapper modelMapper;
    private final CollectionPeriodRepository collectionPeriodRepository;
    private final MemberRepository memberRepository;
    private final CollectionPaymentRepository collectionPaymentRepository;
    private final CollectionWeekRepository collectionWeekRepository;

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

    public CollectionResponseDto endCollectionWeek(UUID collectionWeekId) {

        CollectionWeekEntity collectionWeek = collectionWeekRepository.findById(collectionWeekId).orElseThrow(() -> new ResourceNotFoundException("Collection Week Not found"));
        
        if(collectionWeek.getCompleted()){
            throw new IllegalArgumentException("Collection week already completed");
        }

        List<MemberCollectionPaymentReport> memberCollectionReport = getMembersCollectionReport(collectionWeek);

        CollectionPeriodEntity collectionPeriod = collectionWeek.getCollectionPeriod();
        Boolean completed = memberCollectionReport.stream().allMatch(report -> report.getAmountPaid().compareTo(collectionPeriod.getWeeklyAmount()) >= 0);
        
        if(!completed){
            StringBuilder sb = new StringBuilder("Week cannot be closed. Some members are owing: ");
            for(MemberCollectionPaymentReport report : memberCollectionReport){
                BigDecimal owed = collectionPeriod.getWeeklyAmount().subtract(report.getAmountPaid());
                if(owed.compareTo(BigDecimal.ZERO) > 0){
                    sb.append(report.getName()).append(" owes GHS ").append(owed).append("; ");
                }
            }
            throw new IllegalArgumentException(sb.toString());
        }
        
        collectionWeek.setCompleted(completed);
        collectionWeekRepository.save(collectionWeek);
        return modelMapper.map(collectionWeek, CollectionResponseDto.class);
    }

    public List<MemberCollectionPaymentReport> getMembersCollectionReport(CollectionWeekEntity collectionWeek) {
        // Get members of the group
        
        CollectionPeriodEntity collectionPeriod = collectionWeek.getCollectionPeriod();
        GroupEntity group = collectionPeriod.getGroup();
        List<MemberEntity> members = group.getMembers();

        return members.stream()
            .map(member -> {
                List<CollectionPayments> collectionPayments = collectionPaymentRepository.findByMemberAccountAndCollectionWeek(member, collectionWeek);
                BigDecimal amountPaid = collectionPayments.stream().map(CollectionPayments::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                return new MemberCollectionPaymentReport(member.getId(), member.getName(), amountPaid);
            })
            .toList();

    }

}
