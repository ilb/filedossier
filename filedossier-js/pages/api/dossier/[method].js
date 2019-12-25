import { executeApi } from '@ilb/js-auto-proxy';
import config, { createDossierApi } from '../../../conf/config';

export default async (req, res) => {
  await config.init();
  const xRemoteUser = req && req.headers && req.headers['x-remote-user'];
  const apiDossier = createDossierApi(xRemoteUser);
  executeApi(apiDossier, req, res);
};
