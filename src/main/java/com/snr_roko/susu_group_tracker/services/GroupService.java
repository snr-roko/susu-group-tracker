package com.snr_roko.susu_group_tracker.services;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.snr_roko.susu_group_tracker.dtos.GroupCreationDto;
import com.snr_roko.susu_group_tracker.dtos.GroupResponseDto;
import com.snr_roko.susu_group_tracker.entities.GroupEntity;
import com.snr_roko.susu_group_tracker.repositories.GroupRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class GroupService {

    private final GroupRepository groupRepository;
    private final ModelMapper modelMapper;

    public GroupResponseDto createGroup(GroupCreationDto groupDetails) {

        GroupEntity newGroup = new GroupEntity();
        newGroup.setName(groupDetails.getName());

        return modelMapper.map(groupRepository.save(newGroup), GroupResponseDto.class);
    }
}
