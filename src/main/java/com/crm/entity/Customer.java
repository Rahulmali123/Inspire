package com.crm.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.crm.enums.Membership;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String contact;

    @Enumerated(EnumType.STRING)
    private Membership membership;

    private String preferences;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Visit> visits;

    @ManyToMany(fetch = FetchType.LAZY) // lazy by default
    @JoinTable(
        name = "customer_services",
        joinColumns = @JoinColumn(name = "customer_id"),
        inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    private Set<ServiceEntity> assignedServices;

    @Column(updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private boolean isActive = true; // soft delete

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
     //   this.updatedAt = LocalDateTime.now();
        this.isActive = true;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}