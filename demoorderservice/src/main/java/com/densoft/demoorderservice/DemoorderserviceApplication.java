package com.densoft.demoorderservice;

import com.thoughtworks.xstream.XStream;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.gateway.DefaultCommandGateway;
import org.axonframework.queryhandling.DefaultQueryGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoorderserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoorderserviceApplication.class, args);
    }


    @Bean
    public XStream xStream() {
        XStream xStream = new XStream();

        xStream.allowTypesByWildcard(new String[] { "com.densoft.**" });
        return xStream;
    }

}
