package io.github.agorshkov23.dockerlauncher.helper;

import io.github.agorshkov23.dockerlauncher.docker.compose.v3.Compose3;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@Slf4j
public class ComposeFileHelper {

    private final Yaml yaml = new Yaml();

    public Compose3 load(String path) {
        return load(Paths.get(path));
    }

    public Compose3 load(Path path) {
        try {
            Compose3 compose3 = yaml.loadAs(Files.newBufferedReader(path), Compose3.class);
            return compose3;
        } catch (Exception e) {
            log.warn("failed load docker-compose file {}, reason={}", path, e.getMessage(), e);
            throw new RuntimeException(e);

        }
    }
}
