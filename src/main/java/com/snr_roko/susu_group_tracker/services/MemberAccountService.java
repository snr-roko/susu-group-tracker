package com.snr_roko.susu_group_tracker.services;

import java.math.BigDecimal;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.snr_roko.susu_group_tracker.configs.exceptions.ResourceNotFoundException;
import com.snr_roko.susu_group_tracker.dtos.MemberAccountResponseDto;
import com.snr_roko.susu_group_tracker.dtos.MemberAccountTransactionDto;
import com.snr_roko.susu_group_tracker.dtos.TransactionTypeEnum;
import com.snr_roko.susu_group_tracker.entities.MemberAccountEntity;
import com.snr_roko.susu_group_tracker.repositories.MemberAccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor  
public class MemberAccountService {

    private final MemberAccountRepository memberAccountRepository;
    private final ModelMapper modelMapper;

    public MemberAccountResponseDto depositMoneyIntoAccount(MemberAccountTransactionDto transactionDetails) {

        if(transactionDetails.getTransactionType() != TransactionTypeEnum.DEPOSIT) {
            throw new IllegalArgumentException("Transaction type must be DEPOSIT");
        }

        MemberAccountEntity memberAccount = memberAccountRepository.findById(transactionDetails.getMemberAccountId()).orElseThrow(() -> new ResourceNotFoundException("Member account not found"));
        
        memberAccount.setBalance(transact(transactionDetails.getTransactionType(), transactionDetails.getAmount(), memberAccount.getBalance()));

        return modelMapper.map(memberAccountRepository.save(memberAccount), MemberAccountResponseDto.class);
    }

    public MemberAccountResponseDto withdrawMoneyFromAccount(MemberAccountTransactionDto transactionDetails) {

        if(transactionDetails.getTransactionType() != TransactionTypeEnum.WITHDRAWAL) {
            throw new IllegalArgumentException("Transaction type must be WITHDRAWAL");
        }

        MemberAccountEntity memberAccount = memberAccountRepository.findById(transactionDetails.getMemberAccountId()).orElseThrow(() -> new ResourceNotFoundException("Member account not found"));
        
        memberAccount.setBalance(transact(transactionDetails.getTransactionType(), transactionDetails.getAmount(), memberAccount.getBalance()));

        return modelMapper.map(memberAccountRepository.save(memberAccount), MemberAccountResponseDto.class);
    }

    public MemberAccountResponseDto checkBalance(MemberAccountTransactionDto transactionDetails) {

        if(transactionDetails.getTransactionType() != TransactionTypeEnum.BALANCE_CHECK) {
            throw new IllegalArgumentException("Transaction type must be BALANCE_CHECK");
        }

        MemberAccountEntity memberAccount = memberAccountRepository.findById(transactionDetails.getMemberAccountId()).orElseThrow(() -> new ResourceNotFoundException("Member account not found"));
        
        return modelMapper.map(memberAccount, MemberAccountResponseDto.class);
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
