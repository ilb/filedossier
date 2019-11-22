import '@bb/semantic-ui-css/semantic.min.css';
import PropTypes from 'prop-types';
import FileDossier from '../../classes/FileDossier';
import Dossier from '../../components/Dossier';

function DossierApp ({ dossierData }) {
  /*
   * IMPORTANT: dossierData here - data loaded from ssr
   * use it ONLY to initialise dossier components,
   * because it's gonna mutate later INSIDE CHILDRENS but NOT HERE (eg on file upload, remove, etc)
   */
  return (
    <div className="ui segment" style={{ width: '50%' }}>
      <Dossier dossierData={dossierData} mode="preview"/>
    </div>
  );
}

DossierApp.propTypes = {
  dossierData: PropTypes.shape({
    dossierParams: PropTypes.object.isRequired,
    response: PropTypes.object,
    error: PropTypes.string,
  }).isRequired,
};

DossierApp.getInitialProps = async function ({ query }) {
  const dossier = new FileDossier(query); // { dossierKey, dossierPackage, dossierCode }
  const dossierData = await dossier.getDossier();
  return { dossierData };
};

export default DossierApp;
