import { useState } from 'react';
import PropTypes from 'prop-types';
import { Menu } from 'semantic-ui-react';
import BystroScan from '../BystroScan';
import FileContent from './FileContent';
import ExternalDossier from '../ExternalDossier';

function DossierPreview ({ dossier, external, dossierActions, loading, error, previewOffset }) {
  const dossierFiles = dossier.dossierFile.filter(file => !file.hidden); // don't show hidden files
  const [selectedFileCode, selectFile] = useState(dossierFiles[0] ? dossierFiles[0].code : null);
  const selectedFile = selectedFileCode && dossierFiles.find(file => file.code === selectedFileCode);

  return (
    <div>
      {dossierFiles.length > 1 && <Menu compact style={{ marginBottom: '1rem', overflow: 'auto' }}
        onItemClick={(event, { name }) => { selectFile(name); }}
        items={dossierFiles.map(dossierFile => ({
          key: dossierFile.code, name: dossierFile.code, content: dossierFile.name, active: selectedFileCode === dossierFile.code,
        }))}
      />}
      {selectedFile && <div>
        {!selectedFile.readonly && <div style={{ marginBottom: '1rem' }}>
          <BystroScan
            fileId={selectedFile.uniqId}
            accept={selectedFile.accept}
            uploadFile={({ fileId, fileInput, uploadMode, error } = {}) => {
              if (fileId && fileInput && !error) {
                dossierActions.uploadFile({
                  fileCode: selectedFile.code,
                  file: fileInput.files[0],
                  update: uploadMode === 'merge',
                });
              }
            }}
          />

          {external && <ExternalDossier
            external={external}
            dossierFile={selectedFile}
            dossierActions={dossierActions}
            loading={loading}
            error={error}
          />}
        </div>}

        {selectedFile.exists && <FileContent
          dossierFile={selectedFile}
          previewOffset={previewOffset}
        />}
      </div>}
    </div>
  );
}

DossierPreview.propTypes = {
  dossier: PropTypes.object.isRequired,
  external: PropTypes.array,
  dossierActions: PropTypes.object.isRequired,
  loading: PropTypes.bool,
  error: PropTypes.string,
  previewOffset: PropTypes.number.isRequired,
};

export default DossierPreview;
