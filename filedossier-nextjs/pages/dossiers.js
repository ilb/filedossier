import { useRouter, useEffect } from 'next/router';
import Link from 'next/link';
import { ProcessInstancesApi } from '@ilb/workflow-api/dist';
import config from '../conf/config';
import { Button, Step } from 'semantic-ui-react';
import JsonSchemaForm from '@bb/jsonschema-form';
import '@bb/datetime-picker/lib/index.css';
import '@bb/semantic-ui-css/semantic.min.css'
import superagent from "superagent";
import { DefaultApi as DossierApi} from '@ilb/filedossier-api/dist';

//import '@bb/datetime-picker/lib/index.css';
import Dossier from './components/Dossier';

function dossier(props) {
  // console.log('dossier props', props);
  const activityFormData = props;
    const errorHandler = (data) => {
        alert(data.error);
    };

    return <div className="activityForm">
        {/* activityFormData.dossierData && <Dossier {...activityFormData.dossierData}/> */}
        { activityFormData.dossierFile && <Dossier {...activityFormData}/> }
    </div>;
}

dossier.getInitialProps = async function ( {query, headers}) {
    // использовать ссылку
    // http://localhost:3000/dossiers?dossierCode=stockvaluation_fairpricecalc&dossierKey=3701_stockvaluation_stockvaluation_fairpricecalc&dossierPackage=stockvaluation
    const dossierCode = query.dossierCode;
    const dossierKey =query.dossierKey;
    const dossierPackage = query.dossierPackage;
    const apiDossier=new DossierApi(config.dossierApiClient(headers ? headers['x-remote-user'] : null));
    const data = await apiDossier.getDossier(dossierKey, dossierPackage, dossierCode);
    return data;
};

export default dossier;
