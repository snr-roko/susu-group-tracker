package com.snr_roko.susu_group_tracker.dtos;

import java.time.LocalDate;

import lombok.Data;

@Data
public class CollectionPeriodRequestDto {

    LocalDate periodStart;

    LocalDate periodEnd;
}
