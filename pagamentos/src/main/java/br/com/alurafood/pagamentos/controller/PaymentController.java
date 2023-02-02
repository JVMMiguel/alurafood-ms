package br.com.alurafood.pagamentos.controller;

import br.com.alurafood.pagamentos.dto.PaymentDTO;
import br.com.alurafood.pagamentos.service.PaymentService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService service;

    @GetMapping
    public Page<PaymentDTO> listAll(@PageableDefault(size = 10) Pageable page) {
        return service.getAll(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDTO> getById(@PathVariable @NotNull Long id) {
        PaymentDTO dto = service.findById(id);

        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<PaymentDTO> createPayment(@RequestBody @Valid PaymentDTO paymentDTO, UriComponentsBuilder uriBuilder) {
        PaymentDTO payment = service.createPayment(paymentDTO);
        URI address = uriBuilder.path("/payments/{id}").buildAndExpand(payment.getId()).toUri();

        return ResponseEntity.created(address).body(payment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentDTO> updatePayment(@PathVariable @NotNull Long id, @RequestBody @Valid PaymentDTO paymentDTO) {
        PaymentDTO updatedPayment = service.updatePayment(id, paymentDTO);

        return ResponseEntity.ok(updatedPayment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PaymentDTO> deletePayment(@PathVariable @NotNull Long id) {
        service.deletePayment(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/confirm")
    @CircuitBreaker(name = "updateOrder", fallbackMethod = "authorizedPaymentWithPendingIntegration")
    public void confirmPayment(@PathVariable @NotNull Long id) {
        service.confirmPayment(id);
    }

    public void authorizedPaymentWithPendingIntegration(Long id, Exception e) {
        service.changeStatus(id);
    }

}
