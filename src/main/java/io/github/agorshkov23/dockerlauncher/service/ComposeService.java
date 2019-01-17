package io.github.agorshkov23.dockerlauncher.service;

import io.github.agorshkov23.dockerlauncher.config.property.DockerLauncherProperties;
import io.github.agorshkov23.dockerlauncher.docker.compose.v3.Compose3;
import io.github.agorshkov23.dockerlauncher.helper.ComposeFileHelper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class ComposeService extends AbstractService {

    private final ComposeFileHelper composeFileHelper;
    private final DockerLauncherProperties properties;

    @Getter
    private Compose3 compose = null;

    @PostConstruct
    public void onPostConstruct() {
        String composeLocation = properties.getComposeLocation();
        Compose3 compose = composeFileHelper.load(composeLocation);
        this.compose = Objects.requireNonNull(compose);
    }
}
