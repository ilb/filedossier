import { DossiersApi, ApiClient as DossierApiClient } from '@ilb/filedossier-api';
import { ApiClient } from '@ilb/js-auto-proxy';
import ContextFactory from '@ilb/node_context';

const config = {};

config.fillParams = () => {
  if (!process.browser && !config.paramsLoaded) {
    // auth params
    config.certfile = process.env['ru.bystrobank.apps.workflow.certfile'];
    config.passphrase = process.env['ru.bystrobank.apps.workflow.cert_PASSWORD'];
    const fs = require('fs');
    config.cert = config.certfile !== null ? fs.readFileSync(config.certfile) : null;
    config.ca = process.env.NODE_EXTRA_CA_CERTS ? fs.readFileSync(process.env.NODE_EXTRA_CA_CERTS) : null;

    // web services
    config.workflowWS = process.env['ru.bystrobank.apps.workflow.ws'];

    config.paramsLoaded = true;
  }
};

config.init = async () => {
  if (!process.browser && !config.initialized) {
    const context = new ContextFactory();
    await context.build();
    config.fillParams();
    config.initialized = true;
  }
};

/**
* Applies authentication headers to the request.
* @param {Object} request The request object created by a <code>superagent()</code> call.
* @param req {Object} nextjs req object.
*/
export const applyAuthToRequest = ({ request, xRemoteUser }) => {
  if (!process.browser) {
    config.fillParams();
    request
      .ca(config.ca)
      .key(config.cert)
      .cert(config.cert)
      .pfx({ passphrase: config.passphrase })
      .set('x-remote-user', xRemoteUser || process.env.USER);
  }
};

export function createDossierApi (xRemoteUser) {
  config.fillParams();
  const apiClient = new DossierApiClient();
  apiClient.basePath = `${config.workflowWS}/v2`; // IMPORTANT: server side only (or via createJsProxy)
  apiClient.applyAuthToRequest = (request) => {
    applyAuthToRequest({ request, xRemoteUser });
  };

  const apiDossier = new DossiersApi(apiClient);
  return apiDossier;
}

export function getProxyApiClient (xRemoteUser) {
  config.fillParams();
  const apiClient = new ApiClient();
  apiClient.applyAuthToRequest = (request) => {
    applyAuthToRequest({ request, xRemoteUser });
  };
  return apiClient;
}

export default config;
