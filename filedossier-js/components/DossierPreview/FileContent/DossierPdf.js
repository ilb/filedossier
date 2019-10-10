import PropTypes from 'prop-types';

function DossierPdf (props) {
  return (<div>
    <div>DossierImage</div>
    <pre>{JSON.stringify(props, null, 2)}</pre>
  </div>);
}

DossierPdf.propTypes = {
  dossierFile: PropTypes.object.isRequired,
  dossierActions: PropTypes.object.isRequired,
  query: PropTypes.object.isRequired,
};

export default DossierPdf;
