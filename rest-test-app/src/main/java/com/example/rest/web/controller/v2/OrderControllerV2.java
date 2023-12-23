package com.example.rest.web.controller.v2;

import com.example.rest.mapper.v2.OrderMapperV2;
import com.example.rest.model.Order;
import com.example.rest.service.OrderService;
import com.example.rest.web.model.OrderFilter;
import com.example.rest.web.model.OrderListResponse;
import com.example.rest.web.model.OrderResponse;
import com.example.rest.web.model.UpsertOrderRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/order")
@RequiredArgsConstructor
public class OrderControllerV2 {

    private final OrderService databaseOrderService;
    private final OrderMapperV2 orderMapper;

    @GetMapping
    public ResponseEntity<OrderListResponse> findAll() {
        return ResponseEntity.ok(
                orderMapper.orderListToOrderListResponse(databaseOrderService.findAll())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
                orderMapper.orderToResponse(databaseOrderService.findById(id))
        );
    }

    @PostMapping
    public ResponseEntity<OrderResponse> create(@RequestBody @Valid UpsertOrderRequest request) {
        Order newOrder = databaseOrderService.save(orderMapper.requestToOrder(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(orderMapper.orderToResponse(newOrder));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> update(@RequestBody @Valid UpsertOrderRequest request,
                                                @PathVariable("id") Long orderId) {
        Order updatedOrder = databaseOrderService.update(orderMapper.requestToOrder(orderId, request));
        return ResponseEntity.ok(orderMapper.orderToResponse(updatedOrder));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        databaseOrderService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filter")
    public ResponseEntity<OrderListResponse> filterBy(@Valid OrderFilter filter) {
        return ResponseEntity.ok(orderMapper.orderListToOrderListResponse(databaseOrderService.filterBy(filter)));
    }
}
