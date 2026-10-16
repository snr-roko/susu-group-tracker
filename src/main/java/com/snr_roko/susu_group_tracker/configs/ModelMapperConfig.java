package com.snr_roko.susu_group_tracker.configs;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration 
public class ModelMapperConfig {

    @Bean 
    public ModelMapper getModelMapper() {
        return new ModelMapper(); 
    }
}
