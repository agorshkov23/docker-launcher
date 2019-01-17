package io.github.agorshkov23.dockerlauncher.docker.compose.v3;

import com.spotify.docker.client.messages.ContainerConfig;
import lombok.*;

@Data
public class ServiceSpec {
    private String containerName;
    private String image;

    public ContainerConfig.Builder toContainerConfigBuilder() {
        ContainerConfig.Builder builder = ContainerConfig.builder();
        if (image == null) {
            throw new RuntimeException("image is null temporary is not supported");
        } else {
            builder.image(image);
        }
        return builder;
    }
}
