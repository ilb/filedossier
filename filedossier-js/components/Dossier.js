import React from 'react';
import PropTypes from 'prop-types';
import { Header, Segment, Message } from 'semantic-ui-react';
import FileDossier from '../classes/FileDossier';
import DossierPreview from './DossierPreview';
import DossierTable from './DossierTable';

export const getFileLink = ({ dossierKey, dossierPackage, dossierCode, dossierMode, file, inline }) => (
  `https://devel.net.ilb.ru/workflow-web/web/v2/` +
    `dossiers/${dossierKey}/${dossierPackage}/${dossierCode}/${dossierMode}/dossierfiles/${file.code}` +
    `?nocache=${(file.lastModified || '').replace(/\D/g, '')}` +
    `${inline ? `&mode=inline` : ''}`
);

export const getFileAccept = (file) => {
  if (file && file.allowedMediaTypes && file.allowedMediaTypes.allowedMediaType) {
    return file.allowedMediaTypes.allowedMediaType.join(',');
  }
};

// creates uniq string based on dossierFile and dossierParams
export const getFileId = ({ dossierKey, dossierPackage, dossierCode, file }) => (
  `file_${dossierKey}_${dossierPackage}_${dossierCode}_${file.code}`
);

function Dossier (props) {
  const dossierInst = new FileDossier(props.dossierData.dossierParams);
  const [{ dossierData, loading, error }, dossierActions] = dossierInst.useDossier(props.dossierData); // init hook
  const { dossierParams, response: dossier, error: dossierError } = dossierData || {};
  let DossierComponent;
  switch (props.mode) {
    case 'preview': DossierComponent = DossierPreview; break;
    case 'table': DossierComponent = DossierTable; break;
    default: DossierComponent = DossierTable; // default as table
  }

  const childrenProps = { dossier, dossierError, dossierParams, dossierActions };

  return (
    <div className="dossier-wrap">
      <Segment basic loading={loading} style={{ padding: 0 }}>
        {props.header && dossier && <Header dividing content={dossier.name}/>}
        {!!dossierError && <Message error visible header="Ошибка при загрузке досье" content={dossierError}/>}
        {!!error && <Message error visible header="Ошибка при выполнении действия с досье" content={error}/>}
        {(!dossierParams || (!dossier && !dossierError)) && <Message error visible header="В компонент не переданы данные по досье"/>}
        {(dossier && (!dossier.dossierFile || !dossier.dossierFile.length)) && <Message error visible header="Отсутствуют файлы в досье"/>}
        {dossier && dossier.dossierFile && dossier.dossierFile.length > 0 &&
          <DossierComponent
            dossier={dossier}
            dossierActions={dossierActions}
            dossierParams={dossierParams}
          />
        }
      </Segment>
      {props.children && <div className="dossier-children">
        {props.children && typeof props.children === 'function' && props.children(childrenProps)}
        {props.children && typeof props.children === 'object' && React.cloneElement(props.children, childrenProps)}
      </div>}
    </div>
  );
}

Dossier.propTypes = {
  dossierData: PropTypes.shape({
    dossierParams: PropTypes.object.isRequired,
    response: PropTypes.object,
    error: PropTypes.string,
  }).isRequired,
  mode: PropTypes.string,
  header: PropTypes.bool,
  children: PropTypes.oneOfType([PropTypes.element, PropTypes.func]),
};

export default Dossier;
