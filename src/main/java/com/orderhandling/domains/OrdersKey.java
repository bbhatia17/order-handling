package com.orderhandling.domains;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
/**
 * This domain class is for primary key data of orders table
 */
@Data
@Builder
@PrimaryKeyClass
public class OrdersKey {

        @PrimaryKeyColumn(value = "user_email", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
        private String userEmail;

        @PrimaryKeyColumn(value = "tracking_id", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
        private String trackingId;

        @PrimaryKeyColumn(value = "order_no", ordinal = 2, type = PrimaryKeyType.PARTITIONED)
        private String orderNo;

}
