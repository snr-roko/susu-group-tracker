package com.snr_roko.susu_group_tracker.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.snr_roko.susu_group_tracker.entities.CollectionWeekEntity;

@Repository 
public interface CollectionWeekRepository extends JpaRepository<CollectionWeekEntity, UUID>{


}
