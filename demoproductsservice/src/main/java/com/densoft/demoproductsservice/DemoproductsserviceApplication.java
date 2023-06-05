package com.densoft.demoproductsservice;

import com.densoft.demoproductsservice.command.api.exception.ProductsServiceEventsErrorHandler;
import com.thoughtworks.xstream.XStream;
import org.axonframework.config.EventProcessingConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoproductsserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoproductsserviceApplication.class, args);
    }

    @Autowired
    public void configure(EventProcessingConfigurer eventProcessingConfigurer) {
        eventProcessingConfigurer.registerListenerInvocationErrorHandler(
                "product",
                configuration -> new ProductsServiceEventsErrorHandler()
        );
    }

    @Bean
    public XStream xStream() {
        XStream xStream = new XStream();

        xStream.allowTypesByWildcard(new String[] { "com.densoft.**" });
        return xStream;
    }

}
