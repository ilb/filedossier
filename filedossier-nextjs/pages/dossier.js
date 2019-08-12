import config from '../conf/config';
import '@bb/datetime-picker/lib/index.css';
import '@bb/semantic-ui-css/semantic.min.css'
import { DefaultApi as DossierApi} from '@ilb/filedossier-api/dist';

//import '@bb/datetime-picker/lib/index.css';
import Dossier from './components/Dossier';

function DossierApp(props) {
    const errorHandler = (data) => {
        alert(data.error);
    };

    return <div>
        { props.dossierFile && <Dossier {...props}/> }
    </div>;
}

DossierApp.getInitialProps = async function ( {query, headers}) {
    // использовать ссылку
    // http://localhost:3000/dossier?dossierCode=stockvaluation_fairpricecalc&dossierKey=3701_stockvaluation_stockvaluation_fairpricecalc&dossierPackage=stockvaluation
    const dossierCode = query.dossierCode;
    const dossierKey =query.dossierKey;
    const dossierPackage = query.dossierPackage;
    const apiDossier=new DossierApi(config.dossierApiClient(headers ? headers['x-remote-user'] : null));
    const data = await apiDossier.getDossier(dossierKey, dossierPackage, dossierCode);
    return data;
};

export default DossierApp;
