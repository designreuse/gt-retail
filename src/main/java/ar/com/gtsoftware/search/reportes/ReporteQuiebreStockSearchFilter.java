/*
 * Copyright 2019 GT Software.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package ar.com.gtsoftware.search.reportes;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReporteQuiebreStockSearchFilter extends AbstractReportSearchFilter {

    private LocalDate fechaDesde, fechaHasta;
    private Long idSucursal;

    @Override
    public boolean hasFilter() {
        return hasFechasFilter()
                || idSucursal != null;
    }

    public boolean hasFechasFilter() {
        return (fechaDesde != null && fechaHasta != null);
    }

}