package com.snr_roko.susu_group_tracker.dtos;

import java.time.Instant;
import java.util.UUID;


import lombok.Data;

@Data 
public class GroupResponseDto {

    private UUID id;

    private String name;

    private String cordinatorName;

    private Instant createdAt;
}
