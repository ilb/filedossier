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
  };

  constructor (props) {
    super(props);

    this.state = {
      fileName: null,
      scanColor: props.scanColor || 'color',
      scanDpi: (props.scanDpi || 150).toString(),
    };
  }

  changeField = (e, { name, value }) => {
    this.setState({ [name]: value });
  }

  selectFile = (event) => {
    const fileName = propOr(null, 'target.files[0].name')(event);
    this.changeField(null, { name: 'fileName', value: fileName });
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
    this.changeField(null, { name: 'fileName', value: fileName });
  };

  uploadFile = () => {
    const { fileId } = this.props;
    const { fileName } = this.state;
    const data = { fileName, fileId };
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
    const { fileId, loading, accept } = this.props;
    const { fileName, scanColor, scanDpi } = this.state;

    return (
      <div className="bystro-scan">
        <Button type="button" basic attached="left" content="БыстроСкан" onClick={this.scanStart} disabled={loading}/>
        <Dropdown simple basic open={false} className="right attached button icon setting">
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

        <Button as="div" basic className="bystro-scan-file-button" disabled={loading} title={fileName}>
          <span>{fileName || 'Выбрать файл'}</span>
          <input type="file" id={fileId} accept={accept} onChange={this.selectFile} disabled={loading}/>
        </Button>

        <Button type="button" color="green" content="Загрузить" onClick={this.uploadFile} loading={loading} disabled={loading}/>
      </div>
    );
  }
}

export default BystroScan;
