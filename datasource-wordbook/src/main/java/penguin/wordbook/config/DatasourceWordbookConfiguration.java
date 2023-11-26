package penguin.wordbook.config;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

import jakarta.persistence.EntityManagerFactory;


@Configuration
@PropertySource({ "classpath:application-datasource-wordbook.yml" })
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = { "penguin.wordbook.dao.repository" }
        , entityManagerFactoryRef = DatasourceWordbookConfiguration.ENTITY_MANAGER_FACTORY
        , transactionManagerRef = DatasourceWordbookConfiguration.TRANSACTION_MANAGER
)
public class DatasourceWordbookConfiguration {
    public static final String DATASOURCE_PROPERTIES = "datasource-wordbook.datasource";
    public static final String HIKARI_DATASOURCE = "datasource-wordbook.datasource.hikari";
    public static final String JPA_PROPERTIES = "datasource-wordbook.jpa";
    public static final String ENTITY_MANAGER_FACTORY = "datasource-wordbook.entity-manager-factory";
    public static final String TRANSACTION_MANAGER = "datasource-wordbook.transaction-manager";
    public static final String PERSISTENCE_UNIT = "datasource-wordbook.persistence-unit";
    @Bean(name = DATASOURCE_PROPERTIES)
//    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSourceProperties dataSourceProperties(){
        return new DataSourceProperties();
    }

    @Primary
    @Bean(name = HIKARI_DATASOURCE)
//    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public HikariDataSource dataSource(
            @Qualifier(DATASOURCE_PROPERTIES) DataSourceProperties dataSourceProperties
    ) {
        return DataSourceBuilder
                .derivedFrom(dataSourceProperties.initializeDataSourceBuilder().build())
                .type(HikariDataSource.class)
                .build();
    }

    @Primary
    @Bean(JPA_PROPERTIES)
    public JpaProperties jpaProperties(){
        return new JpaProperties();
    }

    @Bean(ENTITY_MANAGER_FACTORY)
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            @Qualifier(HIKARI_DATASOURCE) HikariDataSource dataSource
            , @Qualifier(JPA_PROPERTIES) JpaProperties jpaProperties
    ) {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPackagesToScan("penguin.wordbook.model.entity");
        emf.setPersistenceUnitName(PERSISTENCE_UNIT);
        emf.setJpaPropertyMap(jpaProperties.getProperties());

        // 여기에 JPA 벤더 어댑터 설정을 추가하세요 (예: HibernateJpaVendorAdapter)
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabasePlatform(jpaProperties.getDatabasePlatform());
        emf.setJpaVendorAdapter(vendorAdapter);
        return emf;
    }

    @DependsOn(ENTITY_MANAGER_FACTORY)
    @Bean(name = TRANSACTION_MANAGER)
    PlatformTransactionManager transactionManager(
            @Qualifier(ENTITY_MANAGER_FACTORY) EntityManagerFactory entityManagerFactory
    ) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}