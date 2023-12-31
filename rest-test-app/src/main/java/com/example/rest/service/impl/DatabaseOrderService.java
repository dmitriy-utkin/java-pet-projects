package com.example.rest.service.impl;

import com.example.rest.exception.EntityNotFoundException;
import com.example.rest.model.Client;
import com.example.rest.model.Order;
import com.example.rest.repository.DatabaseOrderRepository;
import com.example.rest.repository.OrderSpecification;
import com.example.rest.service.ClientService;
import com.example.rest.service.OrderService;
import com.example.rest.utils.BeanUtils;
import com.example.rest.web.model.OrderFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DatabaseOrderService implements OrderService {

    private final DatabaseOrderRepository orderRepository;
    private final ClientService databaseClientService;


    @Override
    public List<Order> filterBy(OrderFilter filter) {
//        return orderRepository.getByProductJpql(filter.getProductName());

//        return orderRepository.getByProductSql(filter.getProductName());

        return orderRepository.findAll(OrderSpecification.withFilter(filter),
                PageRequest.of(filter.getPageNum(), filter.getPageSize())).getContent();
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public Order findById(Long id) {
        return orderRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Order with ID " + id + " not found")
        );
    }

    @Override
    public Order save(Order order) {
        Client client = databaseClientService.findById(order.getClient().getId());
        order.setClient(client);
        return orderRepository.save(order);
    }

    @Override
    public Order update(Order order) {
        checkForUpdate(order.getId());
        Client client = databaseClientService.findById(order.getClient().getId());
        Order existedOrder = findById(order.getId());
        BeanUtils.copyNonNullProperties(order, existedOrder);
        existedOrder.setClient(client);
        return orderRepository.save(existedOrder);
    }

    @Override
    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public void deleteByIdIn(List<Long> ids) {
        orderRepository.deleteAllById(ids);
    }
}
