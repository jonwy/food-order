package com.padepokan79.foodorder.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private Integer userId;
    private String username;
    private String fullname;
    private boolean isDeleted;
    private Date createdTime;
    private Date modifiedTime;
    private String createdBy;
    private String modifiedBy;
}
