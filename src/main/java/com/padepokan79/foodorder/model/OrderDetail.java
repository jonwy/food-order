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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OrderDetail", schema = "public")
@Builder
@EntityListeners(AuditingEntityListener.class)
public class OrderDetail implements Serializable{
    @Id
    @Column(name = "order_detail_id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator_order_detail_id_seq")
    @SequenceGenerator(name = "generator_order_detail_id_seq", sequenceName = "order_detail_order_detail_id_seq", allocationSize = 1, schema = "public")
    private Integer orderDetailId;

    @Column(name = "qty")
    private Integer quantity;

    @Column(name = "total_price")
    private Integer totalPrice;

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

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "food_id")
    private Food food;
    
}
