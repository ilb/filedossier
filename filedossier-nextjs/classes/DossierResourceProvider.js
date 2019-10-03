import { DossiersApi } from '@ilb/filedossier-api';
import DossierResourceServer from './DossierResourceServer';
import DossierResourceClient from './DossierResourceClient';
import config from '../conf/config';

const parseResponseError = (error) => {
  return typeof error === 'string'
    ? error : error.response
      ? error.response.text : error.statusText || error.data || error.message || 'Внутренняя ошибка приложения';
};

/**
* Dossier API provider
*/
export default class DossierResourceProvider {
  constructor (req) {
    const apiClient = config.dossierApiClient((req && req.headers) ? req.headers['x-remote-user'] : null);
    const apiDossier = new DossiersApi(apiClient);
    const DossierResource = process.browser ? DossierResourceClient : DossierResourceServer;
    this.resource = new DossierResource(apiDossier);
  }

  async execute (req) {
    const { method } = req.query;
    return this.resource[method](req);
  }

  /* execute api from pages/api/  */
  async executeApi (req, res) {
    const { method } = req.query;
    if (!method || !this.resource[method]) {
      res.status(500).end(`Not acceptable {query.method} = ${req.query.method}`);
    } else {
      try {
        const response = await this.resource[method](req);
        res.status(200).json(response || {});
      } catch (e) {
        const error = parseResponseError(e);
        res.status(500).end(error);
      }
    }
  }
}
