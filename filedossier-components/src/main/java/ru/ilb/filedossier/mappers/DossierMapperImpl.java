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
package ru.ilb.filedossier.mappers;

import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Named;

import ru.ilb.filedossier.view.DossierFileView;
import ru.ilb.filedossier.view.DossierView;

@Named
public class DossierMapperImpl implements DossierMapper {

    @Inject
    DossierFileMapper dossierFileMapper;

    @Override
    public DossierView fromModel(ru.ilb.filedossier.entities.Dossier model) {
        DossierView view = new DossierView();
        view.setCode(model.getCode());
        view.setName(model.getName());
        view.setValid(String.valueOf(model.isValid()));
        List<DossierFileView> files = model.getDossierFiles().stream()
                .map(mf -> dossierFileMapper.fromModel(mf))
                .collect(Collectors.toList());
        view.setDossierFiles(files);
        return view;
    }

}
