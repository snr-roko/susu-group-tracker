package com.snr_roko.susu_group_tracker.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.snr_roko.susu_group_tracker.entities.CollectionPayments;
import com.snr_roko.susu_group_tracker.entities.CollectionWeekEntity;
import com.snr_roko.susu_group_tracker.entities.MemberEntity;

@Repository 
public interface CollectionPaymentRepository extends JpaRepository<CollectionPayments, UUID> {

    List<CollectionPayments> findByMemberAccountAndCollectionWeek(MemberEntity memberAccount,
            CollectionWeekEntity collectionWeek);

}
