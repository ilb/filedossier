import React, { Component } from 'react';
import PropTypes from 'prop-types';
import propOr from 'lodash/fp/propOr';
import { Button, Dropdown, Radio, Input } from 'semantic-ui-react';
import PrivAPI from './PrivAPI';
import './index.css';

const makeRainbow = (text) => {
  const colors = ['red', 'orange', 'yellow', 'green', 'aqua', 'blue', 'navy'];
  return (text || '').split('').map((char, index) => (
    <span key={index} style={{ color: colors[index % colors.length], textShadow: '1px 1px 0 black' }}>{char}</span>
  ));
};

class BystroScan extends Component {
  static propTypes = {
    fileId: PropTypes.string.isRequired,
    uploadFile: PropTypes.func.isRequired,
    loading: PropTypes.bool,
    scanColor: PropTypes.string,
    scanDpi: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    accept: PropTypes.string,
    multiple: PropTypes.bool,
  };

  constructor (props) {
    super(props);

    this.state = {
      fileName: null,
      fileTitle: null,
      scanColor: props.scanColor || 'color',
      scanDpi: (props.scanDpi || 150).toString(),
      uploadMode: 'new',
    };
  }

  changeField = (e, { name, value }) => {
    this.setState({ [name]: value });
  }

  selectFile = (event) => {
    const files = propOr(null, 'target.files')(event);
    let fileName, fileTitle;
    if (files && files.length) {
      fileName = files.length === 1 ? files[0].name : `Выбрано файлов: ${files.length}`;
      fileTitle = Array.from(files).map(file => file.name).join(' \n');
    }
    this.setState({ fileName, fileTitle });
  };

  scanStart = (e) => {
    e.preventDefault();
    if (!window.privilegedAPI) {
      alert('privilegedAPI.call failed');
      return;
    }
    const { fileId } = this.props;
    const { scanColor, scanDpi } = this.state;

    const fileid = `bystroscan_${fileId}`; // probably file name
    const fileinputid = fileId; // id of input type=file
    const color = scanColor === 'bw' ? 'bw' : 'color';
    const dpi = Number(scanDpi) > 70 ? Number(scanDpi) : 70;

    PrivAPI.scanStart({
      fileid,
      fileinputid,
      color,
      dpi,
      onscanfinish: this.scanFinish,
    });
  };

  scanFinish = (data) => {
    const fileName = data.value || null;
    window.console.log('scanFinish fileName=', fileName);
    this.setState({ fileName, fileTitle: fileName });
  };

  uploadFile = () => {
    const { fileId } = this.props;
    const { fileName, uploadMode } = this.state;
    const data = { fileName, fileId, uploadMode };
    if (!fileName) {
      data.error = 'Выберите файл';
    } else {
      data.fileInput = document.querySelector(`#${fileId}`);
      if (!data.fileInput) {
        data.error = `Не найден input с id=${fileId}`;
      }
    }
    this.props.uploadFile(data);
  };

  render () {
    const { fileId, loading, accept, multiple } = this.props;
    const { fileName, fileTitle, scanColor, scanDpi, uploadMode } = this.state;

    return (
      <div className="bystro-scan">
        <Button type="button" basic attached="left" content="БыстроСкан" onClick={this.scanStart} disabled={loading} id="fdBystroScan"/>
        <Dropdown simple basic icon="setting" open={false} className="right attached button icon">
          <Dropdown.Menu className="bystro-scan-params">
            <div>
              <Radio name="scanColor" value="color"
                label={makeRainbow('цветное')}
                checked={scanColor === 'color'}
                onChange={this.changeField}
              />
              <Radio name="scanColor" value="bw"
                label="ч/б"
                checked={scanColor === 'bw'}
                onChange={this.changeField}
              />
            </div>
            <div>
              <label>Разрешение</label>
              <Input title="Разрешение" type="text" name="scanDpi" maxLength="3" size="mini"
                value={scanDpi} onChange={this.changeField}
              />
              <label>dpi</label>
            </div>
          </Dropdown.Menu>
        </Dropdown>

        <Button as="div" basic className="bystro-scan-file-button" disabled={loading} title={fileTitle} id="fdChooseFile">
          <span>{fileName || 'Выбрать файл'}</span>
          <input type="file" id={fileId} accept={accept} multiple={multiple} onChange={this.selectFile} disabled={loading}/>
        </Button>

        <Button type="button" color="green" attached="left" content="Загрузить" onClick={this.uploadFile} loading={loading} disabled={loading} id="fdUpload"/>
        <Dropdown text=" "
          className="right attached button green icon"
          icon={`${uploadMode === 'merge' ? 'copy' : 'file'} outline`}
          onChange={(e, { value }) => { this.setState({ uploadMode: value }); }}
          value={uploadMode}
          options={[
            { key: 'new', text: 'Загрузить новый файл', value: 'new', icon: 'file outline' },
            { key: 'merge', text: 'Объединить файлы', value: 'merge', icon: 'copy outline' },
          ]}
        />
      </div>
    );
  }
}

export default BystroScan;
