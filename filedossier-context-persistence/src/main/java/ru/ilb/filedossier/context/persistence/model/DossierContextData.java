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
package ru.ilb.filedossier.context.persistence.model;

import org.springframework.data.annotation.Id;

/**
 *
 * @author kuznetsov_me
 */
public class DossierContextData {
    
    @Id
    private Long Id;
    
    private String dataKey;
    
    private String dataValue;
    
    DossierContextData(String dataKey, String dataValue) {
        this.dataKey = dataKey;
        this.dataValue = dataValue;
    }
    
    public String getDataKey() {
        return dataKey;
    }

    public String getDataValue() {
        return dataValue;
    }
  
}
