package br.com.alurafood.pedidos.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {

    private Long id;

    private Integer quantity;

    private String description;

}
