package org.example.languagemaster.dto.mappers;
import org.modelmapper.ModelMapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    public MapperFactory mapperFactory(ModelMapper modelMapper) {
        return new MapperFactory(modelMapper);
    }
}
