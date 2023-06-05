package com.densoft.demoorderservice.command.api.saga;

import com.densoft.democommonservice.commands.*;
import com.densoft.democommonservice.events.*;
import com.densoft.democommonservice.model.User;
import com.densoft.democommonservice.queries.GetUserPaymentDetailsQuery;
import com.densoft.demoorderservice.command.api.events.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Saga
@Slf4j
public class OrderProcessingSaga {
    @Autowired
    private transient CommandGateway commandGateway;
    @Autowired
    private transient QueryGateway queryGateway;

    public OrderProcessingSaga() {
    }


    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderCreatedEvent orderCreatedEvent) {
        log.info("Order created event in saga for order id: {}", orderCreatedEvent.getOrderId());

        GetUserPaymentDetailsQuery getUserPaymentDetailsQuery = new GetUserPaymentDetailsQuery(orderCreatedEvent.getOrderId());

        User user = null;

        try {
            user = queryGateway.query(getUserPaymentDetailsQuery, ResponseTypes.instanceOf(User.class)).join();

            ValidatePaymentCommand validatePaymentCommand = ValidatePaymentCommand.builder()
                    .orderId(orderCreatedEvent.getOrderId())
                    .cardDetails(user.getCardDetails())
                    .paymentId(UUID.randomUUID().toString())
                    .build();

            commandGateway.sendAndWait(validatePaymentCommand);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            log.error("denno");
            //start the compensating transaction
            cancelOrderCommand(orderCreatedEvent.getOrderId());

        }


    }

    private void cancelOrderCommand(String orderId) {
        CancelOrderCommand cancelOrderCommand = new CancelOrderCommand(orderId);
        commandGateway.send(cancelOrderCommand);
    }

    @SagaEventHandler(associationProperty = "orderId")
    private void handle(PaymentProcessedEvent paymentProcessedEvent) {
        log.info("PaymentProcessedEvent in saga for order id: {}", paymentProcessedEvent.getOrderId());
        try {
            //mimic exception
            if (true)
                throw new Exception("exception occured");

            ShipOrderCommand shipOrderCommand = ShipOrderCommand.builder()
                    .shipmentId(UUID.randomUUID().toString())
                    .orderId(paymentProcessedEvent.getOrderId())
                    .build();
            commandGateway.send(shipOrderCommand);
        } catch (Exception e) {
            log.error(e.getMessage());
            //start the compensating transaction
            cancelPaymentCommand(paymentProcessedEvent);
        }
    }

    private void cancelPaymentCommand(PaymentProcessedEvent paymentProcessedEvent) {
        CancelPaymentCommand cancelPaymentCommand = new CancelPaymentCommand(paymentProcessedEvent.getPaymentId(),
                paymentProcessedEvent.getOrderId());
        commandGateway.send(cancelPaymentCommand);
    }

    @SagaEventHandler(associationProperty = "orderId")
    private void handle(OrderShippedEvent orderShippedEvent) {
        log.info("OrderShippedEvent in saga for order id: {}", orderShippedEvent.getOrderId());

        try {
            CompleteOrderCommand completeOrderCommand = CompleteOrderCommand.builder()
                    .orderId(orderShippedEvent.getOrderId())
                    .orderStatus("APPROVED")
                    .build();
            commandGateway.send(completeOrderCommand);
        } catch (Exception e) {
            log.error(e.getMessage());
            //start the compensating transaction
        }

    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderCompletedEvent orderCompletedEvent) {
        log.info("OrderCompletedEvent in saga for order id: {}", orderCompletedEvent.getOrderId());

    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderCancelledEvent orderCancelledEvent) {
        log.info("OrderCancelledEvent in saga for order id: {}", orderCancelledEvent.getOrderId());
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(PaymentCancelledEvent paymentCancelledEvent) {
        log.info("PaymentCancelledEvent in saga for order id: {}", paymentCancelledEvent.getOrderId());
        cancelOrderCommand(paymentCancelledEvent.getOrderId());
    }
}
