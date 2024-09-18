package com.padepokan79.foodorder.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.padepokan79.foodorder.model.Order;
import com.padepokan79.foodorder.service.projection.OrderHistory;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>{

    @Query("SELECT o FROM Order o WHERE o.user.userId= :userId")
    List<Order> findAllByUserId(@Param("userId") Integer userId);

    Page<Order> findAll(Pageable pageable);

    @Query("""
            SELECT o.orderId as orderId, o.orderDate as orderDate, 
                o.user.userId as userId,
                o.totalItem as totalItem, o.totalOrderPrice as totalOrderPrice, 
                o.createdTime as createdTime, o.modifiedTime as modifiedTime, 
                o.createdBy as createdBy, o.modifiedBy as modifiedBy,
                f.foodName as foodName, f.imageFilename as imageFilename,
                od as orderDetail
                FROM Order o
                LEFT JOIN OrderDetail od on o.orderId = od.order.orderId
                LEFT JOIN Food f on f.foodId = od.food.foodId
                WHERE o.user.userId = :userId
            """)
    Page<OrderHistory> findAllByUserId(Pageable pageable, @Param("userId") Integer userId);

    @Query("""
            SELECT o.orderId as orderId, o.orderDate as orderDate, 
                o.user.userId as userId,
                o.totalItem as totalItem, o.totalOrderPrice as totalOrderPrice, 
                o.createdTime as createdTime, o.modifiedTime as modifiedTime, 
                o.createdBy as createdBy, o.modifiedBy as modifiedBy,
                f.foodName as foodName, f.imageFilename as imageFilename,
                od as orderDetail
                FROM Order o
                LEFT JOIN OrderDetail od on o.orderId = od.order.orderId
                LEFT JOIN Food f on f.foodId = od.food.foodId
                WHERE o.user.userId = :userId
            """)
    Page<OrderHistory> findAllByUserId2(Pageable pageable, @Param("userId") Integer userId);
}
