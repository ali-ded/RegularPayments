package com.payments.regularpayments.controller;

import com.payments.regularpayments.exception.BankAccountNotFoundException;
import com.payments.regularpayments.exception.FieldErrorException;
import com.payments.regularpayments.exception.IdenticalBankAccountsException;
import com.payments.regularpayments.exception.PaymentNotFoundException;
import com.payments.regularpayments.dto.PaymentCreateDto;
import com.payments.regularpayments.dto.PaymentDto;
import com.payments.regularpayments.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/payment", produces = MediaType.APPLICATION_JSON_VALUE)
public class PaymentController {
    private final Logger LOGGER = LoggerFactory.getLogger(PaymentController.class);
    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create")
    @Operation(summary = "Создать новый платеж",
            description = "Создает платеж от плательщика получателю с указанием суммы транзакции.<br>" +
                    "В случае успеха метод возвращает PaymentDto",
            parameters = @Parameter(name = "paymentCreateDto",
                    description = "creditAccount - банковский счет плательщика (12 цифр)<br>" +
                            "debitAccount - банковский счет получателя (12 цифр)<br>" +
                            "transactionAmount - сумма транзакции (дробная часть не больше 2 десятичных знаков)",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentCreateDto.class))),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
                    examples = @ExampleObject(value = "{\"creditAccount\": 100000000000, \"debitAccount\": 100000000001, \"transactionAmount\": 5383.18}"))),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успех"),
                    @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса"),
                    @ApiResponse(responseCode = "404", description = "Номер счета не найден"),
                    @ApiResponse(responseCode = "500", description = "Ошибка сервера")
            })
    public ResponseEntity<PaymentDto> createPayment(@RequestBody @Valid final PaymentCreateDto paymentCreateDto,
                                              BindingResult bindingResult) throws
            BankAccountNotFoundException, FieldErrorException, IdenticalBankAccountsException {
        if (bindingResult.hasErrors()) {
            LOGGER.info("POST /create: {}", HttpStatus.BAD_REQUEST);
            throw new FieldErrorException(bindingResult.getFieldErrors(), "Объект имеет невалидные поля");
        }
        PaymentDto paymentDto;
        try {
            paymentDto = paymentService.save(paymentCreateDto);
        } catch (BankAccountNotFoundException bankAccountNotFoundException) {
            LOGGER.info("POST /create: {}", HttpStatus.NOT_FOUND);
            throw bankAccountNotFoundException;
        } catch (IdenticalBankAccountsException identicalBankAccountsException) {
            LOGGER.info("POST /create: {}", HttpStatus.NOT_ACCEPTABLE);
            throw identicalBankAccountsException;
        }
        LOGGER.info("POST /create: {}", HttpStatus.OK);
        return ResponseEntity.ok(paymentDto);
    }

    @PutMapping("/update")
    public ResponseEntity<Void> updatePayment(@RequestBody @Valid final PaymentDto paymentDto,
                                              BindingResult bindingResult) throws
            FieldErrorException, PaymentNotFoundException, BankAccountNotFoundException, IdenticalBankAccountsException {
        if (bindingResult.hasErrors()) {
            LOGGER.info("PUT /update: {}", HttpStatus.BAD_REQUEST);
            throw new FieldErrorException(bindingResult.getFieldErrors(), "Объект имеет невалидные поля");
        }
        try {
            paymentService.update(paymentDto);
        } catch (PaymentNotFoundException paymentNotFoundException) {
            LOGGER.info("PUT /update: {} (payment #{} not found)", HttpStatus.NOT_FOUND, paymentDto.getId());
            throw paymentNotFoundException;
        } catch (BankAccountNotFoundException bankAccountNotFoundException) {
            LOGGER.info("PUT /update: {}", HttpStatus.NOT_FOUND);
            throw  bankAccountNotFoundException;
        } catch (IdenticalBankAccountsException identicalBankAccountsException) {
            LOGGER.info("PUT /update: {}", HttpStatus.NOT_ACCEPTABLE);
            throw identicalBankAccountsException;
        }
        LOGGER.info("PUT /update: {}", HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/get-all-payments-by-payer-inn")
    public ResponseEntity<List<PaymentDto>> getAllPaymentsByPayerInn(@RequestParam ("inn") final long inn) {
        List<PaymentDto> paymentDtoList = paymentService.getPaymentEntitiesByPayerInn(inn);
        LOGGER.info("GET /get-all-payments-by-payer-inn: {}", HttpStatus.OK);
        return ResponseEntity.ok(paymentDtoList);
    }

    @GetMapping("/get-all-payments-by-recipient-inn")
    public ResponseEntity<List<PaymentDto>> getAllPaymentsByRecipientInn(@RequestParam ("inn") final long inn) {
        List<PaymentDto> paymentDtoList = paymentService.getPaymentEntitiesByRecipientInn(inn);
        LOGGER.info("GET /get-all-payments-by-recipient-inn: {}", HttpStatus.OK);
        return ResponseEntity.ok(paymentDtoList);
    }
}
