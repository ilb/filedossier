import { useState } from 'react';
import PropTypes from 'prop-types';
import { Menu } from 'semantic-ui-react';
import BystroScan from '../BystroScan';
import FileContent from './FileContent';
import ExternalDossier from '../ExternalDossier';

function DossierPreview ({ dossier, external, dossierActions, previewOffset, fileCode, readOnly }) {
  const dossierFiles = dossier.dossierFile.filter(file => fileCode ? file.code === fileCode : !file.hidden); // don't show hidden files
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
        {!(selectedFile.readonly || readOnly) && <div style={{ marginBottom: '1rem' }}>
          {external ? <ExternalDossier
            external={external}
            dossierFile={selectedFile}
            importFile={async ({ files, uploadMode }) => {
              const urls = files.map(file => file.path);
              const importResult = await dossierActions.importFile({
                fileCode: selectedFile.code,
                urls,
                update: uploadMode === 'merge',
              });
              return importResult;
            }}
          /> : <BystroScan
            fileId={selectedFile.uniqId}
            accept={selectedFile.accept}
            multiple={selectedFile.allowedMultiple}
            uploadFile={async ({ fileId, fileInput, uploadMode, error } = {}) => {
              if (fileId && fileInput && !error) {
                await dossierActions.uploadFile({
                  fileCode: selectedFile.code,
                  files: fileInput.files,
                  update: uploadMode === 'merge',
                });
              }
            }}
          />}
        </div>}

        {selectedFile.exists &&
          <FileContent
            dossierFile={selectedFile}
            previewOffset={previewOffset}
            dossierActions={dossierActions}
          />
        }
      </div>}
    </div>
  );
}

DossierPreview.propTypes = {
  dossier: PropTypes.object.isRequired,
  external: PropTypes.array,
  dossierActions: PropTypes.object.isRequired,
  previewOffset: PropTypes.number.isRequired,
  fileCode: PropTypes.string,
  readOnly: PropTypes.bool,
};

export default DossierPreview;
