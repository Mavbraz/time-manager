package com.github.mavbraz.timemanager.configuration;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

@Configuration
public class AppConfig {

  //  @Bean
  //  public MongoTemplate mongoTemplate(
  //      MongoDatabaseFactory databaseFactory, MappingMongoConverter converter) {
  //    converter.setTypeMapper(new DefaultMongoTypeMapper(null));
  //
  //    return new MongoTemplate(databaseFactory, converter);
  //  }

  @Autowired private MappingMongoConverter mappingMongoConverter;

  @PostConstruct
  public void setUp() {
    mappingMongoConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
  }
}
