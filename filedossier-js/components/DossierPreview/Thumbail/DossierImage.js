import React from 'react';
import PropTypes from 'prop-types';

function ThumbailImage ({ dossierFile, sizes: { width, height } }) {
  return (
    <div className="thumbail-img">
      <img src={dossierFile.inlinePath} className="ui fluid image"
        style={{ maxWidth: `${width}px`, maxHeight: `${height}px` }}
      />
    </div>
  );
}

ThumbailImage.propTypes = {
  dossierFile: PropTypes.object.isRequired,
  sizes: PropTypes.shape({
    width: PropTypes.number.isRequired,
    height: PropTypes.number.isRequired,
  }).isRequired,
};

export default ThumbailImage;
