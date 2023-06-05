package com.densoft.democommonservice.commands;

import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Value
public class CancelOrderCommand {
    @TargetAggregateIdentifier
    private String orderId;
    private String orderStatus = "CANCELLED";
}
