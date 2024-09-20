package org.threefour.ddip.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.threefour.ddip.datasource.CustomRoutingDataSource;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.threefour.ddip.datasource.DatabaseType.MASTER;
import static org.threefour.ddip.datasource.DatabaseType.SLAVE;

@Configuration
public class DataSourceConfig {
  private static final String MASTER_DATASOURCE = "masterDataSource";
  private static final String SLAVE_DATASOURCE = "slaveDataSource";

  @Value("${spring.datasource.master.driver-class-name}")
  private String masterDriverClassName;

  @Value("${spring.datasource.master.jdbc-url}")
  private String masterJdbcUrl;

  @Value("${spring.datasource.master.username}")
  private String masterUsername;

  @Value("${spring.datasource.master.password}")
  private String masterPassword;

  @Value("${spring.datasource.slave.driver-class-name}")
  private String slaveDriverClassName;

  @Value("${spring.datasource.slave.jdbc-url}")
  private String slaveJdbcUrl;

  @Value("${spring.datasource.slave.username}")
  private String slaveUsername;

  @Value("${spring.datasource.slave.password}")
  private String slavePassword;

  @Bean(MASTER_DATASOURCE)
  public DataSource masterDataSource() {
    return DataSourceBuilder.create()
            .driverClassName(masterDriverClassName)
            .url(masterJdbcUrl)
            .username(masterUsername)
            .password(masterPassword)
            .build();
  }

  @Bean(SLAVE_DATASOURCE)
  public DataSource slaveDataSource() {
    return DataSourceBuilder.create()
            .driverClassName(slaveDriverClassName)
            .url(slaveJdbcUrl)
            .username(slaveUsername)
            .password(slavePassword)
            .build();
  }

  @Bean
  public DataSource routingDataSource(@Qualifier(MASTER_DATASOURCE) DataSource masterDataSource,
                                      @Qualifier(SLAVE_DATASOURCE) DataSource slaveDataSource) {

    CustomRoutingDataSource customRoutingDataSource = new CustomRoutingDataSource();

    Map<Object, Object> dataSourceMap = new HashMap<>();
    dataSourceMap.put(MASTER, masterDataSource);
    dataSourceMap.put(SLAVE, slaveDataSource);

    Map<Object, Object> immutableDataSourceMap = Collections.unmodifiableMap(dataSourceMap);

    customRoutingDataSource.setTargetDataSources(immutableDataSourceMap);
    customRoutingDataSource.setDefaultTargetDataSource(masterDataSource);

    return customRoutingDataSource;
  }

  @Primary
  @Bean
  public DataSource dataSource(@Qualifier("routingDataSource") DataSource routingDataSource) {
    return new LazyConnectionDataSourceProxy(routingDataSource);
  }
}