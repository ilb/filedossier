import { useState } from 'react';
import PropTypes from 'prop-types';
import { Segment, Message, Menu, Image } from 'semantic-ui-react';
import BystroScan from './BystroScan';
import FileDossier from '../classes/FileDossier';
import { getFileLink } from './DossierTable/DossierFile';

function DossierPreview (props) {
  const dossierInst = new FileDossier(props.dossierData.query); // create instance { dossierKey, dossierPackage, dossierCode }
  const [{ dossierData, loading, error }, dossierActions] = dossierInst.useDossier(props.dossierData); // init hook
  const { query, response: dossier, error: dossierError } = dossierData;
  const dossierFiles = (dossier && dossier.dossierFile) || [];
  const [selectedFileCode, selectFile] = useState(dossierFiles[0] ? dossierFiles[0].code : null);
  const selectedFile = selectedFileCode && dossierFiles.find(file => file.code === selectedFileCode);

  return (
    <Segment basic loading={loading} style={{ padding: 0 }}>
      {!!dossierError && <Message error visible header="Ошибка при загрузке досье" content={dossierError}/>}
      {!!error && <Message error visible header="Ошибка при выполнении действия" content={error}/>}
      {dossier && <div>
        {/* <Header dividing content={dossier.name}/> */}
        {(!dossierFiles || !dossierFiles.length) && <Message error visible header="Отсутствуют файлы в досье"/>}
        {dossierFiles.length > 1 && <Menu compact style={{ marginBottom: '1rem' }}
          onItemClick={(event, { name }) => { selectFile(name); }}
          items={dossierFiles.map(dossierFile => ({
            key: dossierFile.code, name: dossierFile.code, content: dossierFile.name, active: selectedFileCode === dossierFile.code,
          }))}
        />}
        {selectedFileCode && selectedFile && <div>
          {!selectedFile.readonly && <BystroScan
            fileId={selectedFileCode}
            uploadFile={({ fileId, fileInput, error } = {}) => {
              if (fileId && fileInput && !error) {
                dossierActions.upload({ fileCode: fileId, file: fileInput.files[0] });
              }
            }}
          />}

          {selectedFile.exists && <div>
            <div className="ui divider"/>
            Загруженный файл: <a href={getFileLink({ ...query, file: selectedFile })} target="_blank" rel='noreferrer noopener'>{selectedFile.name}</a>
            <div className="ui divider"/>
            <Image src={getFileLink({ ...query, file: selectedFile, inline: true })} alt="Не удалось отобразить preview файла"/>
            <div className="ui divider"/>
            <pre>{JSON.stringify(query, null, 2)}</pre>
            <div className="ui divider"/>
            <pre>{JSON.stringify(selectedFile, null, 2)}</pre>
          </div>}
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
