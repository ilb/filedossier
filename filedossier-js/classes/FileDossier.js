import '@bb/datetime-picker/lib/index.css';
import '@bb/semantic-ui-css/semantic.min.css';
import { useState } from 'react';
import { createDossierApi } from '../conf/config';
import { createJsProxy } from '@ilb/js-auto-proxy';

export default class FileDossier {
  constructor ({ dossierKey, dossierPackage, dossierCode, req } = {}) {
    this.dossierKey = dossierKey;
    this.dossierPackage = dossierPackage;
    this.dossierCode = dossierCode;

    const apiDossier = createDossierApi((req && req.headers) ? req.headers['x-remote-user'] : null);
    this.api = createJsProxy(apiDossier, 'dossier');
  }

  getDossier = async () => {
    const query = { dossierKey: this.dossierKey, dossierPackage: this.dossierPackage, dossierCode: this.dossierCode };
    const { response, error } = await this.api.getDossier(this.dossierKey, this.dossierPackage, this.dossierCode);
    return { query, response, error };
  }

  upload = async ({ fileCode, file }) => {
    const response = await this.api.publish(file, [fileCode, this.dossierKey, this.dossierPackage, this.dossierCode]);
    return response;
  }

  _createRequestAction = ({ state, setState, action, withUpdate }) => async (...params) => {
    setState({ ...state, loading: true, error: null });
    const result = {};
    if (action) {
      const result = await action(...params);
      if (result.error) {
        setState({ ...state, loading: false, error: result.error });
        return;
      }
    }

    if (withUpdate) { // update dossier
      const dossierData = await this.getDossier();
      setState({ ...state, dossierData, loading: false });
    } else {
      setState({ ...state, loading: false });
    }
    return result;
  };

  useDossier = (dossierData = {}) => {
    const [state, setState] = useState({ dossierData, loading: false, error: null }); // init state
    const dossierActions = {
      updateDossier: this._createRequestAction({ state, setState, withUpdate: true }),
      upload: this._createRequestAction({ state, setState, action: this.upload.bind(this), withUpdate: true }),
      resetHook: () => { setState({ ...state, loading: false, error: null }); },
    };
    return [state, dossierActions];
  }
}
