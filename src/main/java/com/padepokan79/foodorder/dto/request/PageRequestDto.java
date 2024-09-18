package com.padepokan79.foodorder.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PageRequestDto {
    
    private Integer pageSize = 8;
    private Integer pageNumber = 1;
    private String sortBy = "foodName";
    private String sortDirection = "asc";
}
