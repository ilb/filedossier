import PropTypes from 'prop-types';
import { Table } from 'semantic-ui-react';
import DossierFile from './DossierFile';

function DossierTable ({ dossierParams, dossier, dossierActions }) {
  const dossierFiles = dossier.dossierFile;

  return (
    <Table celled>
      <Table.Header>
        <Table.Row>
          <Table.HeaderCell>Файл</Table.HeaderCell>
          <Table.HeaderCell>Управление</Table.HeaderCell>
        </Table.Row>
      </Table.Header>
      <Table.Body>
        {dossierFiles.map(dossierFile => (
          <DossierFile key={dossierFile.code}
            dossierFile={dossierFile}
            dossierActions={dossierActions}
            dossierParams={dossierParams}
          />
        ))}
      </Table.Body>
    </Table>
  );
}

DossierTable.propTypes = {
  dossierParams: PropTypes.object.isRequired,
  dossier: PropTypes.object.isRequired,
  dossierActions: PropTypes.object.isRequired,
};

export default DossierTable;
