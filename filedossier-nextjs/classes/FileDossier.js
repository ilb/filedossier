import '@bb/datetime-picker/lib/index.css';
import '@bb/semantic-ui-css/semantic.min.css';
import { useState } from 'react';
import DossierResourceProvider from './DossierResourceProvider';

export default class FileDossier {
  constructor ({ dossierKey, dossierPackage, dossierCode } = {}) {
    this.dossierKey = dossierKey;
    this.dossierPackage = dossierPackage;
    this.dossierCode = dossierCode;
    this.api = new DossierResourceProvider();
  }

  getDossier = async function () {
    const query = { method: 'getDossier', dossierKey: this.dossierKey, dossierPackage: this.dossierPackage, dossierCode: this.dossierCode };
    const dossierData = { query };
    try {
      dossierData.response = await this.api.execute({ query });
    } catch (e) {
      dossierData.error = parseResponseError(e);
    }
    return dossierData;
  }

  upload = async function ({ fileCode, file }) {
    const query = { method: 'upload', fileCode, dossierKey: this.dossierKey, dossierPackage: this.dossierPackage, dossierCode: this.dossierCode };
    const response = await this.api.execute({ query, file });
    return response;
  }

  _createRequestAction = ({ state, setState, action, withUpdate }) => async (...params) => {
    setState({ ...state, loading: true, error: null });
    const result = {};
    try {
      if (action) {
        result.response = await action(...params);
      }

      if (withUpdate) { // update dossier
        const dossierData = await this.getDossier();
        setState({ ...state, dossierData, loading: false });
      } else {
        setState({ ...state, loading: false });
      }
    } catch (e) {
      result.error = parseResponseError(e);
      setState({ ...state, loading: false, error: result.error });
    }
    return result;
  };

  useDossier (dossierData = {}) {
    const [state, setState] = useState({ dossierData, loading: false, error: null }); // init state
    const dossierActions = {
      updateDossier: this._createRequestAction({ state, setState, withUpdate: true }),
      upload: this._createRequestAction({ state, setState, action: this.upload.bind(this), withUpdate: true }),
      resetHook: () => { setState({ ...state, loading: false, error: null }); },
    };
    return [state, dossierActions];
  }
}

const parseResponseError = (error) => {
  return typeof error === 'string'
    ? error : error.response
      ? error.response.text : error.statusText || error.data || error.message || 'Внутренняя ошибка приложения';
};
