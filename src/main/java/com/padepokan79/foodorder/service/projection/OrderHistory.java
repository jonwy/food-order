package com.padepokan79.foodorder.service.projection;

import java.time.LocalDate;

import com.padepokan79.foodorder.model.OrderDetail;
public interface OrderHistory {

    Integer getOrderId();
    Integer getTotalItem();
    Integer getUserId();
    Integer getTotalOrderPrice();
    Integer getTotalPrice();
    // Date getOrderDate();
    LocalDate getOrderDate();
    Integer getFoodId();
    String getFoodName();
    String getImageFilename();
    OrderDetail getOrderDetail();
}
