package com.mzc.lms.enrollment.adapter.out.persistence.entity;

import com.mzc.lms.enrollment.domain.model.EnrollmentHistory;
import com.mzc.lms.enrollment.domain.model.EnrollmentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "enrollment_histories",
        indexes = {
                @Index(name = "idx_history_enrollment", columnList = "enrollment_id"),
                @Index(name = "idx_history_changed_at", columnList = "changed_at")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollmentHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "enrollment_id", nullable = false)
    private Long enrollmentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "previous_status", length = 20)
    private EnrollmentStatus previousStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "new_status", nullable = false, length = 20)
    private EnrollmentStatus newStatus;

    @Column(name = "changed_by", length = 100)
    private String changedBy;

    @Column(name = "change_reason", length = 500)
    private String changeReason;

    @CreationTimestamp
    @Column(name = "changed_at", nullable = false, updatable = false)
    private LocalDateTime changedAt;

    public static EnrollmentHistoryEntity fromDomain(EnrollmentHistory history) {
        return EnrollmentHistoryEntity.builder()
                .id(history.getId())
                .enrollmentId(history.getEnrollmentId())
                .previousStatus(history.getPreviousStatus())
                .newStatus(history.getNewStatus())
                .changedBy(history.getChangedBy())
                .changeReason(history.getChangeReason())
                .changedAt(history.getChangedAt())
                .build();
    }

    public EnrollmentHistory toDomain() {
        return EnrollmentHistory.builder()
                .id(this.id)
                .enrollmentId(this.enrollmentId)
                .previousStatus(this.previousStatus)
                .newStatus(this.newStatus)
                .changedBy(this.changedBy)
                .changeReason(this.changeReason)
                .changedAt(this.changedAt)
                .build();
    }
}
