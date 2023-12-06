package si.rso.banksimulator.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import si.rso.banksimulator.dtos.TransactionDTO;
import si.rso.banksimulator.enums.TransactionStatus;
import si.rso.banksimulator.services.TransactionService;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Operation(summary = "Returns a list of all transactions or query by status")
    @ApiResponse(
            responseCode = "200",
            description = "Transaction entities returned",
            content = {
                @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        array =
                                @ArraySchema(
                                        schema = @Schema(implementation = TransactionDTO.class)))
            })
    @GetMapping
    public List<TransactionDTO> getAllTransactions(
            @RequestParam(name = "transactionStatus", required = false)
                    TransactionStatus transactionStatus) {
        return transactionStatus == null
                ? transactionService.getAllTransactions()
                : transactionService.getTransactionByStatus(transactionStatus);
    }

    @Operation(summary = "Returns an entity of transaction")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Transaction entity found",
                        content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array =
                                            @ArraySchema(
                                                    schema =
                                                            @Schema(
                                                                    implementation =
                                                                            TransactionDTO.class)))
                        }),
                @ApiResponse(
                        responseCode = "404",
                        description = "Transaction entity not found",
                        content = @Content)
            })
    @GetMapping("/{transactionUuid}")
    public TransactionDTO getByUuid(@PathVariable("transactionUuid") UUID uuid) {
        return transactionService.findTransactionByUuidDTO(uuid);
    }

    @Operation(summary = "Receives notification from payment network")
    @PutMapping(path = "/{transactionUuid}/complete", produces = MediaType.APPLICATION_JSON_VALUE)
    public void completeTransaction(
            @PathVariable("transactionUuid") UUID uuid,
            @RequestParam(name = "bankBIC") String bankBIC) {
        transactionService.completeTransaction(uuid, bankBIC);
    }

    @Operation(summary = "Finalize completed transaction")
    @PutMapping(path = "/{transactionUuid}/finalizeCompletedTransaction")
    public void finalizeCompletedTransaction(
            @PathVariable("transactionUuid") UUID uuid,
            @RequestParam(name = "bankBIC") String bankBIC) {
        transactionService.finalizeCompletedTransaction(uuid, bankBIC);
    }
}
