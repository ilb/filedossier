import { executeApi } from '@ilb/js-auto-proxy';
import config, { getProxyApiClient } from '../../../conf/config';

export default async (req, res) => {
  await config.init();
  const xRemoteUser = req && req.headers && req.headers['x-remote-user'];
  const proxyApiClient = getProxyApiClient(xRemoteUser);
  executeApi(proxyApiClient, req, res);
};
