package com.snr_roko.susu_group_tracker.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.snr_roko.susu_group_tracker.entities.GroupEntity;

@Repository 
public interface GroupRepository extends JpaRepository<GroupEntity, UUID>{

}
