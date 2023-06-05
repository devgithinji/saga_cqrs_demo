package com.densoft.demoproductsservice.query.api.projection;

import com.densoft.demoproductsservice.command.api.data.ProductRepository;
import com.densoft.demoproductsservice.command.api.model.ProductRestModel;
import com.densoft.demoproductsservice.query.api.queries.GetProductsQuery;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductProjection {
    private final ProductRepository productRepository;

    @QueryHandler
    public List<ProductRestModel> handle(GetProductsQuery getProductsQuery) {
        return productRepository.findAll().stream().map(product -> ProductRestModel
                .builder()
                .price(product.getPrice())
                .name(product.getName())
                .quantity(product.getQuantity()).build()).toList();
    }
}
