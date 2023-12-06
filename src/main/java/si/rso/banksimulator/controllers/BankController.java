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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import si.rso.banksimulator.dtos.BankDTO;
import si.rso.banksimulator.services.BankService;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/v1/banks")
public class BankController {

    private final BankService bankService;

    @Operation(summary = "Returns a list of all banks or query by name")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Bank entities found",
                        content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array =
                                            @ArraySchema(
                                                    schema =
                                                            @Schema(
                                                                    implementation =
                                                                            BankDTO.class)))
                        }),
                @ApiResponse(
                        responseCode = "404",
                        description = "Bank entity not found",
                        content = @Content)
            })
    @GetMapping
    public List<BankDTO> getAllBanks(
            @RequestParam(name = "bankName", required = false) String name) {
        return name == null ? bankService.getAllBanks() : bankService.getBankByNameContaining(name);
    }

    @Operation(summary = "Returns an entity of bank")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Bank entity found",
                        content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array =
                                            @ArraySchema(
                                                    schema =
                                                            @Schema(
                                                                    implementation =
                                                                            BankDTO.class)))
                        }),
                @ApiResponse(
                        responseCode = "404",
                        description = "Bank entity not found",
                        content = @Content)
            })
    @GetMapping("/{bankUuid}")
    public BankDTO getByUuid(@PathVariable("bankUuid") UUID uuid) {
        return bankService.findByUuid(uuid);
    }
}
