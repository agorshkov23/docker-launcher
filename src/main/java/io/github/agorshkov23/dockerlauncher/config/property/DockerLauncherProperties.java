package io.github.agorshkov23.dockerlauncher.config.property;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Параметры приложения.
 */
@Component
@ConfigurationProperties(value = "docker.launcher")
@Data
public class DockerLauncherProperties {

    /**
     * Название проекта.
     */
    private String name = "docker-launcher";

    /**
     * Расположение файла docker-compose.yml
     */
    private String composeLocation = "docker-compose.yml";

    /**
     * Настройки сервисов.
     */
    private ServicesProperties services = new ServicesProperties();

    /**
     * Параметры сервисов.
     */
    @Data
    public static class ServicesProperties {

        /**
         * Параметры сервиса мониторинга.
         */
        private MonitoringServiceProperties monitoring = new MonitoringServiceProperties();
    }

    /**
     * Параметры сервиса мониторинга.
     */
    @Data
    public static class MonitoringServiceProperties {

        /**
         * Признак активности сервиса.
         */
        private boolean active = false;

        /**
         * Частота проверки.
         */
        private int checkRate = 5000;
    }
}
