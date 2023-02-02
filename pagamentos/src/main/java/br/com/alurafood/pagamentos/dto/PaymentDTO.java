package br.com.alurafood.pagamentos.dto;

import br.com.alurafood.pagamentos.model.OrderItem;
import br.com.alurafood.pagamentos.model.Status;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter
public class PaymentDTO {

    private Long id;

    private BigDecimal paymentValue;

    private String name;

    private String number;

    private String expiration;

    private String code;

    private Status status;

    private Long orderId;

    private Long paymentMethodId;

    private List<OrderItem> items;

}
