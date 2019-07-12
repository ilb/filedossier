/*
 * Copyright 2019 slavb.
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
package ru.ilb.filedossier.context.persistence.repositories;

import java.util.Optional;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.ilb.filedossier.context.persistence.model.DossierContextPersistence;

/**
 *
 * @author slavb
 */
public interface DossierContextRepository extends CrudRepository<DossierContextPersistence, Long> {

    @Query("SELECT * FROM DOSSIERCONTEXT WHERE CONTEXTKEY=:contextKey")
    Optional<DossierContextPersistence> findByContextKey(@Param("contextKey") String contextKey);

}
