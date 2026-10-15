package com.snr_roko.susu_group_tracker.entities;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity 
@Table(name="collection_periods")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@FieldDefaults(level=AccessLevel.PRIVATE)
public class MemberAccountEntity {

    @Id 
    @GeneratedValue(strategy=GenerationType.UUID)
    UUID id;
    
    @OneToOne
    @JoinColumn(name="member_id", nullable=false)
    MemberEntity member;

    @Column(nullable=false, precision=19, scale=2)
    BigDecimal balance = BigDecimal.ZERO;
}
