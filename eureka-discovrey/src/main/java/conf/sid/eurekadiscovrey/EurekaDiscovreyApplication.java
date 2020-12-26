package conf.sid.eurekadiscovrey;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaDiscovreyApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaDiscovreyApplication.class, args);
    }

}
