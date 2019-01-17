package io.github.agorshkov23.dockerlauncher.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class InstanceService extends AbstractService {

    private static final String INSTANCE_FILE_NAME = "instance.txt";

    @Getter
    private UUID instance;

    @PostConstruct
    public void onPostConstruct() throws Exception {
        log.debug("init");

        UUID instance = readInstanceFromFile();
        if (instance == null) {
            instance = generateInstance();
            log.info("instance not found, generated new instance={}", instance);
            writeInstanceToFile(instance);
        } else {
            this.instance = instance;
            log.info("started with instance={}", this.instance);
        }
    }

    public String getStringInstance() {
        return instance.toString();
    }

    private UUID generateInstance() {
        return UUID.randomUUID();
    }

    private UUID readInstanceFromFile() {
        try {
            File instanceFile = new File(INSTANCE_FILE_NAME);
            if (instanceFile.exists() && instanceFile.isFile() && instanceFile.canRead()) {
                List<String> strings = Files.readAllLines(Paths.get(instanceFile.getName()));
                String stringInstance = strings.get(0);
                return UUID.fromString(stringInstance);
            }
        } catch (Exception e) {
            log.error("failed read instance from file={}, error={}", INSTANCE_FILE_NAME, e.getMessage(), e);
        }
        return null;
    }

    private static void writeInstanceToFile(UUID instance) throws IOException {
        try {
            Path path = Paths.get(INSTANCE_FILE_NAME);
            Files.write(path, instance.toString().getBytes());
            log.debug("write instance to file={}, instance={}", INSTANCE_FILE_NAME, instance);
        } catch (Exception e) {
            log.error("failed write instance to file={}, error={}", INSTANCE_FILE_NAME, e.getMessage(), e);
            throw e;
        }
    }
}
