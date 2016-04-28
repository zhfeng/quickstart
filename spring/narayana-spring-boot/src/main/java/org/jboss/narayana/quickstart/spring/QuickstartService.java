/*
 * JBoss, Home of Professional Open Source
 * Copyright 2016, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.narayana.quickstart.spring;

import com.arjuna.ats.jbossatx.jta.RecoveryManagerService;
import org.jboss.narayana.quickstart.spring.helpers.DummyXAResource;
import org.jboss.narayana.quickstart.spring.helpers.DummyXAResourceRecovery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.TransactionManager;
import javax.transaction.xa.XAResource;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
@Service
public class QuickstartService {

    @Autowired
    private TransactionManager transactionManager;

    @Autowired
    private EntriesService entriesService;

    @Autowired
    private MessagesService messagesService;

    @Autowired
    private RecoveryManagerService recoveryManagerService;

    public void demonstrateCommit(String entry) throws Exception {
        executeDemonstration(entry, transactionManager::commit, null);
    }

    public void demonstrateRollback(String entry) throws Exception {
        executeDemonstration(entry, transactionManager::rollback, null);
    }

    public void demonstrateCrash(String entry) throws Exception {
        executeDemonstration(entry, transactionManager::commit, new DummyXAResource(true));
    }

    public void demonstrateRecovery() throws Exception {
        System.out.println("Entries at the start: " + entriesService.getAll());
        recoveryManagerService.addXAResourceRecovery(new DummyXAResourceRecovery());
        Thread.sleep(5000);
        System.out.println("Entries at the end: " + entriesService.getAll());
    }

    private void executeDemonstration(String entry, TransactionTerminator terminator, XAResource xaResource) throws Exception {
        System.out.println("Entries at the start: " + entriesService.getAll());

        transactionManager.begin();
        if (xaResource != null) {
            transactionManager.getTransaction().enlistResource(xaResource);
        }
        entriesService.create(entry);
        messagesService.send("Created entry '" + entry + "'");
        terminator.terminate();

        System.out.println("Entries at the end: " + entriesService.getAll());
    }

    private interface TransactionTerminator {

        void terminate() throws Exception;

    }

}
