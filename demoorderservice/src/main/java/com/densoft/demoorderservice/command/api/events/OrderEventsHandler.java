package com.densoft.demoorderservice.command.api.events;

import com.densoft.democommonservice.events.OrderCancelledEvent;
import com.densoft.democommonservice.events.OrderCompletedEvent;
import com.densoft.demoorderservice.data.Order;
import com.densoft.demoorderservice.data.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventsHandler {

    private final OrderRepository orderRepository;

    @EventHandler
    public void on(OrderCreatedEvent orderCreatedEvent) {
        Order order = new Order();
        BeanUtils.copyProperties(orderCreatedEvent, order);
        orderRepository.save(order);
    }

    @EventHandler
    public void on(OrderCompletedEvent orderCompletedEvent) {
        Order order = orderRepository.findById(orderCompletedEvent.getOrderId()).get();
        order.setOrderStatus(orderCompletedEvent.getOrderStatus());
        orderRepository.save(order);

    }

    @EventHandler
    public void on(OrderCancelledEvent orderCancelledEvent) {
        Order order = orderRepository.findById(orderCancelledEvent.getOrderId()).get();
        order.setOrderStatus(orderCancelledEvent.getOrderStatus());
        orderRepository.save(order);
    }
}
