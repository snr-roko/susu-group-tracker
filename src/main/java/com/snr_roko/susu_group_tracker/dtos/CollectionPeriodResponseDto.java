package com.snr_roko.susu_group_tracker.dtos;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;


import lombok.Data;

@Data 
public class CollectionPeriodResponseDto {

    UUID id;

    LocalDate periodStart;

    LocalDate periodEnd;

    Integer numberOfWeeks;

    Integer activeWeek;

    Boolean completed;

    Instant createdAt;

}
