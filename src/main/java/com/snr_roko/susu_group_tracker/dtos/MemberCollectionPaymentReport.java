package com.snr_roko.susu_group_tracker.dtos;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor  
public class MemberCollectionPaymentReport {

    private UUID memberId;
    private String name;
    private BigDecimal amountPaid;
}
