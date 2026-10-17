package com.snr_roko.susu_group_tracker.entities;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "groups")
@Getter 
@Setter
@NoArgsConstructor 
@AllArgsConstructor 
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GroupEntity {

    @Id 
    @GeneratedValue(strategy=GenerationType.UUID)
    UUID id;

    @Column(nullable=false, unique=true)
    String name;

    String cordinatorName;

    @OneToMany(mappedBy = "group")
    List<CollectionPeriodEntity> collectionPeriods = new ArrayList<>();

    @OneToMany(mappedBy = "group", fetch = FetchType.EAGER)
    List<MemberEntity> members = new ArrayList<>();

    @CreationTimestamp 
    Instant createdAt;

    @UpdateTimestamp 
    Instant modifiedAt;
}
