package com.payments.regularpayments.controller;

import com.payments.regularpayments.dto.JournalEntryDto;
import com.payments.regularpayments.exception.*;
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
            description = "Создает регулярный платеж от плательщика получателю с " +
                    "указанием суммы транзакции и периода списания.<br>" +
                    "В случае успеха метод возвращает PaymentDto",
            parameters = @Parameter(name = "paymentCreateDto",
                    description = "creditAccount - банковский счет плательщика (12 цифр)<br>" +
                            "debitAccount - банковский счет получателя (12 цифр)<br>" +
                            "transactionAmount - сумма транзакции (дробная часть не больше 2 десятичных знаков)<br>" +
                            "writeOffPeriod - период повторения транзакции. Может быть указано одно или " +
                            "несколько значений: дни, часы, минуты",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentCreateDto.class))),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
                    examples = @ExampleObject(value = "{\"creditAccount\": 100000000000," +
                            " \"debitAccount\": 100000000001," +
                            " \"transactionAmount\": 5383.18," +
                            " \"writeOffPeriod\": {" +
                            "    \"days\": 30," +
                            "    \"hours\": 0," +
                            "    \"minutes\": 0" +
                            "  }}"))),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успех"),
                    @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса"),
                    @ApiResponse(responseCode = "404", description = "Номер счета не найден"),
                    @ApiResponse(responseCode = "500", description = "Ошибка сервера"),
                    @ApiResponse(responseCode = "406", description = "Счет отправителя и счет получателя идентичны")})
    public ResponseEntity<PaymentDto> createPayment(@RequestBody @Valid final PaymentCreateDto paymentCreateDto,
                                                    BindingResult bindingResult) throws
            BankAccountNotFoundException, FieldErrorException, IdenticalBankAccountsException {
        if (bindingResult.hasErrors()) {
            LOGGER.info("POST /create: {}", HttpStatus.BAD_REQUEST);
            throw new FieldErrorException(bindingResult.getFieldErrors(), "Объект имеет невалидные поля");
        }
        PaymentDto paymentDto;
        try {
            paymentDto = paymentService.create(paymentCreateDto);
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

    @GetMapping("/get")
    @Operation(summary = "Получить платеж по идентификатору",
            description = "Возвращает PaymentDto информацию о платеже по заданному идентификатору",
            parameters = @Parameter(name = "id", description = "идентификатор искомого платежа"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успех"),
                    @ApiResponse(responseCode = "404", description = "По заданному идентификатору платеж не найден"),
                    @ApiResponse(responseCode = "500", description = "Ошибка сервера")})
    public ResponseEntity<PaymentDto> getPayment(@RequestParam("id") final long id) throws
            PaymentNotFoundException {
        PaymentDto paymentDto;
        try {
            paymentDto = paymentService.findById(id);
        } catch (PaymentNotFoundException e) {
            LOGGER.info("GET /get?id={} {}", id, HttpStatus.NOT_FOUND);
            throw e;
        }
        LOGGER.info("GET /get?id={} {}", id, HttpStatus.OK);
        return ResponseEntity.ok(paymentDto);
    }

    @PutMapping("/update")
    @Operation(summary = "Обновить информацию о платеже",
            description = "Обновляет информацию о платеже по заданному идентификатору<br>" +
                    "В случае успеха ничего не возвращает",
            parameters = @Parameter(name = "paymentDto",
                    description = "creditAccount - банковский счет плательщика (12 цифр)<br>" +
                            "debitAccount - банковский счет получателя (12 цифр)<br>" +
                            "transactionAmount - сумма транзакции (дробная часть не больше 2 десятичных знаков)<br>" +
                            "writeOffPeriod - период повторения транзакции. Может быть указано одно или " +
                            "несколько значений: дни, часы, минуты<br>" +
                            "id - идентификатор платежа",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentDto.class))),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
                    examples = @ExampleObject(value = "{\n" +
                            "  \"creditAccount\": 100000000007,\n" +
                            "  \"debitAccount\": 100000000005,\n" +
                            "  \"transactionAmount\": 1000,\n" +
                            "  \"writeOffPeriod\": {\n" +
                            "    \"days\": 15,\n" +
                            "    \"hours\": 4,\n" +
                            "    \"minutes\": null\n" +
                            "  },\n" +
                            "  \"id\": 3\n" +
                            "}"))),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успех"),
                    @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса"),
                    @ApiResponse(responseCode = "404", description = "Платеж или номер счета не найден"),
                    @ApiResponse(responseCode = "500", description = "Ошибка сервера"),
                    @ApiResponse(responseCode = "406", description = "Счет отправителя и счет получателя идентичны")})
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
            throw bankAccountNotFoundException;
        } catch (IdenticalBankAccountsException identicalBankAccountsException) {
            LOGGER.info("PUT /update: {}", HttpStatus.NOT_ACCEPTABLE);
            throw identicalBankAccountsException;
        }
        LOGGER.info("PUT /update: {}", HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/get-all-payments-by-payer-inn")
    @Operation(summary = "Получить список всех платежей по ИНН плательщика",
            description = "Возвращает список всех платежей List&lt;PaymentDto&gt; по указанному ИНН плательщика<br>" +
                    "Если по заданному ИНН плательщика платежи не найдены - возвращает пустой список",
            parameters = @Parameter(name = "inn", description = "ИНН плательщика"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успех", content =
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaymentDto.class))),
                    @ApiResponse(responseCode = "500", description = "Ошибка сервера")})
    public ResponseEntity<List<PaymentDto>> getAllPaymentsByPayerInn(@RequestParam("inn") final long inn) {
        List<PaymentDto> paymentDtoList = paymentService.getPaymentEntitiesByPayerInn(inn);
        LOGGER.info("GET /get-all-payments-by-payer-inn?inn={}: {}", inn, HttpStatus.OK);
        return ResponseEntity.ok(paymentDtoList);
    }

    @GetMapping("/get-all-payments-by-recipient-inn")
    @Operation(summary = "Получить список всех платежей по ИНН получателя",
            description = "Возвращает список всех платежей List&lt;PaymentDto&gt; по указанному ИНН получателя<br>" +
                    "Если по заданному ИНН получателя платежи не найдены - возвращает пустой список",
            parameters = @Parameter(name = "inn", description = "ИНН получателя"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успех", content =
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaymentDto.class))),
                    @ApiResponse(responseCode = "500", description = "Ошибка сервера")})
    public ResponseEntity<List<PaymentDto>> getAllPaymentsByRecipientInn(@RequestParam("inn") final long inn) {
        List<PaymentDto> paymentDtoList = paymentService.getPaymentEntitiesByRecipientInn(inn);
        LOGGER.info("GET /get-all-payments-by-recipient-inn?inn={}: {}", inn, HttpStatus.OK);
        return ResponseEntity.ok(paymentDtoList);
    }

    @GetMapping("/get-all-payments")
    @Operation(summary = "Получить список всех платежей",
            description = "Возвращает список всех платежей List&lt;PaymentDto&gt;<br>" +
                    "Если платежи не найдены - возвращает пустой список",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успех", content =
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaymentDto.class))),
                    @ApiResponse(responseCode = "500", description = "Ошибка сервера")})
    public ResponseEntity<List<PaymentDto>> getAllPayments() {
        List<PaymentDto> paymentDtoList = paymentService.getAllPayments();
        LOGGER.info("GET /get-all-payments: {}", HttpStatus.OK);
        return ResponseEntity.ok(paymentDtoList);
    }
}
