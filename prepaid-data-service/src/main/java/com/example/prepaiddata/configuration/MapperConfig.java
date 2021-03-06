package com.example.prepaiddata.configuration;

import com.example.prepaiddata.utility.EntityDTOMapper;
import com.example.prepaiddata.utility.ModelEntityMapper;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public EntityDTOMapper entityDTOMapper() {
        return new EntityDTOMapper(modelMapper());
    }

    @Bean
    public ModelEntityMapper modelEntityMapper() {
        return new ModelEntityMapper(modelMapper());
    }
}
