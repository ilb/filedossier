/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


export default class DossierResource {

    constructor(dossierApi, { dossierKey, dossierPackage, dossierCode }) {
        this.dossierApi = dossierApi;
        this.dossierKey = dossierKey;
        this.dossierPackage = dossierPackage;
        this.dossierCode = dossierCode;
    }

    getDossier() {
        return this.dossierApi.getDossier(this.dossierKey, this.dossierPackage, this.dossierCode);
    }

    getDossierFileResource(fileCode) {
        return new DossierFileResource(this.dossierApi, {dossierKey: this.dossierKey, dossierPackage: this.dossierPackage, dossierCode: this.dossierCode, fileCode});
    }

}

export class DossierFileResource {

    constructor(dossierApi, { dossierKey, dossierPackage, dossierCode, fileCode }) {
        this.dossierApi = dossierApi;
        this.dossierKey = dossierKey;
        this.dossierPackage = dossierPackage;
        this.dossierCode = dossierCode;
        this.fileCode = fileCode;
    }

    uploadContents(file) {
        return this.dossierApi.uploadContents(this.fileCode, this.dossierKey, this.dossierPackage, this.dossierCode, {file});
    }

    getDownloadLink() {
        let prefix='';
        //development mode
        if (window.location.host.indexOf('.')===-1) {
            prefix='http://localhost:8080';
        };
        return prefix + this.dossierApi.apiClient.basePath + '/dossiers/' + this.dossierKey + '/' + this.dossierPackage + '/' + this.dossierCode + '/dossierfiles/' + this.fileCode;
    }

}
