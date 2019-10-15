import React from 'react';
import PropTypes from 'prop-types';
import { Menu } from 'semantic-ui-react';
import { getFileLink } from '../../Dossier';

function DossierInfo ({ query, dossierFile }) {
  return (<React.Fragment>
    <Menu.Item>Имя файла: {dossierFile.name}</Menu.Item>
    {dossierFile.lastModified && <Menu.Item>Загружен: {dossierFile.lastModified}</Menu.Item>}
    {dossierFile.exists && <Menu.Item as="a" content="Скачать" icon="download"
      href={getFileLink({ ...query, file: dossierFile })} target="_blank"
    />}
  </React.Fragment>);
}

DossierInfo.propTypes = {
  dossierFile: PropTypes.object.isRequired,
  query: PropTypes.object.isRequired,
};

export default DossierInfo;
