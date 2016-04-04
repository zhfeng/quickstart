package org.jboss.narayana.quickstarts.springboot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@SpringBootApplication
public class SpringbootJtaApplication {
	private static final Logger log = LoggerFactory.getLogger(SpringbootJtaApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringbootJtaApplication.class, args);
	}

	@Bean
	JdbcTemplate jdbcTemplate(DataSource dataSource) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		log.info("Creating tables");
		jdbcTemplate.execute("drop table example if exists");
		jdbcTemplate.execute("CREATE TABLE example ( id varchar(10), name varchar(32) )");
		return jdbcTemplate;
	}


}
