package com.geekhome.httpserver.modules;

import com.geekhome.common.ConfigurationMetadata;

import java.util.ArrayList;

interface IConfigurationProvider {
    void save(ArrayList<Collector> collectors, String comment) throws Exception;
    ConfigurationMetadata load(ArrayList<Collector> targets, String backupId) throws Exception;
    CollectorCollection<BackupInfo> getBackups() throws Exception;
}