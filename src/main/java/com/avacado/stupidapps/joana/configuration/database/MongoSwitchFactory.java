package com.avacado.stupidapps.joana.configuration.database;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.avacado.stupidapps.joana.utils.RequestUtils;
import com.mongodb.client.MongoDatabase;

@EnableTransactionManagement
@Configuration
public class MongoSwitchFactory extends SimpleMongoClientDatabaseFactory
{

  @Autowired
  public MongoSwitchFactory(@Value("${mongo.server.name}") String mongoDbServer,
      @Value("${mongo.server.port}") String mongoDbPort,
      @Value("${mongo.server.default.database}") String mongoDbDefaultDatabase)
  {
    super(String.format("mongodb://%s:%s/%s", mongoDbServer, mongoDbPort, mongoDbDefaultDatabase));
  }

  @Override
  protected MongoDatabase doGetMongoDatabase(String dbName)
  {
    String currentRequestDatabase = StringUtils.isNotEmpty(RequestUtils.getDatabaseName())
        ? RequestUtils.getDatabaseName()
        : dbName;
    return super.doGetMongoDatabase(currentRequestDatabase);
  }
  
  @Bean
  public MongoTransactionManager transactionManager(SimpleMongoClientDatabaseFactory dbFactory) {
      return new MongoTransactionManager(dbFactory);
  }

}
