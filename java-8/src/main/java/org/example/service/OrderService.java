package org.example.service;

import org.example.domain.Order;

import java.util.List;

public interface OrderService {

    List<Order> getOrdersByOrganizationId(String organizationId);
}
