package com.snr_roko.susu_group_tracker.dtos;

import java.time.Instant;
import java.util.UUID;

import lombok.Data;

@Data 
public class MemberResponseDto {

    private UUID id;
    private String name;
    private String phoneNumber;
    private Instant createdAt;
}
