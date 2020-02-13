import { useRef } from 'react';
import PropTypes from 'prop-types';
import DossierImage from './DossierImage';
import DossierPdf from './DossierPdf';
import DossierInfo from './DossierInfo';
import './index.css';

function FileContent ({ dossierFile, dossierActions, previewOffset }) {
  const contentRef = useRef(null);

  let ContentComponent;
  switch (dossierFile.type) {
    case 'image': ContentComponent = DossierImage; break;
    case 'pdf': ContentComponent = DossierPdf; break;
    default: ContentComponent = DossierInfo;
  }

  return (
    <div className="file-content" style={{ height: `calc(100vh - ${previewOffset}px)` }}>
      <ContentComponent
        dossierFile={dossierFile}
        dossierActions={dossierActions}
        contentRef={contentRef}
      />
    </div>
  );
}

FileContent.propTypes = {
  dossierFile: PropTypes.object.isRequired,
  dossierActions: PropTypes.object.isRequired,
  previewOffset: PropTypes.number.isRequired,
};

export default FileContent;
