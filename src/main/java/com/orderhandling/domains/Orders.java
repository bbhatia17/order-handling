package com.orderhandling.domains;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * This is domain class for orders table
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(value = "orders")

public class Orders {

        @PrimaryKey
        private OrdersKey ordersKey;

        @Column("user_email")
        private String userEmail;

        @Column("order_no")
        private String orderNo;

        @Column("tracking_id")
        private String trackingId;

        @Column("shipping_date")
        private LocalDateTime shippingDate;

        @Column("package")
        private String packageCount;

        @Column("billing_address")
        private String billingAddress;

        @Column("shipping_address")
        private String shippingAddress;

        @Column("phone_no")
        private String phoneNo;

        @Column("shipping_method")
        private String shippingMethod;

        @Column("product_type")
        private String productType;

        @Column("membership_type")
        private String membershipType;

        @Column("membership_status")
        private String membershipStatus;

        @Column("payment_status")
        private String paymentStatus;

        @Column("quantity")
        private String quantity;

        @Column("total_weight")
        private String totalWeight;

        @Column("price")
        private String price;

}
