package com.padepokan79.foodorder.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class MessageReponseWithData extends MessageResponse{
    
    private Integer total;
    private Object data;

}
