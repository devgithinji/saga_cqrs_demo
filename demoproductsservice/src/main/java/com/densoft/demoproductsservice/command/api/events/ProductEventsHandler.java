package com.densoft.demoproductsservice.command.api.events;

import com.densoft.demoproductsservice.command.api.data.Product;
import com.densoft.demoproductsservice.command.api.data.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ProcessingGroup("product")
public class ProductEventsHandler {
    private final ProductRepository productRepository;

    @EventHandler
    public void on(ProductCreatedEvent productCreatedEvent) throws Exception {
        Product product = new Product();
        BeanUtils.copyProperties(productCreatedEvent, product);
        productRepository.save(product);
        throw new Exception("Exception occured");
    }

    @ExceptionHandler
    public void handleException(Exception exception) throws Exception {
        throw exception;
    }
}
