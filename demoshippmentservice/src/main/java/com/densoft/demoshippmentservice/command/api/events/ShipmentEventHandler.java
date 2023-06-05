package com.densoft.demoshippmentservice.command.api.events;

import com.densoft.democommonservice.events.OrderShippedEvent;
import com.densoft.demoshippmentservice.data.Shipment;
import com.densoft.demoshippmentservice.data.ShipmentRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShipmentEventHandler {

    private final ShipmentRepository shipmentRepository;

    @EventHandler
    public void on(OrderShippedEvent orderShippedEvent) {
        Shipment shipment = new Shipment();
        BeanUtils.copyProperties(orderShippedEvent, shipment);
        shipmentRepository.save(shipment);
    }
}
