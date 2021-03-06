/*
 * Copyright 2017 GT Software.
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
package ar.com.gtsoftware.dao;

import ar.com.gtsoftware.domain.RemitoTipoMovimiento;
import ar.com.gtsoftware.search.AbstractSearchFilter;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Repository
public class RemitoTipoMovimientoFacade extends AbstractFacade<RemitoTipoMovimiento, AbstractSearchFilter> {


    private final EntityManager em;

    public RemitoTipoMovimientoFacade(EntityManager em) {
        super(RemitoTipoMovimiento.class);
        this.em = em;
    }

    @Override
    protected EntityManager getEntityManager() {
        return this.em;
    }

    @Override
    protected Predicate createWhereFromSearchFilter(AbstractSearchFilter sf, CriteriaBuilder cb, Root<RemitoTipoMovimiento> root) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
