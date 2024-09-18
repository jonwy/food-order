package com.padepokan79.foodorder.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "foods", schema = "public")
@EntityListeners(AuditingEntityListener.class)
public class Food implements Serializable{
    @Id
    // menggunakan GenerationType.IDENTITY karena pada db, default value-nya adalah INDENTITY.
    // Menggunakan GenerationType.SEQUENCE ketika, pada db default value-nya menggunakan nextval({seq})
    @GeneratedValue(strategy = GenerationType.IDENTITY/* , generator = "generator_foods_food_id_seq" */)
    // @SequenceGenerator(name = "generator_foods_food_id_seq", initialValue = 1000000, allocationSize = 1)
    @Column(name = "food_id",  unique = true, nullable = false)
    private Integer foodId;

    @Column(name = "food_name")
    private String foodName;
    
    @Column(name = "image_filename")
    private String imageFilename;
    
    @Column(name ="price")
    private Integer price;
    
    @Column(name = "ingredient")
    private String ingredient;
    
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
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "food", fetch = FetchType.LAZY)
    private List<Cart> carts;

    @OneToMany(mappedBy = "food", fetch = FetchType.LAZY)
    private List<FavoriteFood> favoriteFoods;
}
