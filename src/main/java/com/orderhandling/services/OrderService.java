package com.orderhandling.services;

import com.orderhandling.domains.Orders;
import com.orderhandling.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static com.orderhandling.util.OrderHandlingConstants.PAYMENT_STATUS;
import static com.orderhandling.util.OrderHandlingConstants.PRODUCT_TYPE_BOOK;
import static com.orderhandling.util.OrderHandlingConstants.PRODUCT_TYPE_PHYSICAL;
import static com.orderhandling.util.OrderHandlingConstants.PRODUCT_TYPE_VIDEO;

@AllArgsConstructor
@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    public Flux<Orders> getAllOrders() {
        Flux<Orders> orders =  orderRepository.findAll();
        return orders;
    }

    public Mono<Orders> getOrderByUserEmail(String user_email) {
        return orderRepository.findByUserEmail(user_email);
    }

    public Flux<Orders> getOrdersFilterByProductType(String product_type) {
        return orderRepository.findByProductType(product_type)
                .filter(order -> ((order.getProductType().equalsIgnoreCase(PRODUCT_TYPE_BOOK)
                                || order.getProductType().equalsIgnoreCase(PRODUCT_TYPE_PHYSICAL)
                                || order.getProductType().equalsIgnoreCase(PRODUCT_TYPE_VIDEO))
                                && order.getPaymentStatus().equalsIgnoreCase(PAYMENT_STATUS)));
    }

    public Mono<String> updateMembershipType(String user_email, String membership_type) {
        return orderRepository.findByUserEmail(user_email).flatMap(orders -> {
            orders.setMembershipType(membership_type);
            return orderRepository.save(orders).then(Mono.just("updated"));}
                ).switchIfEmpty(orderRepository.save(Orders.builder().userEmail(user_email)
                .membershipType(membership_type)
                .trackingId(UUID.randomUUID().toString())
                .orderNo(UUID.randomUUID().toString())
                .build())
                .then(Mono.just("created")));
    }
}