import React from 'react';
import PropTypes from 'prop-types';
import { Header, Segment, Message } from 'semantic-ui-react';
import FileDossier from '../classes/FileDossier';
import DossierPreview from './DossierPreview';
import DossierTable from './DossierTable';

export const getFileLink = ({ dossierKey, dossierPackage, dossierCode, file, inline }) => (
  `https://devel.net.ilb.ru/workflow-web/web/v2/` +
    `dossiers/${dossierKey}/${dossierPackage}/${dossierCode}/dossierfiles/${file.code}` +
    `${inline ? `?mode=inline&nocache=${file.lastModified.replace(/\D/g, '')}` : ''}`
);

function Dossier (props) {
  const dossierInst = new FileDossier(props.dossierData.query); // create instance { dossierKey, dossierPackage, dossierCode }
  const [{ dossierData, loading, error }, dossierActions] = dossierInst.useDossier(props.dossierData); // init hook
  const { query, response: dossier, error: dossierError } = dossierData;
  let DossierComponent;
  switch (props.mode) {
    case 'preview': DossierComponent = DossierPreview; break;
    case 'table': DossierComponent = DossierTable; break;
    default: DossierComponent = DossierTable; // default as table
  }

  return (
    <div className="dossier-wrap">
      <Segment basic loading={loading} style={{ padding: 0 }}>
        {props.header && <Header dividing content={dossier.name}/>}
        {!!dossierError && <Message error visible header="Ошибка при загрузке досье" content={dossierError}/>}
        {!!error && <Message error visible header="Ошибка при выполнении действия" content={error}/>}
        {(!dossier.dossierFile || !dossier.dossierFile.length) && <Message error visible header="Отсутствуют файлы в досье"/>}
        {dossier && dossier.dossierFile && dossier.dossierFile.length > 0 &&
          <DossierComponent
            dossier={dossier}
            dossierActions={dossierActions}
            query={query}
          />
        }
      </Segment>
      {props.children && <div className="dossier-children">
        {React.cloneElement(props.children, { dossier, dossierParams: query })}
      </div>}
    </div>
  );
}

Dossier.propTypes = {
  dossierData: PropTypes.shape({
    query: PropTypes.object.isRequired,
    response: PropTypes.object,
    error: PropTypes.string,
  }).isRequired,
  mode: PropTypes.string,
  header: PropTypes.bool,
  children: PropTypes.element,
};

export default Dossier;
