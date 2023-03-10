package br.com.alurafood.pedidos.dto;

import br.com.alurafood.pedidos.model.OrderItem;
import br.com.alurafood.pedidos.model.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    private Long id;

    private LocalDateTime dateAndHour;

    private Status status;

    private List<OrderItem> items = new ArrayList<>();

}
