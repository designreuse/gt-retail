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

import ar.com.gtsoftware.dao.ParametrosFacade;
import ar.com.gtsoftware.dto.domain.ParametrosDto;
import ar.com.gtsoftware.enums.Parametros;
import ar.com.gtsoftware.mappers.ParametrosMapper;
import ar.com.gtsoftware.mappers.helper.CycleAvoidingMappingContext;
import ar.com.gtsoftware.service.ParametrosService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParametrosServiceImpl implements ParametrosService {


    private final ParametrosMapper mapper;

    private final ParametrosFacade facade;

    @Override
    public ParametrosDto createOrEdit(@NotNull ParametrosDto parametrosDto) {
        return mapper.entityToDto(facade.createOrEdit(mapper.dtoToEntity(parametrosDto,
                new CycleAvoidingMappingContext())),
                new CycleAvoidingMappingContext());
    }

    @Override
    public ParametrosDto findParametroByName(@NotNull String nombre) {
        return mapper.entityToDto(facade.findParametroByName(nombre), new CycleAvoidingMappingContext());
    }

    @Override
    public List<ParametrosDto> findParametros(@NotNull String txt) {
        return mapper.entitiesToDtos(facade.findParametros(txt), new CycleAvoidingMappingContext());
    }

    @Override
    public boolean getBooleanParam(Parametros parametro) {
        ar.com.gtsoftware.domain.Parametros parametroEntity = facade.findParametroByName(parametro.getNombreParametro());
        return Boolean.parseBoolean(parametroEntity.getValorParametro());
    }

    @Override
    public Long getLongParam(Parametros parametro) {
        ar.com.gtsoftware.domain.Parametros parametroEntity = facade.findParametroByName(parametro.getNombreParametro());
        return Long.parseLong(parametroEntity.getValorParametro());
    }

    @Override
    public String getStringParam(Parametros parametro) {
        ar.com.gtsoftware.domain.Parametros parametroEntity = facade.findParametroByName(parametro.getNombreParametro());
        return parametroEntity.getValorParametro();
    }
}
