package com.Inkomoko.Integration.controller;
import com.Inkomoko.Integration.model.Customer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/customers")
@Tag(name = "Customer API", description = "Operations related to customers")
public class CustomerController {

    private final List<Customer> customers = new ArrayList<>(
            Arrays.asList(
                    new Customer("1", "Alice", "alice@example.com"),
                    new Customer("2", "Bob", "bob@example.com")
            )
    );

    @GetMapping
    @Operation(summary = "Get all customers", description = "Returns a list of all customers")
    public List<Customer> getCustomers() {
        return customers;
    }

    @PostMapping
    @Operation(summary = "Add a customer", description = "Adds a new customer to the system")
    public Customer addCustomer(@RequestBody Customer customer) {
        customers.add(customer);
        return customer;
    }
}
