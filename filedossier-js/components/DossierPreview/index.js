import { useState } from 'react';
import PropTypes from 'prop-types';
import { Menu } from 'semantic-ui-react';
import BystroScan from '../BystroScan';
import FileContent from './FileContent';

function DossierPreview ({ query, dossier, dossierActions }) {
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
          <FileContent
            dossierFile={selectedFile}
            dossierActions={dossierActions}
            query={query}
          />
        </div>}
      </div>}
    </div>
  );
}

DossierPreview.propTypes = {
  query: PropTypes.object.isRequired,
  dossier: PropTypes.object.isRequired,
  dossierActions: PropTypes.object.isRequired,
};

export default DossierPreview;
