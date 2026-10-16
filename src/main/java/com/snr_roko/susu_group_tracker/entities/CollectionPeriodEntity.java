package com.snr_roko.susu_group_tracker.entities;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class CollectionPeriodEntity {

    @Id 
    @GeneratedValue(strategy=GenerationType.UUID)
    UUID id;

    @Column(nullable=false)
    LocalDate periodStart;

    @Column(nullable=false)
    LocalDate periodEnd;

    @Column(nullable=false)
    Integer numberOfWeeks;

    @Column(nullable=false)
    Integer activeWeek = 1;

    @Column(nullable=false)
    Boolean completed = false;

    @CreationTimestamp 
    Instant createdAt;

    @UpdateTimestamp 
    Instant modifiedAt;
}
