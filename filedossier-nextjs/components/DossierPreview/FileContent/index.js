import PropTypes from 'prop-types';
// import { Table } from 'semantic-ui-react';
import DossierImage from './DossierImage';
import DossierPdf from './DossierPdf';
import DossierUnknown from './DossierUnknown';
import './index.css';

function FileContent (props) {
  const fileType = 'image';

  let ContentComponent;
  switch (fileType) {
    case 'image': ContentComponent = DossierImage; break;
    case 'pdf': ContentComponent = DossierPdf; break;
    default: ContentComponent = DossierUnknown;
  }

  return (<ContentComponent {...props}/>);
}

FileContent.propTypes = {
  dossierFile: PropTypes.object.isRequired,
  dossierActions: PropTypes.object.isRequired,
  query: PropTypes.object.isRequired,
};

export default FileContent;
