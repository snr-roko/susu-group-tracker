package com.snr_roko.susu_group_tracker.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import lombok.Data;

@Data
public class CollectionPeriodRequestDto {

    LocalDate periodStart;

    LocalDate periodEnd;

    BigDecimal weeklyAmount;

    UUID groupId;
}
