package com.snr_roko.susu_group_tracker.entities;

import java.time.Instant;
import java.util.List;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity 
@Table(name="collection_weeks")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@FieldDefaults(level=AccessLevel.PRIVATE)
public class CollectionWeekEntity {

    @Id 
    @GeneratedValue(strategy=GenerationType.UUID)
    UUID id;

    @Column(nullable=false)
    Integer weekNumber;

    @JoinColumn(name="collection_period_id", nullable=false)
    @ManyToOne(optional = false)
    CollectionPeriodEntity collectionPeriod;

    @Column(nullable=false)
    Boolean completed = false;

    @OneToMany(mappedBy="collectionWeek")
    List<CollectionPayments> collectionPayments;

    @OneToOne
    @JoinColumn(name="beneficiary_id", nullable=false)
    MemberEntity beneficiary;

    @CreationTimestamp 
    Instant createdAt;

    @UpdateTimestamp 
    Instant modifiedAt;

}
