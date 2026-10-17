package com.snr_roko.susu_group_tracker;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.snr_roko.susu_group_tracker.configs.exceptions.ResourceNotFoundException;
import com.snr_roko.susu_group_tracker.dtos.CollectionPeriodRequestDto;
import com.snr_roko.susu_group_tracker.dtos.CollectionTransactionDto;
import com.snr_roko.susu_group_tracker.dtos.CollectionWeekRequestDto;
import com.snr_roko.susu_group_tracker.dtos.GroupCreationDto;
import com.snr_roko.susu_group_tracker.dtos.GroupResponseDto;
import com.snr_roko.susu_group_tracker.dtos.MemberAccountTransactionDto;
import com.snr_roko.susu_group_tracker.dtos.MemberRequestDto;
import com.snr_roko.susu_group_tracker.dtos.MemberResponseDto;
import com.snr_roko.susu_group_tracker.dtos.TransactionTypeEnum;
import com.snr_roko.susu_group_tracker.services.CollectionPeriodService;
import com.snr_roko.susu_group_tracker.services.CollectionService;
import com.snr_roko.susu_group_tracker.services.GroupService;
import com.snr_roko.susu_group_tracker.services.MemberService;

@SpringBootTest
@Transactional
class CollectionTrackerTest {

    @Autowired
    GroupService groupService;

    @Autowired
    MemberService memberService;

    @Autowired
    CollectionPeriodService periodService;

    @Autowired
    CollectionService collectionService;

    private UUID[] setupGroup() {
        GroupCreationDto g = new GroupCreationDto();
        g.setName("Susu Group");
        GroupResponseDto group = groupService.createGroup(g);
        UUID groupId = group.getId();

        MemberRequestDto m1 = new MemberRequestDto();
        m1.setName("Alice");
        m1.setPhoneNumber("0100000001");
        m1.setGroupId(groupId);
        MemberResponseDto alice = memberService.addMember(m1);

        MemberRequestDto m2 = new MemberRequestDto();
        m2.setName("Bob");
        m2.setPhoneNumber("0100000002");
        m2.setGroupId(groupId);
        MemberResponseDto bob = memberService.addMember(m2);

        MemberAccountTransactionDto dep = new MemberAccountTransactionDto();
        dep.setAmount(new BigDecimal("1000.00"));
        dep.setTransactionType(TransactionTypeEnum.DEPOSIT);

        dep.setMemberId(alice.getId());
        memberService.depositMoneyIntoAccount(dep);
        dep.setMemberId(bob.getId());
        memberService.depositMoneyIntoAccount(dep);

        return new UUID[]{groupId, alice.getId(), bob.getId()};
    }

    @Test
    void allMembersPayInFullWeekCloses() {
        UUID[] ids = setupGroup();
        UUID groupId = ids[0];
        UUID alice = ids[1];
        UUID bob = ids[2];

        CollectionPeriodRequestDto p = new CollectionPeriodRequestDto();
        p.setPeriodStart(LocalDate.of(2026, 1, 1));
        p.setPeriodEnd(LocalDate.of(2026, 2, 1));
        p.setWeeklyAmount(new BigDecimal("50.00"));
        p.setGroupId(groupId);
        UUID periodId = periodService.startCollectionPeriod(p).getId();

        CollectionWeekRequestDto w = new CollectionWeekRequestDto();
        w.setCollectionPeriodId(periodId);
        w.setBeneficiaryId(alice);
        UUID weekId = collectionService.startCollectionWeek(w).getId();

        CollectionTransactionDto tx = new CollectionTransactionDto();
        tx.setCollectionWeekId(weekId);
        tx.setAmount(new BigDecimal("50.00"));

        tx.setMemberAccountId(alice);
        memberService.payCollection(tx);
        tx.setMemberAccountId(bob);
        memberService.payCollection(tx);

        assertDoesNotThrow(() -> collectionService.endCollectionWeek(weekId));
    }

