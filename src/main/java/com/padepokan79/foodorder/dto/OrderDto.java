package com.padepokan79.foodorder.dto;

import java.time.LocalDate;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private Integer orderId;
    private UserDto user;
    private LocalDate orderDate;
    private Integer totalItem;
    private Integer totalOrderPrice;
    private Date createdTime;
    private Date modifiedTime;
    private String createdBy;
    private String modifiedBy;
}
