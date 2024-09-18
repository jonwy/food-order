package com.padepokan79.foodorder.model;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categories", schema = "public")
@EntityListeners(AuditingEntityListener.class)
public class Category implements Serializable{

    @Id
    @Column(name = "category_id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator_categories_category_id_seq")
    @SequenceGenerator(name = "generator_categories_category_id_seq", sequenceName = "categories_category_id_seq", allocationSize = 1, schema = "public")
    private Integer categoryId;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @CreatedDate
    @Column(name = "created_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;
    
    @LastModifiedDate
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedTime;
    private String createdBy;
    private String modifiedBy;
}
