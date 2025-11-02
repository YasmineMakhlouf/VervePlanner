package com.csis231.api.tag;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "tags")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "planner_id", nullable = false)
    private Long plannerId;

    @NotBlank
    @Column(length = 100, nullable = false)
    private String name;

    public Tag() {}

    public Tag(Long plannerId, String name) {
        this.plannerId = plannerId;
        this.name = name;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPlannerId() { return plannerId; }
    public void setPlannerId(Long plannerId) { this.plannerId = plannerId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
