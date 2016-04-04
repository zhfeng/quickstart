package org.jboss.narayana.quickstarts.springboot;

import org.jboss.narayana.quickstarts.springboot.service.ExampleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringbootJtaApplication.class)
public class SpringbootJtaApplicationTests {
	@Autowired
	private ExampleService exampleService;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Test
	public void contextLoads() {
	}

	@Test
	public void testCommit() throws Exception {
		exampleService.testCommit();

		final Integer count = jdbcTemplate.queryForObject("select count(*) from example where id = 1", Integer.class);
		assertNotNull(count);
		assertEquals(1, count.intValue());
	}

	@Test
	public void testRollback() {
		try {
			exampleService.testRollback();
		} catch (RuntimeException e) {
			//This is expected
		}

		//Check to see if we rolled back the insert
		final Integer count = jdbcTemplate.queryForObject("select count(*) from example where id = 2", Integer.class);
		assertNotNull(count);
		assertEquals(0, count.intValue());
	}
}
