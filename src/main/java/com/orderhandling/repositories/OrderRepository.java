package com.orderhandling.repositories;

import com.orderhandling.domains.Orders;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderRepository extends ReactiveCassandraRepository<Orders, String> {
   @AllowFiltering
   Mono<Orders> findByUserEmail(String user_email);

   @AllowFiltering
   Flux<Orders> findByProductType(String product_type);
}
