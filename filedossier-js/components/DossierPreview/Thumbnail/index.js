import { useRef } from 'react';
import PropTypes from 'prop-types';
import DossierImage from './DossierImage';
import DossierPdf from './DossierPdf';

function Thumbnail ({ dossierFile, sizes }) {
  const contentRef = useRef(null);

  let ContentComponent;
  switch (dossierFile.type) {
    case 'image': ContentComponent = DossierImage; break;
    case 'pdf': ContentComponent = DossierPdf; break;
    default: ContentComponent = function Invalid () { return <div>Invalid type: {dossierFile.type}</div>; };
  }

  return (
    <div className="thumbnail" style={{ lineHeight: 0 }}>
      <ContentComponent
        dossierFile={dossierFile}
        contentRef={contentRef}
        sizes={sizes}
      />
    </div>
  );
}

Thumbnail.propTypes = {
  dossierFile: PropTypes.object.isRequired,
  sizes: PropTypes.shape({
    width: PropTypes.number.isRequired,
    height: PropTypes.number.isRequired,
  }).isRequired,
};

export default Thumbnail;
