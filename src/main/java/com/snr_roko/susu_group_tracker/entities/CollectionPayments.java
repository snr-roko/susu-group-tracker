package com.snr_roko.susu_group_tracker.entities;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity 
@Table(name="collection_payments")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@FieldDefaults(level=lombok.AccessLevel.PRIVATE)
public class CollectionPayments {

    @Id 
    @GeneratedValue(strategy=GenerationType.UUID)
    UUID id;

    @ManyToOne 
    @JoinColumn(name="collection_week_id")
    CollectionWeekEntity collectionWeek;

    @ManyToOne 
    @JoinColumn(name="member_id")
    MemberEntity memberAccount;

    @Column(nullable=false)
    BigDecimal amount;
}
