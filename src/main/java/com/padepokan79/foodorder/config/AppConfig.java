package com.padepokan79.foodorder.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.padepokan79.foodorder.utils.MessageUtils;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
    
    @Bean
    public MessageUtils messageUtil() {
        return new MessageUtils();
    }
}
