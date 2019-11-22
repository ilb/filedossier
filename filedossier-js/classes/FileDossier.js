import { useState } from 'react';
import { createDossierApi } from '../conf/config';
import { createJsProxy } from '@ilb/js-auto-proxy';

export default class FileDossier {
  constructor ({ dossierKey, dossierPackage, dossierCode, dossierMode, req } = {}) {
    this.dossierParams = { dossierKey, dossierPackage, dossierCode, dossierMode };

    const apiDossier = createDossierApi((req && req.headers) ? req.headers['x-remote-user'] : null);
    this.api = createJsProxy(apiDossier, 'dossier');
  }

  /* return array of dossier params */
  getDossierParams = () => {
    const keysOrder = ['dossierKey', 'dossierPackage', 'dossierCode', 'dossierMode'];
    const dossierParamsArray = keysOrder.map(key => this.dossierParams[key]);
    return dossierParamsArray;
  };

  getDossier = async () => {
    const { response, error } = await this.api.getDossier(...this.getDossierParams());
    return { dossierParams: this.dossierParams, response, error };
  }

  // download file
  download = async ({ fileCode, version, mode }) => {
    const response = await this.api.download(fileCode, ...this.getDossierParams(), { version, mode });
    return response;
  }

  /* создает новую версию файла (перезаписывает текущий файл) */
  publish = async ({ fileCode, file }) => {
    const response = await this.api.publish(file, [fileCode, ...this.getDossierParams()]);
    return response;
  }

  /* сохраняет файл в текущую версию (добавляет файл?) */
  update = async ({ fileCode, file }) => {
    const response = await this.api.update(file, [fileCode, ...this.getDossierParams()]);
    return response;
  }

  /**
   * Создает асинхронный метод-обертку для вызова сервисов с последующим обновлением данных досье
   * соответственно управляет переменными ззапросов loading и error
   * @param {object} state - request state from useDossier
   * @param {function} setState - function to update state
   * @param {function} action - async function with some request action
   * @param {boolean} withUpdate - flag of necessity to call getDossier after action
   */
  _createRequestAction = ({ state, setState, action, withUpdate }) => async (...params) => {
    setState({ ...state, loading: true, error: null });
    const result = {};
    if (action) {
      const result = await action(...params);
      if (result.error) {
        setState({ ...state, loading: false, error: result.error });
        return result;
      }
    }

    if (withUpdate) { // update dossier
      const dossierData = await this.getDossier();
      setState({ ...state, dossierData, loading: false, error: null });
    } else {
      setState({ ...state, loading: false, error: null });
    }
    return result;
  };

  useDossier = (dossierData = {}) => {
    const [state, setState] = useState({ dossierData, loading: false, error: null }); // init state
    const dossierActions = {
      updateDossier: this._createRequestAction({ state, setState, withUpdate: true }),
      publish: this._createRequestAction({ state, setState, action: this.publish.bind(this), withUpdate: true }),
      update: this._createRequestAction({ state, setState, action: this.update.bind(this), withUpdate: true }),
      resetHook: () => { setState({ ...state, loading: false, error: null }); },
    };
    return [state, dossierActions];
  }
}
