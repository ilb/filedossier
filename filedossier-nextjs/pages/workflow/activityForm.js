import { useRouter, useEffect } from 'next/router';
import Link from 'next/link';
import { ProcessInstancesApi } from '@ilb/workflow-api/dist';
import config from '../../conf/config';
import { Button, Step } from 'semantic-ui-react';
import JsonSchemaForm from '@bb/jsonschema-form';
import '@bb/datetime-picker/lib/index.css';
import '@bb/semantic-ui-css/semantic.min.css'
import superagent from "superagent";
import { DefaultApi as DossierApi} from '@ilb/filedossier-api/dist';

//import '@bb/datetime-picker/lib/index.css';
import Dossier from '../components/Dossier';

// function ActivityForm(activityFormData) {
function ActivityForm(props) {
  const activityFormData = props && props.pageProps;
  console.log('activityFormData', activityFormData);
    const activityInstanceId = activityFormData && activityFormData.activityInstance && activityFormData.activityInstance.id;
    const processInstanceId = activityFormData && activityFormData.activityInstance && activityFormData.activityInstance.processInstanceId;
    console.log('activityInstanceId, processInstanceId', activityInstanceId, processInstanceId);
    const errorHandler = (data) => {
        alert(data.error);
    };
    const submitHandler = async (data) => {
        console.log('submitting', data.formData);
        const res = await superagent.post(process.env.API_PATH + "/activityForm")
                .query({processInstanceId: processInstanceId, activityInstanceId: activityInstanceId})
                .send(data.formData);
        console.log('submitHandler res', res);
        if (res && res.headers) {
          console.log(res.headers['x-location']);
          document.location=res.headers['x-location'].replace(/https:\/\/devel.net.ilb.ru\/workflow-js/,"http://" + document.location.host + "/workflow");
        }
    };

    // TODO возможно лучше в беке добавить ссылку в activityFormData.processStep.href ?
    // document.location.host  ReferenceError: document is not defined
    // activityForm?processInstanceId=5602_stockvaluation_stockvaluation_fairpricecalc&
    // activityInstanceId=8008_5602_stockvaluation_stockvaluation_fairpricecalc_stockvaluation_fairpricecalc_check
      // const url = "http://" + document.location.host + "/workflow/activityForm?processInstanceId=" + processInstanceId + "&activityInstancesId=/";
      const url = "http://" + "localhost:3000" + "/workflow/activityForm?processInstanceId=" + processInstanceId + "&activityInstanceId=";
      activityFormData.processStep.forEach(el => {
        console.log('el.activityId', el.activityId);
        if (el.activityId !== activityInstanceId) {
          el.href = url + el.activityId;
        }
      });

    return <div className="activityForm">
        <Step.Group items={activityFormData.processStep}/>

        <JsonSchemaForm
            schema={activityFormData.jsonSchema}
            formData={activityFormData.formData}
            uiSchema={activityFormData.uiSchema}
            onSubmit={submitHandler}
            >
            <div logg={console.log('activityFormData.activityInstance && activityFormData.activityInstance.state.open', activityFormData.activityInstance && activityFormData.activityInstance.state.open)}>
                { ((activityFormData.activityInstance && activityFormData.activityInstance.state.open) || true) &&
                    <button type="submit" className="ui green button">Выполнить</button>
                }
            </div>
        </JsonSchemaForm>
        { activityFormData.dossierData && <Dossier {...activityFormData.dossierData}/> }


    </div>;
}

ActivityForm.getInitialProps = async function ( {query, headers}) {
    const processInstanceId = query.processInstanceId;
    const activityInstanceId = query.activityInstanceId;
    const api = new ProcessInstancesApi(config.workflowApiClient(headers ? headers['x-remote-user'] : null));
    const data = await api.getActivityForm(activityInstanceId, processInstanceId);
    const apiDossier=new DossierApi(config.dossierApiClient(headers ? headers['x-remote-user'] : null));
    if (data.activityDossier) {
      const {dossierKey, dossierPackage, dossierCode} = data.activityDossier;
      data.dossierData= await apiDossier.getDossier(dossierKey, dossierPackage, dossierCode);
      // TODO добавить в бэк
      data.dossierData.dossierKey=dossierKey;
      data.dossierData.dossierPackage=dossierPackage;
      console.log('data.dossierData',data.dossierData);
    }
    //console.log('data',data);
    return data;
};

export default ActivityForm;
