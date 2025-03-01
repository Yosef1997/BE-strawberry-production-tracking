package com.strawberry.production.weather.entity;

import com.strawberry.production.reject.entity.Reject;
import com.strawberry.production.yield.entity.Yield;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "weather_data")
@NoArgsConstructor
@Getter
@Setter
public class Weather {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @NotNull
    @Column(name = "week_number", nullable = false)
    private Integer weekNumber;

    @NotNull
    @Column(nullable = false)
    private Double humidity;

    @NotNull
    @Column(name = "rain_fall", nullable = false)
    private Double rainFall;

    @NotNull
    @Column(nullable = false)
    private Double temperature;

    @OneToMany(mappedBy = "weekNumber", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Yield> yield;

    @OneToMany(mappedBy = "weekNumber", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reject> rejectRecords;

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
