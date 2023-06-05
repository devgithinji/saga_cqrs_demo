package com.densoft.demousersservice;

import com.thoughtworks.xstream.XStream;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemousersserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemousersserviceApplication.class, args);
    }

    @Bean
    public XStream xStream() {
        XStream xStream = new XStream();

        xStream.allowTypesByWildcard(new String[] { "com.densoft.**" });
        return xStream;
    }

}
