package io.github.agorshkov23.dockerlauncher;

//import com.github.dockerjava.api.DockerClient;
//import com.github.dockerjava.api.command.PingCmd;

import io.github.agorshkov23.dockerlauncher.config.property.DockerLauncherProperties;
import io.github.agorshkov23.dockerlauncher.docker.compose.v3.ServiceSpec;
import io.github.agorshkov23.dockerlauncher.service.ContainerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
@Slf4j
@AllArgsConstructor
public class DockerLauncherApplication implements ApplicationRunner {

    private final ContainerService containerService;

    private final DockerLauncherProperties properties;

    public static void main(String[] args) {
        SpringApplication.run(DockerLauncherApplication.class, args);
    }

    @PostConstruct
    void onPostConstruct() throws Exception {

        ServiceSpec spec = new ServiceSpec();
        spec.setImage("nginx:alpine");
//        containerService.up("nginx", spec);

//        DefaultDockerClient dockerClient = DefaultDockerClient.fromEnv().build();
//        List<com.spotify.docker.client.messages.Container> containers1 = dockerClient.listContainers(com.spotify.docker.client.DockerClient.ListContainersParam.allContainers());
//        for (com.spotify.docker.client.messages.Container container : containers1) {
//            System.out.println(container);
//        }


//        String ping = dockerClient.ping();
//        dockerClient.close();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("started application with properties={}", properties);
    }
}
