import 'semantic-ui-offline/semantic.min.css';
import PropTypes from 'prop-types';
import FileDossier from '../../classes/FileDossier';
import Dossier from '../../components/Dossier';
import config from '../../conf/config';

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
    dossier: PropTypes.object,
    external: PropTypes.array,
    error: PropTypes.string,
  }).isRequired,
};

DossierApp.getInitialProps = async function (req) {
  await config.init();
  const dossierParams = req.query; // { dossierKey, dossierPackage, dossierCode }
  const xRemoteUser = req && req.headers && req.headers['x-remote-user'];
  const dossier = new FileDossier({ dossierParams, xRemoteUser });
  const dossierData = await dossier.getDossier();
  return { dossierData };
};

export default DossierApp;
