package com.padepokan79.foodorder.dto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderHistoryDto {
    private Integer orderId;
    private Integer totalOrderPrice;
    private Integer totalItem;
    @JsonFormat(pattern = "dd MMMM yyyy")
    private LocalDate orderDate;
    private List<OrderDetailDto> orderDetails;
}
