package com.padepokan79.foodorder.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

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
@EntityListeners(AuditingEntityListener.class)
@Builder
@Table(name = "orders")
public class Order implements Serializable{
    @Id
    @Column(name = "order_id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator_orders_order_id_seq")
    @SequenceGenerator(name = "generator_orders_order_id_seq", sequenceName = "orders_order_id_seq", allocationSize = 1, schema = "public")
    private Integer orderId;

    // @Temporal(TemporalType.DATE)
    // @Column(name = "order_date")
    // private Date orderDate;
    @DateTimeFormat(pattern = "dd-MMMM-yyyy")
    @Column(name="order_date")
    private LocalDate orderDate;
    
    @Column(name = "total_item")
    private Integer totalItem;
    
    @Column(name =" total_order_price")
    private Integer totalOrderPrice;

    @CreatedDate
    @Column(name = "created_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;
    
    @LastModifiedDate
    @Column (name = "modified_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedTime;
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "modified_by")
    private String modifiedBy;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
