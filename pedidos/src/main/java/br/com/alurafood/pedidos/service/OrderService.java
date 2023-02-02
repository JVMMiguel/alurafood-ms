package br.com.alurafood.pedidos.service;

import br.com.alurafood.pedidos.dto.OrderDTO;
import br.com.alurafood.pedidos.dto.StatusDTO;
import br.com.alurafood.pedidos.model.Order;
import br.com.alurafood.pedidos.model.Status;
import br.com.alurafood.pedidos.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    @Autowired
    private OrderRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    public List<OrderDTO> listAll() {
        return repository.findAll().stream()
                .map(o -> modelMapper.map(o, OrderDTO.class))
                .collect(Collectors.toList());
    }

    public OrderDTO getById(Long id) {
        Order order = repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        return modelMapper.map(order, OrderDTO.class);
    }

    public OrderDTO createOrder(OrderDTO orderDTO) {
        Order order = modelMapper.map(orderDTO, Order.class);

        order.setDateAndHour(LocalDateTime.now());
        order.setStatus(Status.REALIZED);
        order.getItems().forEach(item -> item.setOrder(order));

        repository.save(order);

        return modelMapper.map(order, OrderDTO.class);
    }

    public OrderDTO updateStatus(Long id, StatusDTO statusDTO) {
        Order order = repository.byIdWithItems(id);

        if (order == null) {
            throw new EntityNotFoundException();
        }

        order.setStatus(statusDTO.getStatus());
        repository.updateStatus(statusDTO.getStatus(), order);

        return modelMapper.map(order, OrderDTO.class);
    }

    public void approvePaymentOfOrder(Long id) {
        Order order = repository.byIdWithItems(id);

        if (order == null) {
            throw new EntityNotFoundException();
        }

        order.setStatus(Status.PAID);
        repository.updateStatus(Status.PAID, order);
    }

}
