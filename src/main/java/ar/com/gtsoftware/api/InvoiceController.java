package ar.com.gtsoftware.api;

import ar.com.gtsoftware.api.request.InvoiceRequest;
import ar.com.gtsoftware.api.response.InvoiceResponse;
import ar.com.gtsoftware.api.response.PointOfSale;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

public interface InvoiceController {

    @GetMapping(path = "/points-of-sale")
    List<PointOfSale> getPointsOfSale();

    @PostMapping(path = "/invoice")
    InvoiceResponse invoiceSale(@Valid @RequestBody InvoiceRequest request);
}
