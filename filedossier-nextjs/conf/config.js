const context = require('../utils/context');
const workflow_api = require('@ilb/workflow-api');
const dossier_api = require('@ilb/filedossier-api');


let certfile, passphrase, cert, ca;

if (!process.browser) {
    certfile = context.lookup('ru.bystrobank.apps.workflow.certfile');
    passphrase = context.lookup('ru.bystrobank.apps.workflow.cert_PASSWORD');
    const fs = require('fs');
    cert = certfile !== null ? fs.readFileSync(certfile) : null;
    ca = process.env.NODE_EXTRA_CA_CERTS ? fs.readFileSync(process.env.NODE_EXTRA_CA_CERTS) : null;
}

function workflowApiClient(xRemoteUser) {
    const apiClient = new workflow_api.ApiClient();
    apiClient.basePath = 'https://devel.net.ilb.ru/workflow-web/web/v2';
    if (!process.browser) {
        apiClient.applyAuthToRequest = (request, authNames) => {
            request.ca(ca).key(cert).cert(cert);
            request._passphrase = passphrase;
            request.set('x-remote-user',xRemoteUser || process.env.USER);
        };
    }
    return apiClient;
}

function dossierApiClient(xRemoteUser) {
    const apiClient = new dossier_api.ApiClient();
    apiClient.basePath = 'https://devel.net.ilb.ru/workflow-web/web/v2';
    if (!process.browser) {
        apiClient.applyAuthToRequest = (request, authNames) => {
            request.ca(ca).key(cert).cert(cert);
            request._passphrase = passphrase;
            request.set('x-remote-user',xRemoteUser || process.env.USER);
        };
    }
    return apiClient;
}

workflow_api.ApiClient.instance.basePath = 'https://devel.net.ilb.ru/workflow-web/web/v2';
//dossier_api.ApiClient.instance.basePath = 'https://devel.net.ilb.ru/workflow-web/web/v2';

module.exports = {
    certfile: certfile,
    passphrase: passphrase,
    cert: cert,
    ca: ca,
    workflowApiClient: workflowApiClient,
    dossierApiClient: dossierApiClient
};
