package com.Inkomoko.Integration.controller;

import com.Inkomoko.Integration.producer.CrmProducer;
import com.Inkomoko.Integration.producer.InventoryProducer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/producers")
@Tag(name = "Producer API", description = "Operations to trigger data producers")
public class ProducerController {

    private final CrmProducer crmProducer;
    private final InventoryProducer inventoryProducer;

    public ProducerController(CrmProducer crmProducer, InventoryProducer inventoryProducer) {
        this.crmProducer = crmProducer;
        this.inventoryProducer = inventoryProducer;
    }

    @PostMapping("/crm")
    @Operation(summary = "Trigger CRM producer", description = "Manually triggers the CRM producer to fetch and publish customer data")
    public String triggerCrmProducer() {
        crmProducer.produceData();
        return "CRM producer triggered successfully";
    }

    @PostMapping("/inventory")
    @Operation(summary = "Trigger Inventory producer", description = "Manually triggers the Inventory producer to fetch and publish product data")
    public String triggerInventoryProducer() {
        inventoryProducer.produceData();
        return "Inventory producer triggered successfully";
    }

    @PostMapping("/all")
    @Operation(summary = "Trigger all producers", description = "Manually triggers all producers to fetch and publish data")
    public String triggerAllProducers() {
        crmProducer.produceData();
        inventoryProducer.produceData();
        return "All producers triggered successfully";
    }
}