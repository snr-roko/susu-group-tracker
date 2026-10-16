package com.snr_roko.susu_group_tracker.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.snr_roko.susu_group_tracker.entities.MemberEntity;

@Repository 
public interface MemberRepository extends JpaRepository<MemberEntity, UUID>{

}
