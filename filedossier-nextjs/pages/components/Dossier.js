import React, { Suspense, useState, useEffect, useRef } from 'react';
import { Table, Button, Message, Loader } from 'semantic-ui-react';
import { DefaultApi as DossierApi} from '@ilb/filedossier-api/dist';


function Dossier( dossier) {
  console.log('dossier',dossier);

    return (
            <div className="fileDosser">
                   <div>
                    Name: {dossier.name}
                    {dossier.dossierFile &&
                                    <Table celled>
                                        <Table.Header>
                                            <Table.Row>
                                                <Table.HeaderCell>Файл</Table.HeaderCell>
                                                <Table.HeaderCell>Управление</Table.HeaderCell>
                                            </Table.Row>
                                        </Table.Header>

                                        <Table.Body>
                                            {dossier.dossierFile.map(file => (
                                              <DossierFile
                                                  key={file.code}
                                                  file={file}
                                                  link={getDownloadLink(dossier, file.code)}
                                                  />
                                            ))}
                                        </Table.Body>
                                    </Table>
                    }
                </div>
            </div>
            );
}
function getDownloadLink(dossier, fileCode) {
    return 'https://devel.net.ilb.ru/workflow-web/web/v2' + '/dossiers/' + dossier.dossierKey + '/' + dossier.dossierPackage + '/' + dossier.code + '/dossierfiles/' + fileCode;
}


function DossierFile( { file: { code, name,exists }, onChange, link }) {


    return (
            <Table.Row>
                <Table.Cell>
                {exists && <a href={link}>{name}</a>}
                {!exists && name }
                </Table.Cell>
                <Table.Cell>
                </Table.Cell>
            </Table.Row>
            );
}

export default Dossier;
