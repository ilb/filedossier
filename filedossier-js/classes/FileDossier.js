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

  /* lock file - set readonly = true */
  lockFile = async ({ fileCode }) => {
    const response = await this.api.lock(fileCode, this.dossierKey, this.dossierPackage, this.dossierCode);
    return response;
  }

  /* hide file - set hidden = true */
  hideFile = async ({ fileCode }) => {
    const response = await this.api.hide(fileCode, this.dossierKey, this.dossierPackage, this.dossierCode);
    return response;
  }

  // download file
  download = async ({ fileCode, version, mode }) => {
    const response = await this.api.download(fileCode, this.dossierKey, this.dossierPackage, this.dossierCode, { version, mode });
    return response;
  }

  /* создает новую версию файла (перезаписывает текущий файл) */
  publish = async ({ fileCode, file }) => {
    const response = await this.api.publish(file, [fileCode, this.dossierKey, this.dossierPackage, this.dossierCode]);
    return response;
  }

  /* сохраняет файл в текущую версию (добавляет файл?) */
  update = async ({ fileCode, file }) => {
    const response = await this.api.update(file, [fileCode, this.dossierKey, this.dossierPackage, this.dossierCode]);
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
        return;
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
      lockFile: this._createRequestAction({ state, setState, action: this.lockFile.bind(this), withUpdate: true }),
      hideFile: this._createRequestAction({ state, setState, action: this.hideFile.bind(this), withUpdate: true }),
      resetHook: () => { setState({ ...state, loading: false, error: null }); },
    };
    return [state, dossierActions];
  }
}
