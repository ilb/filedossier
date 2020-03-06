import { useState } from 'react';
import PropTypes from 'prop-types';
import { Button, Modal, Grid, List, Message, Dropdown, Label, Checkbox } from 'semantic-ui-react';
import FileContent from '../DossierPreview/FileContent';
import Thumbnail from '../DossierPreview/Thumbnail';
import './index.css';

function ExternalDossier ({ external, dossierFile, importFile }) {
  if (!external || !external.length) { return null; }
  const initialState = { selectedFile: external[0], selectedFiles: [], uploadMode: 'new', loading: false, error: null };
  const [state, _setState] = useState(initialState);
  const { selectedFile, selectedFiles, uploadMode, loading, error } = state;
  const setState = (newState) => { _setState({ ...state, ...newState }); };

  const closeModal = () => {
    const closeIcon = document.querySelector('#externalDossierModal > i.close.icon');
    closeIcon.click();
  };

  const importSelectedFiles = async () => {
    if (selectedFiles && selectedFiles.length) {
      setState({ loading: true, error: null });
      const importResult = await importFile({ files: selectedFiles, uploadMode });
      setState({ loading: false, error: importResult.error });
      if (!importResult.error/* && uploadMode !== 'merge' */) {
        closeModal();
      }
    }
  };

  const thumbnailSizes = {
    width: 120,
    height: 170,
  };

  const selectFile = (e, { checked }) => {
    e.stopPropagation();
    const file = external[e.currentTarget.dataset.index];
    const newSelectedFiles = checked ? [...selectedFiles, file] : selectedFiles.filter(f => f.path !== file.path);
    setState({ selectedFile: file, selectedFiles: newSelectedFiles });
  };

  const renderFileThumbnail = (file, fileIndex) => {
    const selectedIndex = selectedFiles.findIndex(f => f.path === file.path) + 1;

    return (
      <List.Item key={file.path} style={{ display: 'inline-block' }}
        active={file.path === selectedFile.path}
        onClick={() => { setState({ selectedFile: file }); }}
      >
        <Thumbnail dossierFile={file} sizes={thumbnailSizes}/>
        <Checkbox className="thumbnail-file-name" style={{ width: `${thumbnailSizes.width}px` }}
          data-index={fileIndex} onChange={selectFile} checked={!!selectedIndex} label={file.name}
        />
        {!!selectedIndex && <Label color="blue" circular content={selectedIndex}/>}
      </List.Item>
    );
  };

  return (
    <div className="external-dossier">
      <Modal
        id="externalDossierModal"
        size="fullscreen"
        closeIcon
        closeOnDimmerClick={false}
        trigger={<Button basic content="Выбрать файл" id="fdChooseExternalFile"/>}
        onOpen={() => { setState(initialState); }}
        style={{ position: 'static' }}
      >
        <Modal.Header>
          Выбор файла: {dossierFile.name}
        </Modal.Header>
        <Modal.Content>
          <Grid>
            <Grid.Column style={{ width: '450px' }}>
              {/* <List selection onItemClick={(e, { index }) => { selectFile(external[index]); }}
                items={external.map((file, index) => ({ key: file.path, content: file.name, active: file.path === selectedFile.path, index }))}
                style={{ maxHeight: 'calc(100vh - 270px)', overflow: 'auto' }}
              /> */}
              <List selection style={{ maxHeight: 'calc(100vh - 270px)', overflow: 'auto' }} className="external-dossier-files-list">
                {external.map(renderFileThumbnail)}
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
          <Button type="button" color="green" attached="left"
            content={`${uploadMode === 'merge' ? 'Догрузить' : 'Загрузить'} файлы: ${selectedFiles.length}`}
            onClick={importSelectedFiles}
            loading={loading}
            disabled={loading || !selectedFiles.length}
          />
          <Dropdown text=" "
            className="right attached button green icon upload-mode-selection"
            icon={`${uploadMode === 'merge' ? 'copy' : 'file'} outline`}
            onChange={(e, { value }) => { setState({ uploadMode: value }); }}
            value={uploadMode}
            options={[
              { key: 'new', text: 'Загрузить новый файл', value: 'new', icon: 'file outline' },
              { key: 'merge', text: 'Объединить файлы', value: 'merge', icon: 'copy outline' },
            ]}
          />
        </Modal.Actions>
      </Modal>
    </div>
  );
}

ExternalDossier.propTypes = {
  external: PropTypes.array.isRequired,
  dossierFile: PropTypes.object.isRequired,
  importFile: PropTypes.func.isRequired,
};

export default ExternalDossier;
