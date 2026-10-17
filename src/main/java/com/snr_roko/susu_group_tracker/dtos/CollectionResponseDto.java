package com.snr_roko.susu_group_tracker.dtos;

import java.time.Instant;
import java.util.UUID;

import lombok.Data;

@Data 
public class CollectionResponseDto {

    UUID id;

    Integer weekNumber;

    Boolean completed = false;

    Instant createdAt;
}
