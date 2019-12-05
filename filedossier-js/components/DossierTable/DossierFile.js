import PropTypes from 'prop-types';
import { Table } from 'semantic-ui-react';
import DossierActions from './DossierActions';

function DossierFile ({ dossierFile, dossierActions }) {
  return (
    <Table.Row key={dossierFile.code}>
      <Table.Cell>
        {dossierFile.path
          ? <a href={dossierFile.path} target="_blank" rel='noreferrer noopener'>{dossierFile.name}</a>
          : dossierFile.name
        }
      </Table.Cell>
      <Table.Cell style={{ position: 'relative' }}>
        <DossierActions
          dossierFile={dossierFile}
          dossierActions={dossierActions}
        />
      </Table.Cell>
    </Table.Row>
  );
}

DossierFile.propTypes = {
  dossierFile: PropTypes.object.isRequired,
  dossierActions: PropTypes.object.isRequired,
};

export default DossierFile;
