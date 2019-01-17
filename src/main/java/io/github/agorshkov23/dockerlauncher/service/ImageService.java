package io.github.agorshkov23.dockerlauncher.service;

import com.spotify.docker.client.DockerClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class ImageService extends AbstractService {

    private final DockerClient dockerClient;
}
