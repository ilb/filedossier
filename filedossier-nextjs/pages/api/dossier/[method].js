import DossierResourceProvider from '../../../classes/DossierResourceProvider';

export default async (req, res) => {
  const resource = new DossierResourceProvider(req);
  resource.executeApi(req, res);
};
