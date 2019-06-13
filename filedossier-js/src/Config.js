import { ApiClient, DefaultApi } from './openapi/src';

const client = new ApiClient();
client.basePath = '/filedossier-web/web';
export const dossierApi = new DefaultApi(client);