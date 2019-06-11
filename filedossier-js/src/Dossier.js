import React, { Suspense, useState, useEffect, useRef } from 'react';
import { Table, Button } from 'semantic-ui-react'



        function useDossier(props) {
            const [dossier, setDossier] = useState(null);
            const {dossierKey, dossierPackage, dossierCode} = props;



            const getDossier = async function () {
                const response = await fetch('/filedossier-web/web/dossiers/' + dossierKey + '/' + dossierPackage + '/' + dossierCode + '.json'); // Uses dossierId prop
                const json = await response.json();
                setDossier(json);
            };

            const setContents = async function (fileCode, formData) {
                const response = await fetch('/filedossier-web/web/dossiers/' + dossierKey + '/' + dossierPackage + '/' + dossierCode + '/dossierfiles/' + fileCode,
                        {
                            method: 'POST',
                            body: formData
                        });
            };

            useEffect(() => {
                getDossier();
            }, [dossierKey, dossierPackage, dossierCode]); // Or [] if effect doesn't need props or state

            return {dossier, resource: {getDossier, setContents}};
        }

function Dossier( { dossierKey, dossierPackage, dossierCode }) {
    const {dossier, resource} = useDossier({dossierKey, dossierPackage, dossierCode})

    return (
            <div className="fileDosser">
                {dossier && <div>
                    Name: {dossier.name}
                    {dossier.dossierFile &&
                                    <Table celled>
                                        <Table.Header>
                                            <Table.Row>
                                                <Table.HeaderCell>Файл</Table.HeaderCell>
                                                <Table.HeaderCell>Действие</Table.HeaderCell>
                                            </Table.Row>
                                        </Table.Header>

                                        <Table.Body>
                                            {dossier.dossierFile.map((file) => <DossierFile file={file} key={file.code} resource={resource}/>)}
                                        </Table.Body>
                                    </Table>
                    }



                </div>}
            </div>
            );



    //return <div>1111</div>;
}

function DossierFile( { file: { code, name }, resource }) {

    const inputFileEl = useRef(null)

    const remove = () => {
        console.log('code', code);
        resource.getDossier();
    };

    const upload = () => {
        console.log('code', code, inputFileEl.current.files);
        const files = inputFileEl.current.files;

        const formData = new FormData();

        for (var i = 0; i < files.length; i++) {
            formData.append(i, files.item(i));
        }

        resource.setContents(code, formData);
        resource.getDossier();
    };


    return <Table.Row>
        <Table.Cell>{name}</Table.Cell>
        <Table.Cell><Button content="Удалить" onClick={remove}/>
            <input ref={inputFileEl} type='file' onChange={upload}  />
        </Table.Cell>
    </Table.Row>;
}

export default Dossier;