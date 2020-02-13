import { useState } from 'react';
import { createJsProxy } from '@ilb/js-auto-proxy';
import { createDossierApi, getProxyApiClient } from '../conf/config';

export default class FileDossier {
  constructor ({ dossierParams, xRemoteUser } = {}) {
    this.dossierParams = dossierParams; // { dossierKey, dossierPackage, dossierCode, dossierMode };

    const apiDossier = createDossierApi(xRemoteUser);
    this.apiDossier = createJsProxy(apiDossier, 'proxies/dossier');

    const proxyApiClient = getProxyApiClient(xRemoteUser);
    this.proxyApiClient = createJsProxy(proxyApiClient, 'proxies/proxy');
  }

  /* return array of dossier params */
  getDossierParams = () => {
    const keysOrder = ['dossierKey', 'dossierPackage', 'dossierCode', 'dossierMode'];
    const dossierParamsArray = keysOrder.map(key => this.dossierParams[key]);
    return dossierParamsArray;
  };

  getFileType = (file) => {
    if (file.mediaType) {
      if (file.mediaType.indexOf('image/') === 0) {
        return 'image';
      } else if (file.mediaType === 'application/pdf') {
        return 'pdf';
      }
    }
  };

  getFileLink = ({ file, inline }) => {
    if (file.exists && file.link) {
      const link = file.link.find(l => l.rel === inline ? 'inline' : 'attachment');
      let href = link && link.href;
      if (href) {
        const separator = href.indexOf('?') !== -1 ? '&' : '?';
        href += `${separator}_nocache=${(file.lastModified || '').replace(/\D/g, '')}`;
      }

      return href;
    }
  };

  getFileAccept = (file) => {
    if (file && file.allowedMediaTypes) {
      return file.allowedMediaTypes.join(',');
    }
  };

  /** creates uniq string based on dossierFile and dossierParams */
  getFileUniqId = (file) => {
    const { dossierKey, dossierPackage, dossierCode } = this.dossierParams;
    return `file_${dossierKey}_${dossierPackage}_${dossierCode}_${file.code}`;
  };

  prepareDossier = (dossier) => {
    if (dossier.dossierFile && dossier.dossierFile.length) {
      dossier.dossierFile = dossier.dossierFile.map(file => ({
        ...file,
        type: this.getFileType(file),
        path: this.getFileLink({ file, inline: false }),
        inlinePath: this.getFileLink({ file, inline: true }),
        accept: this.getFileAccept(file),
        uniqId: this.getFileUniqId(file),
        canSaveRotation: true,
      }));
    }
    return dossier;
  };

  getFileTypeByExt = (ext) => {
    if (ext) {
      if (['jpeg', 'jpg', 'jpe', 'png'].indexOf(ext.toLowerCase()) !== -1) {
        return 'image';
      } else if (ext === 'pdf') {
        return 'pdf';
      }
    }
  };

  parseExternalFile = (file) => ({
    ...file,
    type: this.getFileTypeByExt(file.ext),
    path: file.path + '&contentDisposition=attachment',
    lastModified: file.date_create,
    inlinePath: file.path + '&contentDisposition=inline',
  });

  /* Получение данных досье */
  getDossier = async () => {
    var { response: dossier, error } = await this.apiDossier.getDossier(...this.getDossierParams());
    if (dossier) { dossier = this.prepareDossier(dossier); }
    let result = { dossierParams: this.dossierParams, dossier, error };

    if (!error && this.dossierParams.externalDossier) {
      var { response: external, error: externalError } = await this.proxyApiClient.get(this.dossierParams.externalDossier);
      if (external && external.length) { external = external.map(this.parseExternalFile); }
      result = { ...result, external, externalError };
    }

    return result;
  }

  /* Сохранение угла поворота файла */
  saveFileRotation = async ({ file, angle }) => {
    if (file.canSaveRotation) {
      await new Promise(resolve => setTimeout(resolve, 1000, angle)); // TODO это заглушка, убрать
      // const result = await this.apiDossier.saveFileRotation(...this.getDossierParams(), file.code, angle);
      // return result;
    }
  };

  // download file
  download = async ({ fileCode, version, mode }) => {
    const response = await this.apiDossier.download(fileCode, ...this.getDossierParams(), { version, mode });
    return response;
  }

  getUploadMethod = (merge) => {
    let uploadMethod = merge ? 'update' : 'publish';
    if (!process.browser) { uploadMethod += '1'; }
    return uploadMethod;
  };

  /** Загрyзка файла
   * @param {string} fileCode - dossier file code
   * @param {array{File}} file - a file to upload
   * @param {File} files - array of files to upload and merge
   * @param {bolean} update - if true file will be merged with existed
   */
  uploadFile = async ({ fileCode, file, files, update }) => {
    const filesToUpload = files || [file];
    if (!filesToUpload || !filesToUpload.length) {
      return { error: 'Не переданы файлы для загрузки' };
    }
    let response;
    for (let i = 0; i < filesToUpload.length; i++) {
      const merge = update || i !== 0;
      response = await this[this.getUploadMethod(merge)]({ fileCode, file: filesToUpload[i] });
      if (response && response.error) { return response; }
    }
    return response;
  }

  /* создает новую версию файла */
  publish = async ({ fileCode, file }) => {
    const response = await this.apiDossier.publish(file, [fileCode, ...this.getDossierParams()]);
    return response;
  }

  /* создает новую версию файла (для использования на серверной стороне, без изменения порядка аргyментов) */
  publish1 = async ({ fileCode, file }) => {
    const response = await this.apiDossier.publish(fileCode, ...this.getDossierParams(), { file });
    return response;
  }

  /* сохраняет файл в текущую версию */
  update = async ({ fileCode, file }) => {
    const response = await this.apiDossier.update(file, [fileCode, ...this.getDossierParams()]);
    return response;
  }

  /* сохраняет файл в текущую версию */
  update1 = async ({ fileCode, file }) => {
    const response = await this.apiDossier.update(fileCode, ...this.getDossierParams(), { file });
    return response;
  }

  /* import file/files from url/urls */
  importFile = async ({ fileCode, url, urls, update }) => {
    const fileUrls = urls || [url];
    if (!fileUrls || !fileUrls.length) {
      return { error: 'Не переданы файлы для загрузки' };
    }

    let importResult;
    for (let i = 0; i < fileUrls.length; i++) {
      /* Here we gonna convert file to base64 (on download) and back to buffer (before publish) */
      const fileResult = await this.proxyApiClient.get(encodeURI(fileUrls[i]), { accept: '*/*', returnType: 'Base64' });
      if (fileResult.error || !fileResult.response) {
        return fileResult;
      }

      const file = new Buffer(fileResult.response, 'base64');
      const merge = update || i !== 0;
      importResult = await this[this.getUploadMethod(merge)]({ fileCode, file });
      if (importResult && importResult.error) { return importResult; }
    }
    return importResult;
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
      uploadFile: this._createRequestAction({ state, setState, action: this.uploadFile.bind(this), withUpdate: true }),
      importFile: this._createRequestAction({ state, setState, action: this.importFile.bind(this), withUpdate: true }),
      saveFileRotation: this.saveFileRotation.bind(this),
      resetHook: () => { setState({ ...state, loading: false, error: null }); },
    };
    return [state, dossierActions];
  }
}
