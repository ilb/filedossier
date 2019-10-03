require('@ilb/node_context').config({ debug: true });
const dossierApi = require('@ilb/filedossier-api');

let certfile, passphrase, cert, ca;

if (!process.browser) {
  certfile = process.env['ru.bystrobank.apps.workflow.certfile'];
  passphrase = process.env['ru.bystrobank.apps.workflow.cert_PASSWORD'];
  const fs = require('fs');
  cert = certfile !== null ? fs.readFileSync(certfile) : null;
  ca = process.env.NODE_EXTRA_CA_CERTS ? fs.readFileSync(process.env.NODE_EXTRA_CA_CERTS) : null;
}

function dossierApiClient (xRemoteUser) {
  const apiClient = new dossierApi.ApiClient();
  apiClient.basePath = 'https://devel.net.ilb.ru/workflow-web/web/v2';
  if (!process.browser) {
    apiClient.applyAuthToRequest = (request/* , authNames */) => {
      request.ca(ca).key(cert).cert(cert);
      request._passphrase = passphrase;
      request.set('x-remote-user', xRemoteUser || process.env.USER);
    };
  }
  return apiClient;
}

module.exports = {
  certfile,
  passphrase,
  cert,
  ca,
  dossierApiClient,
};
