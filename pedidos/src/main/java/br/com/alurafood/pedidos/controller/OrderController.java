package br.com.alurafood.pedidos.controller;

import br.com.alurafood.pedidos.dto.OrderDTO;
import br.com.alurafood.pedidos.dto.StatusDTO;
import br.com.alurafood.pedidos.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService service;

    @GetMapping
    public List<OrderDTO> listAll() {
        return service.listAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getById(@PathVariable @NotNull Long id) {
        OrderDTO orderDTO = service.getById(id);

        return ResponseEntity.ok(orderDTO);
    }

    @GetMapping("/port")
    public String returnPort(@Value("${local.server.port}") String port) {
        return String.format("The request was responded by the instance in port %s", port);
    }

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody @Valid OrderDTO orderDTO, UriComponentsBuilder uriBuilder) {
        OrderDTO realizedOrder = service.createOrder(orderDTO);

        URI address = uriBuilder.path("/orders/{id}").buildAndExpand(realizedOrder.getId()).toUri();

        return ResponseEntity.created(address).body(realizedOrder);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<OrderDTO> updateStatus(@PathVariable Long id, @RequestBody StatusDTO statusDTO) {
        OrderDTO orderDTO = service.updateStatus(id, statusDTO);

        return ResponseEntity.ok(orderDTO);
    }


    @PutMapping("/{id}/paid")
    public ResponseEntity<Void> approvePayment(@PathVariable @NotNull Long id) {
        service.approvePaymentOfOrder(id);

        return ResponseEntity.ok().build();
    }

}
