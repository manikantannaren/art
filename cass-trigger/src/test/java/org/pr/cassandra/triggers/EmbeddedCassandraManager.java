/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pr.cassandra.triggers;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.cassandra.config.DatabaseDescriptor;
import org.apache.cassandra.io.util.FileUtils;
import org.apache.cassandra.service.CassandraDaemon;
import org.apache.cassandra.thrift.Cassandra;
import org.slf4j.LoggerFactory;

/**
 *
 * @author msivasub
 */
public class EmbeddedCassandraManager {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(EmbeddedCassandraManager.class);
    ExecutorService executor;
    EmbeddedCassandraServiceHolder cassandraService;

    private EmbeddedCassandraManager() {
        executor = Executors.newSingleThreadExecutor();
    }

    public static EmbeddedCassandraManager getInstance() {
        return EmbeddedCassandraManagerHolder.INSTANCE;
    }

    private static class EmbeddedCassandraManagerHolder {

        private static final EmbeddedCassandraManager INSTANCE = new EmbeddedCassandraManager();
    }

    public void startCassandra() throws URISyntaxException, IOException {
        if (cassandraService != null) {
            return;
        }
        System.setProperty("cassandra.config", getClass().getResource("/cassandra/conf/cassandra.yml").toURI().toString());
        CassandraServiceDataCleaner dataCleaner = new CassandraServiceDataCleaner();
        dataCleaner.prepare();
        cassandraService = new EmbeddedCassandraServiceHolder();
        executor.submit(cassandraService);

    }

    public void stop() {
        if (cassandraService != null) {
            cassandraService.stop();
        }
        cassandraService = null;
    }

    public Cassandra.Client getClient() {
        throw new UnsupportedOperationException();
    }

    private static class CassandraServiceDataCleaner {

        /**
         * Creates all data dir if they don't exist and cleans them
         *
         * @throws IOException
         */
        public void prepare() throws IOException {
            DatabaseDescriptor.daemonInitialization();
//            DatabaseDescriptor.loadConfig();
            makeDirsIfNotExist();
            cleanupDataDirectories();
        }

        /**
         * Deletes all data from cassandra data directories, including the
         * commit log.
         *
         * @throws IOException in case of permissions error etc.
         */
        public void cleanupDataDirectories() throws IOException {
            for (String s : getDataDirs()) {
                cleanDir(s);
            }
        }

        /**
         * Creates the data diurectories, if they didn't exist.
         *
         * @throws IOException if directories cannot be created (permissions
         * etc).
         */
        public void makeDirsIfNotExist() throws IOException {
            for (String s : getDataDirs()) {
                mkdir(s);
            }
        }

        /**
         * Collects all data dirs and returns a set of String paths on the file
         * system.
         *
         * @return
         */
        private Set<String> getDataDirs() {
            Set<String> dirs = new HashSet<>();
            for (String s : DatabaseDescriptor.getAllDataFileLocations()) {
                dirs.add(s);
            }
//            dirs.add(DatabaseDescriptor.getLogFileLocation());
            return dirs;
        }

        /**
         * Creates a directory
         *
         * @param dir
         * @throws IOException
         */
        private void mkdir(String dir) throws IOException {
            FileUtils.createDirectory(dir);
        }

        /**
         * Removes all directory content from file the system
         *
         * @param dir
         * @throws IOException
         */
        private void cleanDir(String dir) throws IOException {
            File dirFile = new File(dir);
            if (dirFile.exists()
                    && dirFile.isDirectory()) {
                FileUtils.delete(dirFile.listFiles());
            }
        }
    }

    private static class EmbeddedCassandraServiceHolder implements Runnable {

        CassandraDaemon daemon;

        public EmbeddedCassandraServiceHolder() throws IOException {
            daemon = new CassandraDaemon();
            daemon.init(null);
        }

        public void stop() {
            daemon.stop();
        }

        @Override
        public void run() {
            daemon.start();
        }

    }
}
