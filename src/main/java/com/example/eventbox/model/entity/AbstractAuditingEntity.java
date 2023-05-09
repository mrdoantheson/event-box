package com.example.eventbox.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@MappedSuperclass
public abstract class AbstractAuditingEntity {
    @Column
    private Boolean deleted;
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "update_by")
    private String updatedBy;
    @Column(name = "created_date")
    private LocalDateTime createdDate;
    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;
}
