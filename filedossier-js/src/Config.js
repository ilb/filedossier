import { ApiClient, DefaultApi } from '@ilb/filedossier-api/dist';

const client = new ApiClient();
client.basePath = '/filedossier-web/web';
export const dossierApi = new DefaultApi(client);