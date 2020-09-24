package com.orderhandling.services;

import com.orderhandling.domains.Orders;
import com.orderhandling.repositories.OrderRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import javax.naming.ServiceUnavailableException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {OrderService.class})
public class OrderServiceTest {

    OrderService service;

    @MockBean
    private OrderRepository repository;

    @Before
    public void setUp() {
        service = new OrderService(repository);
    }

    @Test
    public void shouldReturnAllOrders() {

        //given
        Orders order = Orders.builder()
                .productType("book")
                .userEmail("babita.arora@gmail.com")
                .orderNo("12345")
                .trackingId("123")
                .build();

        given(repository.findAll()).willReturn(Flux.just(order));

        // when
        Flux<Orders> configurableItemsResponse = service.getAllOrders();

        //then
        StepVerifier.create(configurableItemsResponse)
                .assertNext(response -> {
                    assertEquals(order.getTrackingId(), response.getTrackingId());
                })
                .verifyComplete();
    }

    @Test
    public void shouldGetOrderByUserEmail() {

        //given
        Orders order = Orders.builder()
                .productType("book")
                .userEmail("babita.arora@gmail.com")
                .orderNo("12345")
                .trackingId("123")
                .build();

        given(repository.findByUserEmail("babita.arora@gmail.com")).willReturn(Mono.just(order));

        // when
        Mono<Orders> orderResponse = service.getOrderByUserEmail("babita.arora@gmail.com");

        //then
        StepVerifier.create(orderResponse)
                .assertNext(response -> {
                    assertEquals(order.getTrackingId(), response.getTrackingId());
                })
                .verifyComplete();
    }

    @Test
    public void shouldNotGetOrderByUserEmailDatabaseUnavailable() {

        //given
        Orders order = Orders.builder()
                .productType("book")
                .userEmail("babita.arora@gmail.com")
                .orderNo("12345")
                .trackingId("123")
                .build();

        given(repository.findByUserEmail("babita.arora@gmail.com")).willReturn(Mono.error(new ServiceUnavailableException()));

        // when
        Mono<Orders> orderResponse = service.getOrderByUserEmail("babita.arora@gmail.com");

        //then
        StepVerifier.create(orderResponse).expectErrorMatches(throwable -> throwable instanceof ServiceUnavailableException)
                .verify();

    }

    @Test
    public void shouldCreateMembershipType() {

        //given
        Orders order = Orders.builder()
                .productType("book")
                .userEmail("babita.arora@gmail.com")
                .orderNo("12345")
                .trackingId("123")
                .membershipType("new")
                .build();

        given(repository.findByUserEmail("babita.arora@gmail.com")).willReturn(Mono.empty());
        given(repository.save(any())).willReturn(Mono.empty());

        // when
        Mono<String> orderResponse = service.updateMembershipType("babita.arora@gmail.com","new");

        //then
        StepVerifier.create(orderResponse)
                .assertNext(response -> {
                    assertEquals("created",response);
                })
                .verifyComplete();

    }

    @Test
    public void shouldCreateNewMembershipType() {

        //given
        Orders order = Orders.builder()
                .productType("book")
                .userEmail("babita.arora@gmail.com")
                .orderNo("12345")
                .trackingId("123")
                .membershipType("new")
                .build();

        given(repository.findByUserEmail("babita.arora@gmail.com")).willReturn(Mono.empty());
        given(repository.save(any())).willReturn(Mono.empty());

        // when
        Mono<String> orderResponse = service.updateMembershipType("babita.arora@gmail.com","new");

        //then
        StepVerifier.create(orderResponse)
                .assertNext(response -> {
                    assertEquals("created",response);
                })
                .verifyComplete();

    }

    public void shouldUpdateExistingMembershipType() {

        //given
        Orders order = Orders.builder()
                .productType("book")
                .userEmail("bbhatia17@gmail.com")
                .orderNo("12345")
                .trackingId("123")
                .membershipType("updated")
                .build();

        given(repository.findByUserEmail("bbhatia17@gmail.com")).willReturn(Mono.just(order));
        given(repository.save(any())).willReturn(Mono.just(order));

        // when
        Mono<String> orderResponse = service.updateMembershipType("bbhatia17@gmail.com","updated");

        //then
        StepVerifier.create(orderResponse)
                .assertNext(response -> {
                    assertEquals("updated",response);
                })
                .verifyComplete();

    }
}
