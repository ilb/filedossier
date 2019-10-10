import { executeApi } from '@ilb/js-auto-proxy';
import { createDossierApi } from '../../../conf/config';

export default async (req, res) => {
  const apiDossier = createDossierApi((req && req.headers) ? req.headers['x-remote-user'] : null);
  executeApi(apiDossier, req, res);
};
