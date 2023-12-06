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
import si.rso.banksimulator.dtos.ContactDTO;
import si.rso.banksimulator.services.ContactService;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/v1/contacts")
public class ContactController {

    private final ContactService contactService;

    @Operation(summary = "Returns a list of all contacts or query by name")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Contact entities found",
                        content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array =
                                            @ArraySchema(
                                                    schema =
                                                            @Schema(
                                                                    implementation =
                                                                            ContactDTO.class)))
                        }),
                @ApiResponse(
                        responseCode = "404",
                        description = "Contact entity not found",
                        content = @Content)
            })
    @GetMapping
    public List<ContactDTO> getAllContacts(
            @RequestParam(name = "contactName", required = false) String name) {
        return name == null
                ? contactService.getAllContacts()
                : contactService.getContactByNameContaining(name);
    }

    @Operation(summary = "Returns an entity of contact")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Contact entity found",
                        content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array =
                                            @ArraySchema(
                                                    schema =
                                                            @Schema(
                                                                    implementation =
                                                                            ContactDTO.class)))
                        }),
                @ApiResponse(
                        responseCode = "404",
                        description = "Contact entity not found",
                        content = @Content)
            })
    @GetMapping("/{contactUuid}")
    public ContactDTO getByUuid(@PathVariable("contactUuid") UUID uuid) {
        return contactService.findByUuid(uuid);
    }
}
