import React, { Suspense, useState, useEffect, useRef } from 'react';
import { Table, Button, Message, Loader } from 'semantic-ui-react';
import {dossierApi} from './Config';
import DossierResource from './DossierResource';
import {useResource} from './ReactHelper';

function Dossier( { dossierKey, dossierPackage, dossierCode }) {
    const dossierResource = new DossierResource(dossierApi, {dossierKey, dossierPackage, dossierCode});

    const [dossier, getDossier] = useResource(() => dossierResource.getDossier());

    useEffect(() => {getDossier();} , [dossierKey, dossierPackage, dossierCode]);

    return (
            <div className="fileDosser">
                {dossier.loading && <Loader active /> }
                {dossier.error && <Message error visible content={dossier.error}/> }
                {dossier.value && <div>
                    Name: {dossier.value.name}
                    {dossier.value.dossierFile &&
                                    <Table celled>
                                        <Table.Header>
                                            <Table.Row>
                                                <Table.HeaderCell>Файл</Table.HeaderCell>
                                                <Table.HeaderCell>Действие</Table.HeaderCell>
                                            </Table.Row>
                                        </Table.Header>

                                        <Table.Body>
                                            {dossier.value.dossierFile.map((file) => <DossierFile file={file} key={file.code} onChange={getDossier} resource={dossierResource.getDossierFileResource(file.code)}/>)}
                                        </Table.Body>
                                    </Table>
                    }

                </div>}
            </div>
            );
}

function DossierFile( { file: { code, name }, onChange, resource }) {

    const inputFileEl = useRef(null);

    const remove = () => {
        console.log('code', code);
        onChange();
    };

    const upload = () => {
        //console.log('upload', code, inputFileEl.current.files);
        const files = inputFileEl.current.files;

//        const formData = new FormData();
//
//        for (var i = 0; i < files.length; i++) {
//            formData.append(i, files.item(i));
//        }

        resource.uploadContents(inputFileEl.current.files[0]);
        onChange();
    };


    return <Table.Row>
    <Table.Cell>{name}</Table.Cell>
    <Table.Cell><Button content="Удалить" onClick={remove}/>
        <input ref={inputFileEl} type="file" name="file" onChange={upload}  />
    </Table.Cell>
</Table.Row>;
}

export default Dossier;