    @Test
    void partialPaymentWeekCannotCloseReportsOwed() {
        UUID[] ids = setupGroup();
        UUID groupId = ids[0];
        UUID alice = ids[1];
        UUID bob = ids[2];

        CollectionPeriodRequestDto p = new CollectionPeriodRequestDto();
        p.setPeriodStart(LocalDate.of(2026, 1, 1));
        p.setPeriodEnd(LocalDate.of(2026, 2, 1));
        p.setWeeklyAmount(new BigDecimal("50.00"));
        p.setGroupId(groupId);
        UUID periodId = periodService.startCollectionPeriod(p).getId();

        CollectionWeekRequestDto w = new CollectionWeekRequestDto();
        w.setCollectionPeriodId(periodId);
        w.setBeneficiaryId(alice);
        UUID weekId = collectionService.startCollectionWeek(w).getId();

        CollectionTransactionDto tx = new CollectionTransactionDto();
        tx.setCollectionWeekId(weekId);
        tx.setAmount(new BigDecimal("30.00"));

        tx.setMemberAccountId(alice);
        memberService.payCollection(tx);

        tx.setMemberAccountId(bob);
        tx.setAmount(new BigDecimal("50.00"));
        memberService.payCollection(tx);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> collectionService.endCollectionWeek(weekId));

        assertTrue(ex.getMessage().contains("Alice"), "message should name the member who owes: " + ex.getMessage());
        assertTrue(ex.getMessage().contains("20"), "message should state how much is owed: " + ex.getMessage());
    }

    @Test
    void rejectZeroAmountPayment() {
        UUID[] ids = setupGroup();
        UUID periodId = createPeriod(ids[0]);
        UUID weekId = startWeekByPeriod(periodId, ids[1]);

        CollectionTransactionDto tx = new CollectionTransactionDto();
        tx.setCollectionWeekId(weekId);
        tx.setMemberAccountId(ids[1]);
        tx.setAmount(BigDecimal.ZERO);

        assertThrows(IllegalArgumentException.class, () -> memberService.payCollection(tx));
    }

    @Test
    void rejectNegativeAmountPayment() {
        UUID[] ids = setupGroup();
        UUID periodId = createPeriod(ids[0]);
        UUID weekId = startWeekByPeriod(periodId, ids[1]);

        CollectionTransactionDto tx = new CollectionTransactionDto();
        tx.setCollectionWeekId(weekId);
        tx.setMemberAccountId(ids[1]);
        tx.setAmount(new BigDecimal("-10.00"));

        assertThrows(IllegalArgumentException.class, () -> memberService.payCollection(tx));
    }

    @Test
    void rejectPaymentAboveOwed() {
        UUID[] ids = setupGroup();
        UUID periodId = createPeriod(ids[0]);
        UUID weekId = startWeekByPeriod(periodId, ids[1]);

        CollectionTransactionDto tx = new CollectionTransactionDto();
        tx.setCollectionWeekId(weekId);
        tx.setMemberAccountId(ids[1]);
        tx.setAmount(new BigDecimal("60.00"));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> memberService.payCollection(tx));
        assertTrue(ex.getMessage().contains("payable"));
    }

    @Test
    void rejectUnknownMemberPayment() {
        UUID[] ids = setupGroup();
        UUID periodId = createPeriod(ids[0]);
        UUID weekId = startWeekByPeriod(periodId, ids[1]);

        CollectionTransactionDto tx = new CollectionTransactionDto();
        tx.setCollectionWeekId(weekId);
        tx.setMemberAccountId(UUID.randomUUID());
        tx.setAmount(new BigDecimal("50.00"));

        assertThrows(ResourceNotFoundException.class, () -> memberService.payCollection(tx));
    }

    @Test
    void rejectDuplicateMember() {
        UUID[] ids = setupGroup();
        UUID groupId = ids[0];

        MemberRequestDto dup = new MemberRequestDto();
        dup.setName("Alice");
        dup.setPhoneNumber("0100000003");
        dup.setGroupId(groupId);

        assertThrows(IllegalArgumentException.class, () -> memberService.addMember(dup));
    }

    private UUID createPeriod(UUID groupId) {
        CollectionPeriodRequestDto p = new CollectionPeriodRequestDto();
        p.setPeriodStart(LocalDate.of(2026, 1, 1));
        p.setPeriodEnd(LocalDate.of(2026, 2, 1));
        p.setWeeklyAmount(new BigDecimal("50.00"));
        p.setGroupId(groupId);
        return periodService.startCollectionPeriod(p).getId();
    }

    private UUID startWeekByPeriod(UUID periodId, UUID beneficiaryId) {
        CollectionWeekRequestDto w = new CollectionWeekRequestDto();
        w.setCollectionPeriodId(periodId);
        w.setBeneficiaryId(beneficiaryId);
        return collectionService.startCollectionWeek(w).getId();
    }
}
