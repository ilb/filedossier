import { useState } from 'react';
import PropTypes from 'prop-types';
import { Menu } from 'semantic-ui-react';
import BystroScan from '../BystroScan';
import FileContent from './FileContent';
import { getFileId, getFileAccept } from '../Dossier';

function DossierPreview ({ dossierParams, dossier, dossierActions }) {
  const dossierFiles = dossier.dossierFile;
  const [selectedFileCode, selectFile] = useState(dossierFiles[0] ? dossierFiles[0].code : null);
  const selectedFile = selectedFileCode && dossierFiles.find(file => file.code === selectedFileCode);

  return (
    <div>
      {dossierFiles.length > 1 && <Menu compact style={{ marginBottom: '1rem' }}
        onItemClick={(event, { name }) => { selectFile(name); }}
        items={dossierFiles.map(dossierFile => ({
          key: dossierFile.code, name: dossierFile.code, content: dossierFile.name, active: selectedFileCode === dossierFile.code,
        }))}
      />}
      {selectedFile && <div>
        {!selectedFile.readonly && <BystroScan
          fileId={getFileId({ ...dossierParams, file: selectedFile })}
          accept={getFileAccept(selectedFile)}
          uploadFile={({ fileId, fileInput, error } = {}) => {
            if (fileId && fileInput && !error) {
              dossierActions.publish({ fileCode: selectedFile.code, file: fileInput.files[0] });
            }
          }}
        />}

        {!selectedFile.readonly && selectedFile.exists && <div className="ui divider"/>}

        {selectedFile.exists && <div>
          <FileContent
            dossierFile={selectedFile}
            dossierActions={dossierActions}
            dossierParams={dossierParams}
          />
        </div>}
      </div>}
    </div>
  );
}

DossierPreview.propTypes = {
  dossierParams: PropTypes.object.isRequired,
  dossier: PropTypes.object.isRequired,
  dossierActions: PropTypes.object.isRequired,
};

export default DossierPreview;
