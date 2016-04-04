package org.jboss.narayana.quickstarts.springboot.config;

import com.arjuna.ats.jta.TransactionManager;
import com.arjuna.ats.jta.UserTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.EnableLoadTimeWeaving;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.sql.DataSource;

/**
 * Created by jhaskovec on 4/4/16.
 */
@Configuration
@EnableTransactionManagement
//@EnableLoadTimeWeaving(aspectjWeaving=EnableLoadTimeWeaving.AspectJWeaving.ENABLED)
public class TransactionConfig implements TransactionManagementConfigurer {
	private static final Logger log = LoggerFactory.getLogger(TransactionConfig.class);

	@Autowired
	private DataSource datasource;

	@Override
	public PlatformTransactionManager annotationDrivenTransactionManager() {
		JtaTransactionManager transactionManager = new JtaTransactionManager(UserTransaction.userTransaction(), TransactionManager.transactionManager());
		transactionManager.setTransactionManagerName("transactionManager");

		return transactionManager;
	}

	@Bean
	JdbcTemplate jdbcTemplate(DataSource dataSource) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		log.info("Creating tables");
		jdbcTemplate.execute("drop table example if exists");
		jdbcTemplate.execute("CREATE TABLE example ( id INT(10), name varchar(32), PRIMARY KEY(id))");
		return jdbcTemplate;
	}

}
