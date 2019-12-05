import { useState } from 'react';
import PropTypes from 'prop-types';
import { Button, Modal, Grid, List, Message } from 'semantic-ui-react';
import FileContent from '../DossierPreview/FileContent';
import Thumbail from '../DossierPreview/Thumbail';
import './index.css';

function ExternalDossier ({ external, dossierFile, dossierActions, loading, error }) {
  if (!external || !external.length) { return null; }
  const [selectedFile, selectFile] = useState(external[0]);

  const closeModal = () => {
    const closeIcon = document.querySelector('#externalDossierModal > i.close.icon');
    closeIcon.click();
  };

  const importFile = async () => {
    if (selectedFile && selectedFile.path) {
      const importResult = await dossierActions.importFile({ fileCode: dossierFile.code, url: selectedFile.path });
      if (!importResult.error) {
        closeModal();
      }
    }
  };

  const thumbailSizes = {
    width: 120,
    height: 170,
  };

  return (
    <div className="external-dossier">
      <Modal
        id="externalDossierModal"
        size="fullscreen"
        closeIcon
        closeOnDimmerClick={false}
        trigger={<Button content="Выбрать"/>}
        className="scrolling"
      >
        <Modal.Header>
          Выбрать файл: {dossierFile.name}
        </Modal.Header>
        <Modal.Content>
          <Grid>
            <Grid.Column style={{ width: '450px' }}>
              {/* <List selection onItemClick={(e, { index }) => { selectFile(external[index]); }}
                items={external.map((file, index) => ({ key: file.path, content: file.name, active: file.path === selectedFile.path, index }))}
                style={{ maxHeight: 'calc(100vh - 270px)', overflow: 'auto' }}
              /> */}
              <List selection style={{ maxHeight: 'calc(100vh - 270px)', overflow: 'auto' }} className="external-dossier-files-list">
                {external.map(file => (
                  <List.Item key={file.path} style={{ display: 'inline-block' }}
                    active={file.path === selectedFile.path}
                    onClick={selectFile.bind(null, file)}
                  >
                    <Thumbail dossierFile={file} sizes={thumbailSizes}/>
                    <div className="thumbail-file-name" style={{ width: `${thumbailSizes.width}px` }}>
                      {file.name}
                    </div>
                  </List.Item>
                ))}
              </List>
            </Grid.Column>
            <Grid.Column style={{ width: 'calc(100% - 450px)' }}>
              {selectedFile &&
                <FileContent
                  dossierFile={selectedFile}
                  previewOffset={270}
                />
              }
            </Grid.Column>
          </Grid>
        </Modal.Content>
        <Modal.Actions>
          {!!error && <Message error compact content={error} style={{ margin: 0, padding: '0.6rem 1rem' }}/>}
          <Button color="green" content="Выбрать" onClick={importFile} loading={loading} disabled={loading}/>
        </Modal.Actions>
      </Modal>
    </div>
  );
}

ExternalDossier.propTypes = {
  external: PropTypes.array.isRequired,
  dossierFile: PropTypes.object.isRequired,
  dossierActions: PropTypes.object.isRequired,
  loading: PropTypes.bool,
  error: PropTypes.string,
};

export default ExternalDossier;
