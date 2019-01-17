package io.github.agorshkov23.dockerlauncher.service;

import org.slf4j.LoggerFactory;

public abstract class AbstractService {

    protected AbstractService() {
        LoggerFactory.getLogger(getClass()).debug("init");
    }
}
