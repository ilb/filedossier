/*
 * Copyright 2019 kuznetsov_me.
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
package ru.ilb.filedossier.context.persistence;

import javax.inject.Named;
import org.springframework.data.relational.core.mapping.NamingStrategy;
import org.springframework.data.relational.core.mapping.RelationalPersistentProperty;
import org.springframework.util.Assert;

/**
 *
 * @author kuznetsov_me
 */
@Named
public class DossierContextNamingStrategy implements NamingStrategy {

    @Override
    public String getReverseColumnName(RelationalPersistentProperty property) {
        return property.getOwner().getTableName() + "_ID";
    }

    @Override
    public String getColumnName(RelationalPersistentProperty property) {
        Assert.notNull(property, "Property must not be null.");
        return property.getName().toUpperCase();
    }

    @Override
    public String getTableName(Class<?> type) {
        Assert.notNull(type, "Type must not be null.");
        return type.getSimpleName().toUpperCase();
    }
}
