package ar.com.gtsoftware.api;

import io.swagger.annotations.ApiModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@ApiModel(value = "API para obtener impresiones")
public interface PrintController {

    @GetMapping(path = "/downloads/budget")
    void getSaleBudget(@RequestParam Long saleId);

    @GetMapping(path = "/downloads/invoice")
    void getInvoice(@RequestParam Long saleId);

    @GetMapping(path = "/downloads/deliveryNote")
    void getDeliveryNote(@RequestParam Long deliveryNoteId);
}
