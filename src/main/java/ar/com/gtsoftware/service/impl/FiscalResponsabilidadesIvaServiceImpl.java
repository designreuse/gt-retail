/*
 * Copyright 2018 GT Software.
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

package ar.com.gtsoftware.service.impl;

import ar.com.gtsoftware.dao.FiscalResponsabilidadesIvaFacade;
import ar.com.gtsoftware.domain.FiscalResponsabilidadesIva;
import ar.com.gtsoftware.dto.domain.FiscalResponsabilidadesIvaDto;
import ar.com.gtsoftware.mappers.FiscalResponsabilidadesIvaMapper;
import ar.com.gtsoftware.search.AbstractSearchFilter;
import ar.com.gtsoftware.service.BaseEntityService;
import ar.com.gtsoftware.service.FiscalResponsabilidadesIvaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FiscalResponsabilidadesIvaServiceImpl
        extends BaseEntityService<FiscalResponsabilidadesIvaDto, AbstractSearchFilter,
        FiscalResponsabilidadesIva>
        implements FiscalResponsabilidadesIvaService {

    private final FiscalResponsabilidadesIvaFacade facade;

    private final FiscalResponsabilidadesIvaMapper mapper;

    @Override
    protected FiscalResponsabilidadesIvaFacade getFacade() {
        return facade;
    }

    @Override
    protected FiscalResponsabilidadesIvaMapper getMapper() {
        return mapper;
    }
}
