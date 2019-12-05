import { executeApi } from '@ilb/js-auto-proxy';
import { getProxyApiClient } from '../../../conf/config';

export default async (req, res) => {
  const xRemoteUser = req && req.headers && req.headers['x-remote-user'];
  const proxyApiClient = getProxyApiClient(xRemoteUser);
  executeApi(proxyApiClient, req, res);
};
