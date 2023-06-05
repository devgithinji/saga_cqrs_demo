package com.densoft.demopaymentsservice.command.api.events;

import com.densoft.democommonservice.events.PaymentCancelledEvent;
import com.densoft.democommonservice.events.PaymentProcessedEvent;
import com.densoft.demopaymentsservice.data.Payment;
import com.densoft.demopaymentsservice.data.PaymentRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class PaymentsEventHandler {
    private final PaymentRepository paymentRepository;

    public PaymentsEventHandler(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @EventHandler
    public void on(PaymentProcessedEvent paymentProcessedEvent) {
        Payment payment = Payment.builder()
                .paymentId(paymentProcessedEvent.getPaymentId())
                .orderId(paymentProcessedEvent.getOrderId())
                .paymentStatus("COMPLETED")
                .timeStamp(new Date())
                .build();

        paymentRepository.save(payment);
    }

    @EventHandler
    public void on(PaymentCancelledEvent paymentCancelledEvent) {
        Payment payment = paymentRepository.findById(paymentCancelledEvent.getPaymentId()).get();
        payment.setPaymentStatus(paymentCancelledEvent.getPaymentStatus());
        paymentRepository.save(payment);
    }
}
