import { useRef } from 'react';
import PropTypes from 'prop-types';
// import { Table } from 'semantic-ui-react';
import DossierImage from './DossierImage';
import DossierPdf from './DossierPdf';
import DossierInfo from './DossierInfo';
import './index.css';

function FileContent (props) {
  const contentRef = useRef(null);
  const { dossierFile } = props;
  let fileType;
  if (dossierFile.mediaType) {
    if (dossierFile.mediaType.indexOf('image/') === 0) {
      fileType = 'image';
    } else if (dossierFile.mediaType === 'application/pdf') {
      fileType = 'pdf';
    }
  }

  let ContentComponent;
  switch (fileType) {
    case 'image': ContentComponent = DossierImage; break;
    case 'pdf': ContentComponent = DossierPdf; break;
    default: ContentComponent = DossierInfo;
  }

  return (
    <div className="file-content" style={{ height: 'calc(100vh - 11rem)' }}>
      <ContentComponent {...props} contentRef={contentRef}/>
    </div>
  );
}

FileContent.propTypes = {
  dossierFile: PropTypes.object.isRequired,
  dossierActions: PropTypes.object.isRequired,
  dossierParams: PropTypes.object.isRequired,
};

export default FileContent;
