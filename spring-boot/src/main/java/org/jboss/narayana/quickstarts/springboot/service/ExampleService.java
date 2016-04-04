package org.jboss.narayana.quickstarts.springboot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Jeffrey Haskovec on 4/2/2016.
 *
 * This example is intended to be tested with a non-XA Connection transaction
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class ExampleService {
    private static final Logger log = LoggerFactory.getLogger(ExampleService.class);

    @Autowired
    private JdbcTemplate jdbc;

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

    public void testRollback() throws Exception{
        log.info("test rollback");

        jdbc.execute("insert into exmaple values (2, 'test2')");

        throw new Exception("Throw exception in method to see if we rolled back the insert");
    }

}
