import { DossiersApi, ApiClient as DossierApiClient } from '@ilb/filedossier-api';
import { ApiClient } from '@ilb/js-auto-proxy';

const config = {};
function fillparams() {
  if(!config.paramsloaded) {
    config.certfile = process.env['ru.bystrobank.apps.workflow.certfile'];
    config.passphrase = process.env['ru.bystrobank.apps.workflow.cert_PASSWORD'];
    const fs = require('fs');
    config.cert = config.certfile !== null ? fs.readFileSync(config.certfile) : null;
    config.ca = process.env.NODE_EXTRA_CA_CERTS ? fs.readFileSync(process.env.NODE_EXTRA_CA_CERTS) : null;
    config.paramsloaded = true;
  }
}

/**
* Applies authentication headers to the request.
* @param {Object} request The request object created by a <code>superagent()</code> call.
* @param req {Object} nextjs req object.
*/
const applyAuthToRequest = ({ request, xRemoteUser }) => {
  if (!process.browser) {
    fillparams();
    request
      .ca(config.ca)
      .key(config.cert)
      .cert(config.cert)
      .pfx({ passphrase: config.passphrase })
      .set('x-remote-user', xRemoteUser || process.env.USER);
  }
};

export function createDossierApi (xRemoteUser) {
  const apiClient = new DossierApiClient();
  apiClient.basePath = 'https://devel.net.ilb.ru/workflow-web/web/v2';
  apiClient.applyAuthToRequest = (request) => {
    applyAuthToRequest({ request, xRemoteUser });
  };

  const apiDossier = new DossiersApi(apiClient);
  return apiDossier;
}

export function getProxyApiClient (xRemoteUser) {
  const apiClient = new ApiClient();
  apiClient.applyAuthToRequest = (request) => {
    applyAuthToRequest({ request, xRemoteUser });
  };
  return apiClient;
}

export default config;
