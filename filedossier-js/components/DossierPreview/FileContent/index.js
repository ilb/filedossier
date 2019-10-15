import { useRef, useState } from 'react';
import PropTypes from 'prop-types';
// import { Table } from 'semantic-ui-react';
import DossierImage from './DossierImage';
import DossierPdf from './DossierPdf';
import DossierInfo from './DossierInfo';
import './index.css';

function FileContent (props) {
  // temporary manual type selection
  const [fileType, setFileType] = useState('image');
  const contentRef = useRef(null);

  let ContentComponent;
  switch (fileType) {
    case 'image': ContentComponent = DossierImage; break;
    case 'pdf': ContentComponent = DossierPdf; break;
    default: ContentComponent = DossierInfo;
  }

  return (
    <div className="file-content" style={{ height: 'calc(100vh - 11rem)' }}>
      <select onChange={(e) => { setFileType(e.currentTarget.value); }} style={{ position: 'absolute', top: '3rem' }}>
        <option value="image">Картинка</option>
        <option value="pdf">PDF файл</option>
        <option value="other">Другое (ссылка на файл)</option>
      </select>
      <ContentComponent {...props} contentRef={contentRef}/>
    </div>
  );
}

FileContent.propTypes = {
  dossierFile: PropTypes.object.isRequired,
  dossierActions: PropTypes.object.isRequired,
  query: PropTypes.object.isRequired,
};

export default FileContent;
