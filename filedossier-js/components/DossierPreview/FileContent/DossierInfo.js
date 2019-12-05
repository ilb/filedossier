import React from 'react';
import PropTypes from 'prop-types';
import { Menu } from 'semantic-ui-react';

function DossierInfo ({ dossierFile }) {
  return (<React.Fragment>
    <Menu.Item>Имя файла: {dossierFile.name}</Menu.Item>
    {dossierFile.lastModified && <Menu.Item>Загружен: {dossierFile.lastModified}</Menu.Item>}
    {dossierFile.path && <Menu.Item as="a" content="Скачать" icon="download" href={dossierFile.path} target="_blank"/>}
  </React.Fragment>);
}

DossierInfo.propTypes = {
  dossierFile: PropTypes.object.isRequired,
};

export default DossierInfo;
