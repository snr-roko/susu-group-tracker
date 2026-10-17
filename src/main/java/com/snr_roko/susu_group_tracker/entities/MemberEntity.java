package com.snr_roko.susu_group_tracker.entities;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity 
@Table(name="members")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE) 
public class MemberEntity {

    @Id 
    @GeneratedValue(strategy=GenerationType.UUID)
    UUID id;

    @Column(nullable=false, unique=true)
    String name;

    @Column(nullable=false)
    String phoneNumber;

    @JoinColumn(name="group_id", nullable=false)
    @ManyToOne(optional = false)
    GroupEntity group;

    @JoinColumn(name="account_id", nullable=false)
    MemberAccountEntity memberAccount;

    @CreationTimestamp 
    Instant createdAt;

    @UpdateTimestamp 
    Instant modifiedAt;

}
