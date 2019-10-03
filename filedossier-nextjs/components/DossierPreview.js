import { useState } from 'react';
import PropTypes from 'prop-types';
import { Segment, Message, Header, Menu, Divider } from 'semantic-ui-react';
import BystroScan from './BystroScan';
import FileDossier from '../classes/FileDossier';

function DossierPreview (props) {
  const dossierInst = new FileDossier(props.dossierData.query); // create instance { dossierKey, dossierPackage, dossierCode }
  const [{ dossierData, loading, error }, dossierActions] = dossierInst.useDossier(props.dossierData); // init hook
  const { /* query, */ response: dossier, error: dossierError } = dossierData;
  const dossierFiles = (dossier && dossier.dossierFile) || [];
  const [selectedFileCode, selectFile] = useState(dossierFiles[0] ? dossierFiles[0].code : null);

  return (
    <Segment basic loading={loading} style={{ padding: 0 }}>
      {!!dossierError && <Message error visible header="Ошибка при загрузке досье" content={dossierError}/>}
      {!!error && <Message error visible header="Ошибка при выполнении действия" content={error}/>}
      {dossier && <div>
        <Header dividing content={dossier.name}/>
        {dossierFiles.length > 0 && <Menu compact
          // className={classnames('compact-menu', styles.modeMenu)}
          onItemClick={(event, { name }) => { selectFile(name); }}
          items={dossierFiles.map(dossierFile => ({
            key: dossierFile.code, name: dossierFile.code, content: dossierFile.name, active: selectedFileCode === dossierFile.code,
          }))}
        />}
        {(!dossierFiles || !dossierFiles.length) && <Message error visible header="Отсутствуют файлы в досье"/>}
        {selectedFileCode && <div>
          <Divider/>
          <BystroScan
            fileId={selectedFileCode}
            uploadFile={({ fileId, fileInput, error } = {}) => {
              if (fileId && fileInput && !error) {
                dossierActions.upload({ fileCode: fileId, file: fileInput.files[0] });
              }
            }}
          />
        </div>}
      </div>}
    </Segment>
  );
}

DossierPreview.propTypes = {
  dossierData: PropTypes.shape({
    query: PropTypes.object.isRequired,
    response: PropTypes.object,
    error: PropTypes.string,
  }).isRequired,
};

export default DossierPreview;
