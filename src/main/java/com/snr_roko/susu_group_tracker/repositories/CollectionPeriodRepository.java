package com.snr_roko.susu_group_tracker.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.snr_roko.susu_group_tracker.entities.CollectionPeriodEntity;

public interface CollectionPeriodRepository extends JpaRepository<CollectionPeriodEntity, UUID> {

}
