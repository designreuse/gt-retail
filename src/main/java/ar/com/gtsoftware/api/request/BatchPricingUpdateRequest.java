package ar.com.gtsoftware.api.request;

import ar.com.gtsoftware.search.ProductosSearchFilter;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
public class BatchPricingUpdateRequest {

    @NotNull
    private final ProductosSearchFilter searchFilter;
    private final BigDecimal costUpdatePercent;
    private final List<ProductPercent> percentsToDelete;
    private final List<ProductPercent> percentsToAdd;
}
