package si.rso.banksimulator.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import si.rso.banksimulator.dtos.CustomerDTO;
import si.rso.banksimulator.services.CustomerService;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    @Operation(summary = "Returns a list of all customers or query by email")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Customer entities found",
                        content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array =
                                            @ArraySchema(
                                                    schema =
                                                            @Schema(
                                                                    implementation =
                                                                            CustomerDTO.class)))
                        }),
                @ApiResponse(
                        responseCode = "404",
                        description = "Customer entity not found",
                        content = @Content)
            })
    @GetMapping
    public List<CustomerDTO> getAllCustomers(
            @RequestParam(name = "customerEmail", required = false) String email) {
        return email == null
                ? customerService.getAllCustomers()
                : Arrays.asList(customerService.findByEmailDTO(email));
    }

    @Operation(summary = "Returns an entity of customer")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Customer entity found",
                        content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array =
                                            @ArraySchema(
                                                    schema =
                                                            @Schema(
                                                                    implementation =
                                                                            CustomerDTO.class)))
                        }),
                @ApiResponse(
                        responseCode = "404",
                        description = "Customer entity not found",
                        content = @Content)
            })
    @GetMapping("/{customerUuid}")
    public CustomerDTO getByUuid(@PathVariable("customerUuid") UUID uuid) {
        return customerService.findByUuidDTO(uuid);
    }
}
