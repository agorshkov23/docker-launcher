package io.github.agorshkov23.dockerlauncher.service;

import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.DockerClient.ListContainersParam;
import com.spotify.docker.client.messages.Container;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;
import io.github.agorshkov23.dockerlauncher.config.property.DockerLauncherProperties;
import io.github.agorshkov23.dockerlauncher.docker.compose.v3.ServiceSpec;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class ContainerService extends AbstractService {

    private static final String DOCKER_INSTANCE_LABEL_NAME = "io.github.agorshkov23.dockerlauncher.instance";

    private final InstanceService instanceService;

    private final DockerClient dockerClient;

    private final DockerLauncherProperties properties;

    public Container getContainerByName(String containerName) {
        try {
            for (Container container : getContainers()) {
                if (container.names().contains("/" + containerName)) {
                    return container;
                }
            }
            return null;
        } catch (Exception e) {
            log.error("failed start container: {}, error={}", containerName, e.getMessage(), e);
            throw new RuntimeException("Failed start container " + containerName + ": " + e.getMessage(), e);
        }
    }

    public List<Container> getContainers() {
        try {
            ListContainersParam param = ListContainersParam.withLabel(DOCKER_INSTANCE_LABEL_NAME, instanceService.getStringInstance());
            return dockerClient.listContainers(
                    param,
                    ListContainersParam.withStatusCreated(),
                    ListContainersParam.withStatusExited(),
                    ListContainersParam.withStatusPaused(),
                    ListContainersParam.withStatusRestarting(),
                    ListContainersParam.withStatusRunning()
            );
        } catch (Exception e) {
            log.error("failed get containers: error={}", e.getMessage(), e);
            throw new RuntimeException("Failed get containers: " + e.getMessage(), e);
        }
    }

    public Container createContainer(String name, ServiceSpec spec) {
        try {
            dockerClient.pull(spec.getImage());

            Map<String, String> labels = Collections.singletonMap(DOCKER_INSTANCE_LABEL_NAME, instanceService.getStringInstance());

            //  TODO: add prefix latest if tag missing
            ContainerConfig containerConfig = ContainerConfig.builder()
                    .image(spec.getImage())
                    .labels(labels)
                    .build();

            dockerClient.createContainer(containerConfig, getContainerName(name));
            return getContainerByName(name);
        } catch (Exception e) {
            log.error("failed create container: {}, error={}", name, e.getMessage(), e);
            throw new RuntimeException("Failed create container " + name, e);
        }

    }

    public void start(String name) {
        try {
            Container container = getContainerByName(name);
            if (container == null) {
                throw new RuntimeException("Failed start container: container " + name + " not exists");
            }
            dockerClient.startContainer(container.id());
        } catch (Exception e) {
            log.warn("failed start container: container {} not exists", name);
        }
    }

    public void up(String name, ServiceSpec spec) {
        try {
            dockerClient.pull(spec.getImage());

            Map<String, String> labels = Collections.singletonMap(DOCKER_INSTANCE_LABEL_NAME, instanceService.getStringInstance());

            ContainerConfig containerConfig = ContainerConfig.builder()
                    .image(spec.getImage())
                    .labels(labels)
                    .build();

            ContainerCreation container = dockerClient.createContainer(containerConfig, getContainerName(name));

            dockerClient.startContainer(container.id());
        } catch (Exception e) {
            log.error("failed up container: {}, error={}", name, e.getMessage(), e);
            throw new RuntimeException("Failed up container " + name, e);
        }
    }

    private String getContainerName(String name) {
        return properties.getName() + "_" + name;
    }
}
