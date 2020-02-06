import { useState } from 'react';
import PropTypes from 'prop-types';
import { List } from 'semantic-ui-react';
import FileContent from '../DossierPreview/FileContent';
import Thumbnail from '../DossierPreview/Thumbnail';
import './index.css';

function FilesList ({ files, previewOffset = 270 }) {
  if (!files || !files.length) { return null; }
  const [selectedFile, selectFile] = useState(files[0]);

  const thumbnailSizes = {
    width: 120,
    height: 170,
  };

  const renderFileThumbnail = (file) => {
    return (
      <List.Item key={file.path} style={{ display: 'inline-block' }}
        active={file.path === selectedFile.path}
        onClick={() => { selectFile(file); }}
      >
        <Thumbnail dossierFile={file} sizes={thumbnailSizes}/>
        <div className="thumbnail-file-name" style={{ width: `${thumbnailSizes.width}px` }}>{file.name}</div>
      </List.Item>
    );
  };

  return (
    <div className="compact-external-files-list">
      <div className="external-sidebar" style={{ height: `calc(100vh - ${previewOffset}px)`, width: `${thumbnailSizes.width + 46}px`, marginLeft: `-${thumbnailSizes.width + 46}px` }}>
        <List selection style={{ height: '100%', overflow: 'auto', padding: '1rem' }} className="external-dossier-files-list">
          {files.map(renderFileThumbnail)}
        </List>
        <div className="external-sidebar-label">
          Файлы: {files.findIndex(f => f.path === selectedFile.path) + 1} / {files.length}
        </div>
      </div>
      {selectedFile &&
        <FileContent
          dossierFile={selectedFile}
          previewOffset={previewOffset}
        />
      }
    </div>
  );
}

FilesList.propTypes = {
  files: PropTypes.array.isRequired,
  previewOffset: PropTypes.number,
};

export default FilesList;
