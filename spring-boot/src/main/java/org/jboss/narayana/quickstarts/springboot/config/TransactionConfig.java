package org.jboss.narayana.quickstarts.springboot.config;

import com.arjuna.ats.internal.jta.transaction.arjunacore.TransactionManagerImple;
import com.arjuna.ats.jta.UserTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.sql.DataSource;
import javax.transaction.TransactionManager;

/**
 * Created by jhaskovec on 4/4/16.
 */
@Configuration
@EnableTransactionManagement
public class TransactionConfig implements TransactionManagementConfigurer {
    private static final Logger log = LoggerFactory.getLogger(TransactionConfig.class);

    @Autowired
    private DataSource datasource;

    @Bean
    public PlatformTransactionManager jtaTransactionManager() {
        JtaTransactionManager tm = new JtaTransactionManager();
        tm.setUserTransaction(UserTransaction.userTransaction());
        tm.setTransactionManager(transactionManager());
        tm.setTransactionManagerName("transactionManager");

        return tm;
    }

    @Bean
    public TransactionManager transactionManager() {
        return new TransactionManagerImple();
    }

    @Bean
    JdbcTemplate jdbcTemplate() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(datasource);
        log.info("Creating tables");
        jdbcTemplate.execute("drop table example if exists");
        jdbcTemplate.execute("CREATE TABLE example ( id INT(10), name varchar(32), PRIMARY KEY(id))");
        return jdbcTemplate;
    }

    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return jtaTransactionManager();
    }
}
