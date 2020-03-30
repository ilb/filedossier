import React from 'react';
import PropTypes from 'prop-types';
import { Header, Segment, Message } from 'semantic-ui-react';
import FileDossier from '../classes/FileDossier';
import DossierPreview from './DossierPreview';
import DossierTable from './DossierTable';

function Dossier (props) {
  const dossierInst = new FileDossier({ dossierParams: props.dossierData.dossierParams });
  const [{ dossierData, loading, error }, dossierActions] = dossierInst.useDossier(props.dossierData); // init hook
  const { dossierParams, dossier, error: dossierError, external, externalError } = dossierData || {};
  let DossierComponent;
  switch (props.mode) {
    case 'preview': DossierComponent = DossierPreview; break;
    case 'table': DossierComponent = DossierTable; break;
    default: DossierComponent = DossierTable; // default as table
  }

  const childrenProps = { dossier, dossierError, dossierParams, dossierActions };
  const previewOffset = props.previewOffset || 140; // preview height will be calc(100vh - 140px)

  return (
    <div className="dossier-wrap">
      <Segment basic loading={loading} style={{ padding: 0 }}>
        {props.header && dossier && <Header dividing content={dossier.name}/>}
        {!!dossierError && <Message error visible header="Ошибка при загрузке досье" content={dossierError}/>}
        {!!externalError && <Message error visible header="Ошибка при загрузке внешнего досье" content={externalError}/>}
        {!!error && <Message error visible header="Ошибка при выполнении действия с досье" content={error}/>}
        {(!dossierParams || (!dossier && !dossierError)) && <Message error visible header="В компонент не переданы данные по досье"/>}
        {(dossier && (!dossier.dossierFile || !dossier.dossierFile.length)) && <Message error visible header="Отсутствуют файлы в досье"/>}
        {dossier && dossier.dossierFile && dossier.dossierFile.length > 0 &&
          <DossierComponent
            dossierParams={dossierParams}
            dossier={dossier}
            external={external}
            dossierActions={dossierActions}
            previewOffset={previewOffset}
            fileCode={props.fileCode}
            readOnly={props.readOnly}
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
    dossier: PropTypes.object,
    external: PropTypes.array,
    error: PropTypes.string,
  }).isRequired,
  mode: PropTypes.string,
  header: PropTypes.bool,
  children: PropTypes.oneOfType([PropTypes.element, PropTypes.func]),
  previewOffset: PropTypes.number,
  fileCode: PropTypes.string, /* specify exactly one file, ignore hidden flag */
  readOnly: PropTypes.bool, /* manualy hide upload block */
};

export default Dossier;
