package com.snr_roko.susu_group_tracker.dtos;

import java.util.UUID;

import lombok.Data;

@Data 
public class CollectionWeekRequestDto {

    private UUID collectionPeriodId;
    private UUID beneficiaryId;
}
