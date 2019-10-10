import { DossiersApi, ApiClient } from '@ilb/filedossier-api';
require('@ilb/node_context').config({ debug: true });

const config = {};
if (!process.browser) {
  config.certfile = process.env['ru.bystrobank.apps.workflow.certfile'];
  config.passphrase = process.env['ru.bystrobank.apps.workflow.cert_PASSWORD'];
  const fs = require('fs');
  config.cert = config.certfile !== null ? fs.readFileSync(config.certfile) : null;
  config.ca = process.env.NODE_EXTRA_CA_CERTS ? fs.readFileSync(process.env.NODE_EXTRA_CA_CERTS) : null;
}

export function createDossierApi (xRemoteUser) {
  const apiClient = new ApiClient();
  apiClient.basePath = 'https://devel.net.ilb.ru/workflow-web/web/v2';
  if (!process.browser) {
    apiClient.applyAuthToRequest = (request/* , authNames */) => {
      request.ca(config.ca).key(config.cert).cert(config.cert);
      request._passphrase = config.passphrase;
      request.set('x-remote-user', xRemoteUser || process.env.USER);
    };
  }

  const apiDossier = new DossiersApi(apiClient);
  return apiDossier;
}

export default config;
