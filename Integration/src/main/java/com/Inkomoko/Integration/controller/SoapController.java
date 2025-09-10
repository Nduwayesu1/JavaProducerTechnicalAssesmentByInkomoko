package com.Inkomoko.Integration.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/soap")
@Tag(name = "SOAP API", description = "Mock SOAP endpoint")
public class SoapController {

    @PostMapping
    @Operation(summary = "Add customer via SOAP", description = "Returns a mock SOAP response")
    public String addCustomerSoap(@RequestBody String soapRequest) {
        return "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                "<soap:Body><AddCustomerResponse><status>Success</status></AddCustomerResponse></soap:Body></soap:Envelope>";
    }
}
