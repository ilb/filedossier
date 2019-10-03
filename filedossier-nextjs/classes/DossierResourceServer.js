/**
* Dossier API handler for server
*/
export default class DossierResource {
  constructor (apiDossier) {
    this.apiDossier = apiDossier;
  }

  /** Get dossier data */
  async getDossier ({ query }) {
    const { dossierKey, dossierPackage, dossierCode } = query;
    const dossierData = await this.apiDossier.getDossier(dossierKey, dossierPackage, dossierCode);
    return dossierData;
  }

  /** Upload file to dossier */
  async upload ({ query, body }) {
    const { fileCode, dossierKey, dossierPackage, dossierCode } = query;
    await this.apiDossier.publish(fileCode, dossierKey, dossierPackage, dossierCode, { file: body });
    return { status: 'OK' };
  }
}
