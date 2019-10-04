import '@bb/semantic-ui-css/semantic.min.css';
import PropTypes from 'prop-types';
import FileDossier from '../../classes/FileDossier';
import DossierTable from '../../components/DossierTable';

function DossierApp ({ dossierData }) {
  /*
   * IMPORTANT: dossierData here - data loaded from ssr
   * use it ONLY to initialise dossier components,
   * because it's gonna mutate later INSIDE CHILDRENS but NOT HERE (eg on file upload, remove, etc)
   */
  return (
    <div className="ui segment" style={{ margin: '1rem' }}>
      <DossierTable dossierData={dossierData}/>
    </div>
  );
}

DossierApp.propTypes = {
  dossierData: PropTypes.shape({
    query: PropTypes.object.isRequired,
    response: PropTypes.object,
    error: PropTypes.string,
  }).isRequired,
};

DossierApp.getInitialProps = async function ({ query, req }) {
  const dossier = new FileDossier({ ...query, req }); // { dossierKey, dossierPackage, dossierCode, req }
  const dossierData = await dossier.getDossier();
  return { dossierData };
};

export default DossierApp;
