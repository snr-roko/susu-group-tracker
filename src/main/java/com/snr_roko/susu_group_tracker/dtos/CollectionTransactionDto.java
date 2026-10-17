package com.snr_roko.susu_group_tracker.dtos;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Data;

@Data 
public class CollectionTransactionDto {

    private UUID collectionWeekId;
    private UUID memberAccountId;
    private BigDecimal amount;
}
