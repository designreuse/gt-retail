/*
 * Copyright 2014 GT Software.
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

import ar.com.gtsoftware.domain.PersonasTelefonos;
import ar.com.gtsoftware.domain.PersonasTelefonos_;
import ar.com.gtsoftware.domain.Personas_;
import ar.com.gtsoftware.search.PersonasTelefonosSearchFilter;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Repository
public class PersonasTelefonosFacade extends AbstractFacade<PersonasTelefonos, PersonasTelefonosSearchFilter> {


    private final EntityManager em;

    public PersonasTelefonosFacade(EntityManager em) {
        super(PersonasTelefonos.class);
        this.em = em;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    protected Predicate createWhereFromSearchFilter(PersonasTelefonosSearchFilter psf, CriteriaBuilder cb, Root<PersonasTelefonos> root) {

        Predicate p = null;
        if (psf.getIdPersona() != null) {
            p = cb.equal(root.get(PersonasTelefonos_.idPersona).get(Personas_.id), psf.getIdPersona());
        }

        return p;
    }

}
