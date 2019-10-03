import superagent from 'superagent';

/**
* Dossier API handler for client
*/
export default class DossierResourceClient {
  /** Upload file to dossier */
  async getDossier ({ query }) {
    const { dossierKey, dossierPackage, dossierCode } = query;
    const res = await superagent.get(process.env.API_PATH + `/dossier/getDossier`)
      .query({ dossierKey, dossierPackage, dossierCode });
    return res.status === 200 ? JSON.parse(res.text) : res.text;
  }

  /** Upload file to dossier */
  async upload ({ query, file }) {
    const res = await superagent.post(process.env.API_PATH + `/dossier/upload`)
      .set('Content-Type', 'multipart/form-data')
      .query(query)
      .send(file);
    return res.status === 200 ? JSON.parse(res.text) : res.text;
  }
}
