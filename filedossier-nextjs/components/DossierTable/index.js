import PropTypes from 'prop-types';
import { Segment, Message, Header, Table } from 'semantic-ui-react';
import DossierFile from './DossierFile';
import FileDossier from '../../classes/FileDossier';

function DossierTable (props) {
  const dossierInst = new FileDossier(props.dossierData.query); // create instance { dossierKey, dossierPackage, dossierCode }
  const [{ dossierData, loading, error }, dossierActions] = dossierInst.useDossier(props.dossierData); // init hook
  const { query, response: dossier, error: dossierError } = dossierData;
  const dossierFiles = (dossier && dossier.dossierFile) || [];

  return (
    <Segment basic loading={loading} style={{ padding: 0 }}>
      {!!dossierError && <Message error visible header="Ошибка при загрузке досье" content={dossierError}/>}
      {!!error && <Message error visible header="Ошибка при выполнении действия" content={error}/>}
      {dossier && <div>
        <Header dividing content={dossier.name}/>
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
                query={query}
              />
            ))}
            {(!dossierFiles || !dossierFiles.length) && <Message error visible header="Отсутствуют файлы в досье"/>}
          </Table.Body>
        </Table>
      </div>}
    </Segment>
  );
}

DossierTable.propTypes = {
  dossierData: PropTypes.shape({
    query: PropTypes.object.isRequired,
    response: PropTypes.object,
    error: PropTypes.string,
  }).isRequired,
};

export default DossierTable;
