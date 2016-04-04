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
public interface ExampleService {
    public void testCommit() throws Exception;

    public void testRollback();
}
