package com.snr_roko.susu_group_tracker.services;

import java.math.BigDecimal;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.snr_roko.susu_group_tracker.configs.exceptions.ResourceNotFoundException;
import com.snr_roko.susu_group_tracker.dtos.CollectionTransactionDto;
import com.snr_roko.susu_group_tracker.dtos.MemberAccountResponseDto;
import com.snr_roko.susu_group_tracker.dtos.MemberAccountTransactionDto;
import com.snr_roko.susu_group_tracker.dtos.MemberRequestDto;
import com.snr_roko.susu_group_tracker.dtos.MemberResponseDto;
import com.snr_roko.susu_group_tracker.dtos.TransactionTypeEnum;
import com.snr_roko.susu_group_tracker.entities.CollectionPayments;
import com.snr_roko.susu_group_tracker.entities.CollectionPeriodEntity;
import com.snr_roko.susu_group_tracker.entities.CollectionWeekEntity;
import com.snr_roko.susu_group_tracker.entities.MemberEntity;
import com.snr_roko.susu_group_tracker.repositories.CollectionPaymentRepository;
import com.snr_roko.susu_group_tracker.repositories.CollectionWeekRepository;
import com.snr_roko.susu_group_tracker.repositories.GroupRepository;
import com.snr_roko.susu_group_tracker.repositories.MemberRepository;
import com.snr_roko.susu_group_tracker.entities.GroupEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor  
public class MemberService {

    private final MemberRepository memberRepository;
    private final GroupRepository groupRepository;
    private final ModelMapper modelMapper;
    private final CollectionWeekRepository collectionWeekRepository;
    private final CollectionPaymentRepository collectionPaymentRepository;

    public MemberResponseDto addMember(MemberRequestDto memberDetails) {
        GroupEntity group = groupRepository.findById(memberDetails.getGroupId()).orElseThrow(() -> new ResourceNotFoundException("Group not found"));

        if(memberRepository.existsByGroupAndName(group, memberDetails.getName())){
            throw new IllegalArgumentException("Member already exists in this group");
        }

        MemberEntity newMember = new MemberEntity();
        newMember.setName(memberDetails.getName());
        newMember.setPhoneNumber(memberDetails.getPhoneNumber());
        newMember.setGroup(group);

        return modelMapper.map(memberRepository.save(newMember), MemberResponseDto.class);
    }

    public MemberAccountResponseDto depositMoneyIntoAccount(MemberAccountTransactionDto transactionDetails) {

        if(transactionDetails.getTransactionType() != TransactionTypeEnum.DEPOSIT) {
            throw new IllegalArgumentException("Transaction type must be DEPOSIT");
        }

        MemberEntity memberAccount = memberRepository.findById(transactionDetails.getMemberId()).orElseThrow(() -> new ResourceNotFoundException("Member account not found"));
        
        memberAccount.setAccountBalance(transact(transactionDetails.getTransactionType(), transactionDetails.getAmount(), memberAccount.getAccountBalance()));

        return modelMapper.map(memberRepository.save(memberAccount), MemberAccountResponseDto.class);
    }

    public MemberAccountResponseDto withdrawMoneyFromAccount(MemberAccountTransactionDto transactionDetails) {

        if(transactionDetails.getTransactionType() != TransactionTypeEnum.WITHDRAWAL) {
            throw new IllegalArgumentException("Transaction type must be WITHDRAWAL");
        }

        MemberEntity memberAccount = memberRepository.findById(transactionDetails.getMemberId()).orElseThrow(() -> new ResourceNotFoundException("Member account not found"));
        
        memberAccount.setAccountBalance(transact(transactionDetails.getTransactionType(), transactionDetails.getAmount(), memberAccount.getAccountBalance()));

        return modelMapper.map(memberRepository.save(memberAccount), MemberAccountResponseDto.class);
    }

    public MemberAccountResponseDto checkBalance(MemberAccountTransactionDto transactionDetails) {

        if(transactionDetails.getTransactionType() != TransactionTypeEnum.BALANCE_CHECK) {
            throw new IllegalArgumentException("Transaction type must be BALANCE_CHECK");
        }

        MemberEntity memberAccount = memberRepository.findById(transactionDetails.getMemberId()).orElseThrow(() -> new ResourceNotFoundException("Member account not found"));
        
        return modelMapper.map(memberAccount, MemberAccountResponseDto.class);
    }

    public MemberAccountResponseDto payCollection(CollectionTransactionDto collectionTransactionDetails) {
        MemberEntity memberAccount = memberRepository.findById(collectionTransactionDetails.getMemberAccountId()).orElseThrow(() -> new ResourceNotFoundException("Member account not found"));

        if(collectionTransactionDetails.getAmount() == null || collectionTransactionDetails.getAmount().compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Payment amount must be greater than zero");
        }

        // check if the member has enough balance
        if(memberAccount.getAccountBalance().compareTo(collectionTransactionDetails.getAmount()) < 0) {
            throw new IllegalArgumentException("Member account balance is not enough");
        }

        // get the period
        CollectionWeekEntity collectionWeek = collectionWeekRepository.findById(collectionTransactionDetails.getCollectionWeekId()).orElseThrow(() -> new ResourceNotFoundException("Collection period not found"));
        CollectionPeriodEntity collectionPeriod = collectionWeek.getCollectionPeriod();

        // get the weekly amount
        BigDecimal weeklyAmount = collectionPeriod.getWeeklyAmount();

        // get payments made by the member
        List<CollectionPayments> collectionPaymentsByMemberForPeriod = collectionPaymentRepository.findByMemberAccountAndCollectionWeek(memberAccount, collectionWeek);

        // get total paid
        BigDecimal totalPaid = collectionPaymentsByMemberForPeriod.stream().map(CollectionPayments::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal amountPayable = weeklyAmount.subtract(totalPaid);

        // check if the amount paid is greater than the amount payable
        if(amountPayable.compareTo(collectionTransactionDetails.getAmount()) < 0) {
            throw new IllegalArgumentException("Amount paid is greater than the amount payable");
        }

        memberAccount.setAccountBalance(transact(TransactionTypeEnum.WITHDRAWAL, collectionTransactionDetails.getAmount(), memberAccount.getAccountBalance()));

        // get the beneficiary
        MemberEntity beneficiary = collectionWeek.getBeneficiary();

        // update beneficiary balance
        beneficiary.setAccountBalance(transact(TransactionTypeEnum.DEPOSIT, collectionTransactionDetails.getAmount(), beneficiary.getAccountBalance()));

        return modelMapper.map(memberRepository.save(memberAccount), MemberAccountResponseDto.class);
    }

    public BigDecimal transact(TransactionTypeEnum transactionType, BigDecimal amount, BigDecimal balance) {

        if (transactionType == TransactionTypeEnum.DEPOSIT) {
            balance = balance.add(amount);
        }

        if (transactionType == TransactionTypeEnum.WITHDRAWAL) {
            balance = balance.subtract(amount);
        }

        return balance;

    }
}
