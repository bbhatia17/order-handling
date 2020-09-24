package com.orderhandling.controller;

import com.orderhandling.domains.Orders;
import com.orderhandling.handler.OrderHandler;
import com.orderhandling.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Validated
@RestController
@RequestMapping("/")
public class OrderHandlingController {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderHandler orderHandler;

    @GetMapping("/orders")
    public Flux<Orders> getAllOrders() {
        Flux<Orders> orders = orderService.getAllOrders();
        return orders;
    }

    @GetMapping("orders/{user_email}")
    public Mono<Orders> getOrderByUserEmail(@Email @PathVariable String user_email) {
        return orderService.getOrderByUserEmail(user_email);
    }

    @GetMapping("orders/filterByProductType/{product_type}")
    public Mono<ResponseEntity> getOrdersFilterByProductType(@NotEmpty @PathVariable String product_type) {
        return orderService.getOrdersFilterByProductType(product_type).collectList()
                .map(order -> orderHandler.packingSlipGenerator(order));
    }

    @PutMapping("orders/membership")
    public Mono<String> updateMembershipType(
            @NotEmpty @Email
            @RequestParam(value = "user_email", required = true) String user_email,
            @NotEmpty @RequestParam(value = "membership_type", required = true) String membership_type) {
            return orderService.updateMembershipType(user_email,membership_type);
    }

}