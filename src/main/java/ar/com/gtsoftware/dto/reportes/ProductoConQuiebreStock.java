package ar.com.gtsoftware.dto.reportes;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ProductoConQuiebreStock implements Serializable {
    @EqualsAndHashCode.Include
    private String codigoPropio;
    private String descripcion;
    private String unidadVenta;
    private BigDecimal stockMinimo;
    private BigDecimal stockSucursal;
}
