import PropTypes from 'prop-types';
import { Table } from 'semantic-ui-react';
import DossierActions from './DossierActions';

export const getFileLink = ({ dossierKey, dossierPackage, dossierCode, file, inline }) => (
  `https://devel.net.ilb.ru/workflow-web/web/v2/dossiers/${dossierKey}/${dossierPackage}/${dossierCode}/dossierfiles/${file.code}${inline ? '?mode=inline' : ''}`
);

function DossierFile ({ dossierFile, dossierActions, query }) {
  return (
    <Table.Row key={dossierFile.code}>
      <Table.Cell>
        {dossierFile.exists
          ? <a href={getFileLink({ ...query, file: dossierFile })} target="_blank" rel='noreferrer noopener'>{dossierFile.name}</a>
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
  query: PropTypes.object.isRequired,
};

export default DossierFile;
