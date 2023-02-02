package br.com.alurafood.pagamentos.service;

import br.com.alurafood.pagamentos.dto.PaymentDTO;
import br.com.alurafood.pagamentos.http.OrderClient;
import br.com.alurafood.pagamentos.model.Payment;
import br.com.alurafood.pagamentos.model.Status;
import br.com.alurafood.pagamentos.repository.PaymentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private OrderClient order;

    public Page<PaymentDTO> getAll(Pageable page) {
        return repository
                .findAll(page)
                .map(payment -> modelMapper.map(payment, PaymentDTO.class));
    }

    public PaymentDTO findById(Long id) {
        Payment payment = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cant find payment for this id"));

        PaymentDTO dto = modelMapper.map(payment, PaymentDTO.class);
        dto.setItems(order.getOrderItems(payment.getId()).getItems());

        return dto;
    }

    public PaymentDTO createPayment(PaymentDTO dto) {
        Payment payment = modelMapper.map(dto, Payment.class);
        payment.setStatus(Status.CREATED);
        repository.save(payment);

        return modelMapper.map(payment, PaymentDTO.class);
    }

    public PaymentDTO updatePayment(Long id, PaymentDTO dto) {
        Payment payment = modelMapper.map(dto, Payment.class);
        payment.setId(id);
        payment = repository.save(payment);

        return modelMapper.map(payment, PaymentDTO.class);
    }

    public void deletePayment(Long id) {
        repository.deleteById(id);
    }

    public void confirmPayment(Long id) {
        Optional<Payment> payment = repository.findById(id);

        if (!payment.isPresent()) {
            throw new EntityNotFoundException();
        }

        payment.get().setStatus(Status.CONFIRMED);
        repository.save(payment.get());
        order.updatePayment(payment.get().getOrderId());
    }

    public void changeStatus(Long id) {
        Optional<Payment> payment = repository.findById(id);

        if (!payment.isPresent()) {
            throw new EntityNotFoundException();
        }

        payment.get().setStatus(Status.CONFIRMED_WITHOUT_INTEGRATION);
        repository.save(payment.get());
    }

}
