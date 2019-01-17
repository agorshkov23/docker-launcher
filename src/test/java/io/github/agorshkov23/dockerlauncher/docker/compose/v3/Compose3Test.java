package io.github.agorshkov23.dockerlauncher.docker.compose.v3;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;

public class Compose3Test {

    @Test
    public void parseTest() {
        String path = "docker/compose/docker-compose-3.yml";
        InputStream stream = getClass().getResourceAsStream(path);
        if (stream == null) {
            stream = getClass().getClassLoader().getResourceAsStream(path);
        }
        Assertions.assertNotNull(stream);
        Yaml yaml = new Yaml(new Constructor(Compose3.class));
        Compose3 dockerComposeYaml = yaml.load(stream);
        Assertions.assertNotNull(dockerComposeYaml);
    }
}