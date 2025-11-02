package com.csis231.api.plannerUser;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "planner_users")
public class PlannerUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "planner_id", nullable = false)
    private Long plannerId; // references planners.id

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(length = 20)
    private String role;

    @Column(name = "added_at")
    private Instant addedAt = Instant.now();

    public PlannerUser() {}

    public PlannerUser(Long plannerId, Long userId, String role) {
        this.plannerId = plannerId;
        this.userId = userId;
        this.role = role;
        this.addedAt = Instant.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPlannerId() { return plannerId; }
    public void setPlannerId(Long plannerId) { this.plannerId = plannerId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Instant getAddedAt() { return addedAt; }
    public void setAddedAt(Instant addedAt) { this.addedAt = addedAt; }
}
