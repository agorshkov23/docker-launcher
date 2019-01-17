package io.github.agorshkov23.dockerlauncher.docker.compose.v3;

import lombok.Data;

import java.util.Map;

@Data
public class Compose3 {
    private String version;
    private Map<String, ServiceSpec> services;
}
