package org.jboss.narayana.quickstarts.springboot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by jhaskovec on 4/4/16.
 */
@Service
@Transactional
public class ExampleServiceImpl implements ExampleService {
	private static final Logger log = LoggerFactory.getLogger(ExampleService.class);

	@Autowired
	private JdbcTemplate jdbc;


	@Override
	public void testCommit() throws Exception {
		try {
			log.info("testCommit");
			jdbc.execute("insert into example values (1, 'test1')");
			log.info("testCommit OK");
		} catch (Exception e) {
			log.error("testCommit FAIL with " + e);
			throw e;
		}
	}


	@Override
	public void testRollback() {
		log.info("test rollback");

		jdbc.execute("insert into example values (2, 'test2')");

		throw new RuntimeException("Throw exception in method to see if we rolled back the insert");
	}

}
