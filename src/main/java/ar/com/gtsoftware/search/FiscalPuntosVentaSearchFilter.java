/*
 * Copyright 2018 GT Software.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ar.com.gtsoftware.search;

import ar.com.gtsoftware.enums.TiposPuntosVenta;
import lombok.*;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FiscalPuntosVentaSearchFilter extends AbstractSearchFilter {

    private Long idSucursal;
    private Boolean activo;
    private Integer nroPuntoVenta;
    private List<TiposPuntosVenta> tiposPuntoVenta;

    @Override
    public boolean hasFilter() {
        return idSucursal != null
                || activo != null
                || nroPuntoVenta != null
                || hasTiposPuntoVentaFilter();
    }

    public boolean hasTiposPuntoVentaFilter() {
        return CollectionUtils.isNotEmpty(tiposPuntoVenta);
    }

}
