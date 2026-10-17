package com.snr_roko.susu_group_tracker.services;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.snr_roko.susu_group_tracker.configs.exceptions.ResourceNotFoundException;
import com.snr_roko.susu_group_tracker.dtos.MemberRequestDto;
import com.snr_roko.susu_group_tracker.dtos.MemberResponseDto;
import com.snr_roko.susu_group_tracker.entities.MemberAccountEntity;
import com.snr_roko.susu_group_tracker.entities.MemberEntity;
import com.snr_roko.susu_group_tracker.repositories.GroupRepository;
import com.snr_roko.susu_group_tracker.repositories.MemberAccountRepository;
import com.snr_roko.susu_group_tracker.repositories.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor  
public class MemberService {

    private final MemberRepository memberRepository;
    private final GroupRepository groupRepository;
    private final ModelMapper modelMapper;
    private final MemberAccountRepository memberAccountRepository;

    public MemberResponseDto addMember(MemberRequestDto memberDetails) {
        MemberEntity newMember = new MemberEntity();
        
        newMember.setName(memberDetails.getName());
        newMember.setPhoneNumber(memberDetails.getPhoneNumber());
        newMember.setGroup(groupRepository.findById(memberDetails.getGroupId()).orElseThrow(() -> new ResourceNotFoundException("Group not found")));
        
        // create a new member account for the member
        MemberAccountEntity memberAccount = new MemberAccountEntity();
        memberAccount.setMember(newMember);

        newMember.setMemberAccount(memberAccountRepository.save(memberAccount));

        return modelMapper.map(memberRepository.save(newMember), MemberResponseDto.class);
    }
}
