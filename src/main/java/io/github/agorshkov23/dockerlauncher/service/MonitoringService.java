package io.github.agorshkov23.dockerlauncher.service;

import com.spotify.docker.client.messages.Container;
import io.github.agorshkov23.dockerlauncher.config.property.DockerLauncherProperties;
import io.github.agorshkov23.dockerlauncher.docker.compose.v3.ServiceSpec;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Map;

@Service
@ConditionalOnProperty(prefix = "docker.launcher.services.monitoring", name = "active", havingValue = "true")
@Slf4j
@AllArgsConstructor
public class MonitoringService extends AbstractService {
    private final ComposeService composeService;
    private final ContainerService containerService;
    private final DockerLauncherProperties properties;

    @Scheduled(fixedRateString = "${docker.launcher.services.monitoring.check-rate}")
    public void onScheduledEvent() {
        check();
    }

    public void check() {
        Map<String, ServiceSpec> composeServices = composeService.getCompose().getServices();
        for (String serviceName : composeServices.keySet()) {
            ServiceSpec spec = composeServices.get(serviceName);
            String containerName = spec.getContainerName() == null
                    ? properties.getName() + "_" + serviceName
                    : spec.getContainerName();

            //  Поиск среди запущенных контейнеров
            Container container = containerService.getContainerByName(containerName);
            if (container == null) {
                //  Контейнер не найден, создаем контейнер
                container = containerService.createContainer(containerName, spec);
                Assert.notNull(container, "container");
            }

            //  Проверка состояния контейнера
            if ("running".equals(container.state())) {
                log.debug("container {} is {}", containerName, container.state());
                continue;
            }

            log.warn("starting container {}: container is {}. starting...", containerName, container.state());
            containerService.start(containerName);
        }

//        log.trace("check");
//        for (Container container : containerService.getContainers()) {
//            String containerName = container.names().get(0).substring(1);
//            if ("running".equals(container.state())) {
//                log.debug("container {} is {}", containerName, container.state());
//                continue;
//            }
//
//            log.warn("starting container {}: container is {}. starting...", containerName, container.state());
//            containerService.start(containerName);
//        }
    }
}
