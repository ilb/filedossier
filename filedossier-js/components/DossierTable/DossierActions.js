import { useState } from 'react';
import PropTypes from 'prop-types';
import { Segment, Button, Icon } from 'semantic-ui-react';
import BystroScan from '../BystroScan';

function DossierActions ({ dossierFile, dossierActions, query }) {
  if (dossierFile.readonly) { return <div/>; }
  const { dossierKey, dossierPackage, dossierCode } = query;
  const [uploadOpened, setUploadOpen] = useState(null);

  const closeUploadModal = () => {
    setUploadOpen(false);
    document.onclick = null;
  };

  const openUploadModal = (event) => {
    setUploadOpen(true);

    /* hide modal on click outside */
    const modal = event.currentTarget.parentNode.querySelector('.segment');
    document.onclick = (e) => {
      if (!modal.contains(e.target)) {
        closeUploadModal();
      }
    };
  };

  return (
    <div>
      <Button size="small" positive
        content="Загрузить"
        disabled={uploadOpened}
        onClick={openUploadModal}
      />
      <Button negative size="small"
        content="Удалить"
        // onClick={remove}
      />
      <Segment style={{ position: 'absolute', top: -20, zIndex: 1000, display: uploadOpened ? '' : 'none' }}>
        {uploadOpened && <div>
          <BystroScan
            fileId={`${dossierKey}_${dossierPackage}_${dossierCode}_${dossierFile.code}`}
            uploadFile={({ fileId, fileInput, error } = {}) => {
              if (fileId && fileInput && !error) {
                dossierActions.upload({ fileCode: fileId, file: fileInput.files[0] });
                closeUploadModal();
              }
            }}
          />
          <Icon link name="close" onClick={closeUploadModal} style={{ position: 'absolute', top: 0, right: 0 }}/>
        </div>}
      </Segment>
    </div>
  );
}

DossierActions.propTypes = {
  dossierFile: PropTypes.object.isRequired,
  dossierActions: PropTypes.object.isRequired,
  query: PropTypes.object.isRequired,
};

export default DossierActions;
