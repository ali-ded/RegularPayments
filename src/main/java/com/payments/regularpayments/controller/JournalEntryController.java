package com.payments.regularpayments.controller;

import com.payments.regularpayments.dto.JournalEntryDto;
import com.payments.regularpayments.exception.JournalEntryNotFoundException;
import com.payments.regularpayments.exception.PaymentNotFoundException;
import com.payments.regularpayments.service.JournalEntryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/journal-entry", produces = MediaType.APPLICATION_JSON_VALUE)
public class JournalEntryController {
    private final JournalEntryService journalEntryService;
    private final Logger LOGGER;

    public JournalEntryController(JournalEntryService journalEntryService) {
        this.journalEntryService = journalEntryService;
        LOGGER = LoggerFactory.getLogger(JournalEntryController.class);
    }

    @Operation(summary = "Создать проводку по платежу")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Проводка успешно создана", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = JournalEntryDto.class))
            }),
            @ApiResponse(responseCode = "404", description = "Указанный платеж не найден", content = @Content),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    @PostMapping("/create")
    public ResponseEntity<JournalEntryDto> createJournalEntry(@Parameter(description = "Идентификатор платежа")
                                                              @RequestParam("paymentId") final long paymentId) throws PaymentNotFoundException {
        JournalEntryDto journalEntryDto;
        try {
            journalEntryDto = journalEntryService.create(paymentId);
        } catch (PaymentNotFoundException e) {
            LOGGER.info("POST /create?paymentId={}: {}", paymentId, HttpStatus.NOT_FOUND);
            throw e;
        }
        LOGGER.info("POST /create?paymentId={}: {}", paymentId, HttpStatus.OK);
        return ResponseEntity.ok(journalEntryDto);
    }

    @Operation(summary = "Удалить(сторнировать) проводку по идентификатору",
            description = "Метод изменяет поле paymentResult на false и поле даты-времени paymentDate на текущую.<br>" +
                    "В случае успеха метод ничего не возвращает")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Проводка успешно удалена", content = @Content),
            @ApiResponse(responseCode = "404", description = "Указанная проводка не найдена", content = @Content),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteJournalEntry(@Parameter(description = "Идентификатор проводки")
                                                   @RequestParam("journalEntryId") final long journalEntryId)
            throws JournalEntryNotFoundException {
        try {
            journalEntryService.delete(journalEntryId);
        } catch (JournalEntryNotFoundException e) {
            LOGGER.info("DELETE /delete?paymentId={}: {}", journalEntryId, HttpStatus.NOT_FOUND);
            throw e;
        }
        LOGGER.info("DELETE /create?paymentId={}: {}", journalEntryId, HttpStatus.OK);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Получить проводку по идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Проводка успешно найдена", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = JournalEntryDto.class))
            }),
            @ApiResponse(responseCode = "404", description = "Проводка не найдена", content = @Content),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    @GetMapping("/get")
    public ResponseEntity<JournalEntryDto> getJournalEntry(@Parameter(description = "Идентификатор проводки")
                                                           @RequestParam final long journalEntryId) throws JournalEntryNotFoundException {
        JournalEntryDto journalEntryDto;
        try {
            journalEntryDto = journalEntryService.get(journalEntryId);
        } catch (JournalEntryNotFoundException e) {
            LOGGER.info("GET /get?journalEntryId={}: {}", journalEntryId, HttpStatus.NOT_FOUND);
            throw e;
        }
        LOGGER.info("GET /get?journalEntryId={}: {}", journalEntryId, HttpStatus.OK);
        return ResponseEntity.ok(journalEntryDto);
    }

    @Operation(summary = "Получить список всех проводок по идентификатору платежа",
            description = "Возвращает список всех проводок List&lt;JournalEntryDto&gt; по указанному идентификатору платежа<br>" +
                    "Если по заданному платежу проводки не найдены - возвращает пустой список",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успех"),
                    @ApiResponse(responseCode = "500", description = "Ошибка сервера")
            })
    @GetMapping("/get-all-journal-entries-by-payment")
    public ResponseEntity<List<JournalEntryDto>> getAllJournalEntries(@Parameter(description = "Идентификатор платежа")
                                                           @RequestParam final long paymentId) {
        List<JournalEntryDto> journalEntryDtoList = journalEntryService.getAll(paymentId);
        LOGGER.info("GET /get-all-journal-entries-by-payment?paymentId={}: {}", paymentId, HttpStatus.OK);
        return ResponseEntity.ok(journalEntryDtoList);
    }

    @Operation(summary = "Списание платежа",
            description = "Метод проверяет необходимость списания платежа по заданному идентификатору. " +
                    "При этом учитываются время последнего списания и период списания, указанный в платеже. " +
                    "В случае необходимости сумма транзакции списывается со счета отправителя на счет получателя.<br>" +
                    "Метод ничего не возвращает.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успех"),
                    @ApiResponse(responseCode = "404", description = "Платеж не найден"),
                    @ApiResponse(responseCode = "500", description = "Ошибка сервера")
            })
    @PostMapping("/write-off-payment/{paymentId}")
    public ResponseEntity<Void> writeOffPayment(@PathVariable final long paymentId)
            throws JournalEntryNotFoundException, PaymentNotFoundException {
        try {
            journalEntryService.writeOffPayment(paymentId);
        } catch (JournalEntryNotFoundException | PaymentNotFoundException e) {
            LOGGER.info("GET /write-off-payment?paymentId={}: {}", paymentId, HttpStatus.NOT_FOUND);
            throw e;
        }
        LOGGER.info("GET /write-off-payment?paymentId={}: {}", paymentId, HttpStatus.OK);
        return ResponseEntity.ok().build();
    }
}
