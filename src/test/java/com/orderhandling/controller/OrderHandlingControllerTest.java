package com.orderhandling.controller;

import com.orderhandling.domains.Orders;
import com.orderhandling.handler.OrderHandler;
import com.orderhandling.services.OrderService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@WebFluxTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {OrderHandlingController.class})
public class OrderHandlingControllerTest {

    private static final String BASE_URL = "/orders";
    private static final String ZEROTH_ELEMENT = "[0]";

    @Autowired
    private ApplicationContext context;

    private WebTestClient webTestClient;

    @MockBean
    private OrderService service;

    @MockBean
    private OrderHandler handler;

    @Before
    public void setUp() {
        webTestClient = WebTestClient.bindToApplicationContext(context).build();
    }

    @AfterEach
    public void tearDown() {
        webTestClient = null;
    }

    @Test
    public void shouldGetAllOrders() {
        //given
        Orders order = Orders.builder()
                .productType("book")
                .userEmail("babita.arora@gmail.com")
                .orderNo("12345")
                .trackingId("123")
                .build();
        given(service.getAllOrders()).willReturn(Flux.just(order));
        //when
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(BASE_URL)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                //then
                .jsonPath(ZEROTH_ELEMENT).isNotEmpty()
                .jsonPath(ZEROTH_ELEMENT + "trackingId").isEqualTo("123")
                .jsonPath(ZEROTH_ELEMENT + "orderNo").isEqualTo("12345")
                .jsonPath(ZEROTH_ELEMENT + "userEmail").isEqualTo("babita.arora@gmail.com");

    }

    @Test
    public void shouldNotUpdateMembershipType(){
        webTestClient.put().uri(BASE_URL + "/membership")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void shouldUpdateMembershipType(){
        webTestClient.put().uri(BASE_URL + "/membership?membership_type=upgrade&user_email=senthil.arunachalam@gmail.com")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void shouldGetAllOrdersBasedOnUserEmail() {
        //given
        Orders order = Orders.builder()
                .productType("book")
                .userEmail("babita.arora@gmail.com")
                .orderNo("12345")
                .trackingId("123")
                .build();
        given(service.getOrderByUserEmail(any())).willReturn(Mono.just(order));
        //when
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(BASE_URL +"/babita.arora@gmail.com")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                //then
                .jsonPath( "trackingId").isEqualTo("123")
                .jsonPath( "orderNo").isEqualTo("12345")
                .jsonPath( "userEmail").isEqualTo("babita.arora@gmail.com");

    }
}