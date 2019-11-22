import PropTypes from 'prop-types';
import { Table } from 'semantic-ui-react';
import DossierActions from './DossierActions';
import { getFileLink } from '../Dossier';

function DossierFile ({ dossierFile, dossierActions, dossierParams }) {
  return (
    <Table.Row key={dossierFile.code}>
      <Table.Cell>
        {dossierFile.exists
          ? <a href={getFileLink({ ...dossierParams, file: dossierFile })} target="_blank" rel='noreferrer noopener'>{dossierFile.name}</a>
          : dossierFile.name
        }
      </Table.Cell>
      <Table.Cell style={{ position: 'relative' }}>
        <DossierActions
          dossierFile={dossierFile}
          dossierActions={dossierActions}
          dossierParams={dossierParams}
        />
      </Table.Cell>
    </Table.Row>
  );
}

DossierFile.propTypes = {
  dossierFile: PropTypes.object.isRequired,
  dossierActions: PropTypes.object.isRequired,
  dossierParams: PropTypes.object.isRequired,
};

export default DossierFile;
