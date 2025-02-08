package com.strawberry.production.report.entity;

import com.strawberry.production.users.entity.Users;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Entity
@Table(name = "report")
@NoArgsConstructor
@Getter
@Setter
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users pic;

    @NotNull
    @Column(name = "gross_strawberry_weight", nullable = false)
    private Double grossStrawberryWeight;

    @NotNull
    @Column(name = "pack_a_quantity", nullable = false)
    private Integer packAQuantity;

    @NotNull
    @Column(name = "pack_b_quantity", nullable = false)
    private Integer packBQuantity;

    @NotNull
    @Column(name = "pack_c_quantity", nullable = false)
    private Integer packCQuantity;

    @NotNull
    @Column(name = "reject_weight", nullable = false)
    private Double rejectWeight;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }

    @PreRemove
    public void preRemove() {
        this.deletedAt = Instant.now();
    }
}
