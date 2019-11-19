import React from 'react';
import PropTypes from 'prop-types';
import ControlsMenu, { getZoomOutScale, getZoomInScale, calcScaleNum, dragToScroll } from './ControlsMenu';
import { getFileLink } from '../../Dossier';

class DossierImage extends React.Component {
  state = {
    src: null,
    scaleValue: 'pageWidthOption', /* for selection */
    scaleNum: 1,
    rotate: 0,
    fileDate: null,
  };

  componentDidMount () {
    const { query, dossierFile } = this.props;
    const src = getFileLink({ ...query, file: dossierFile, inline: true });
    this.setState({ src, fileDate: dossierFile.lastModified });
  }

  static getDerivedStateFromProps (props, state) {
    const { query, dossierFile } = props;
    if (state.fileDate && state.fileDate !== dossierFile.lastModified) {
      const src = getFileLink({ ...query, file: dossierFile, inline: true });
      return { src, fileDate: dossierFile.lastModified };
    }
    return null;
  }

  rotateFile = (angle) => {
    this.resetContainerScroll();
    let rotate = this.state.rotate + angle;
    if (rotate < 0) { rotate = 270; }
    if (rotate > 270) { rotate = 0; }
    this.setupRotatedImageSize(rotate);
  };

  resetContainerScroll = () => {
    const img = this.props.contentRef.current;
    if (img) {
      const imgContainer = img.parentNode;
      imgContainer.scrollTop = imgContainer.scrollLeft = 0;
    }
  };

  imageOnLoadHandler = async () => {
    this.setupRotatedImageSize(0); // reset rotation
    await new Promise(resolve => { setTimeout(resolve, 10); });
    this.initManipulations();
    this.resetContainerScroll();
  };

  setupRotatedImageSize = async (rotate) => {
    const img = this.props.contentRef.current;
    if (img) {
      this.setState({ rotate }, () => {
        this.setScale(this.state.scaleValue); // NOTE: use scaleValue on rotate
      });
    }
  };

  initManipulations = () => {
    const img = this.props.contentRef.current;
    if (img) {
      const imgContainer = img.parentNode;
      // scroll
      imgContainer.removeEventListener('DOMMouseScroll', this.onMouseScrollHandler, false);
      imgContainer.onmousewheel = this.onMouseScrollHandler;
      imgContainer.addEventListener('DOMMouseScroll', this.onMouseScrollHandler, false);

      // drag
      imgContainer.onmousedown = dragToScroll;
    }
  };

  onMouseScrollHandler = (e) => {
    if (e.ctrlKey) {
      e.preventDefault();
      e.stopPropagation();
      const { scaleNum } = this.state;
      if (scaleNum) {
        const newScaleNum = (e.deltaY || e.detail) > 0 ? getZoomOutScale(scaleNum) : getZoomInScale(scaleNum);
        this.setScale(newScaleNum);
      }
    }
  }

  setScale = (scale) => {
    const { rotate } = this.state;
    const img = this.props.contentRef.current;
    const canvasContainer = img.parentNode;
    const { width, height } = window.getComputedStyle(canvasContainer);
    const containerSizes = { width: parseFloat(width), height: parseFloat(height) };
    const elementSizes = { width: img.naturalWidth, height: img.naturalHeight };
    const scaleNum = calcScaleNum({ scale, rotate, containerSizes, elementSizes });

    const newWidth = img.naturalWidth * scaleNum;
    const newHeight = img.naturalHeight * scaleNum;
    img.style.width = `${newWidth}px`;
    img.style.minWidth = `${newWidth}px`;
    img.style.maxWidth = `${newWidth}px`;
    img.style.height = `${newHeight}px`;
    img.style.minHeight = `${newHeight}px`;

    if (rotate % 180 !== 0) { // 90 or 270
      img.style.marginTop = (newWidth - newHeight) / 2 + 'px';
      img.style.marginLeft = -(newWidth - newHeight) / 2 + 'px';
    } else {
      img.style.marginTop = 0;
      img.style.marginLeft = 0;
    }

    this.setState({ scaleValue: scale, scaleNum });
  }

  render () {
    const { query, dossierFile, contentRef } = this.props;
    const { src, scaleValue, scaleNum, rotate } = this.state;
    return (
      <div className="dossier-img">
        <ControlsMenu
          query={query}
          dossierFile={dossierFile}
          scaleValue={scaleValue} scaleNum={scaleNum} setScale={this.setScale}
          rotateFile={this.rotateFile}
        />
        <div className="dossier-img-container">
          <img ref={contentRef} src={src}
            className={`ui fluid image dossier-img-rotate${rotate}`}
            onLoad={this.imageOnLoadHandler}
            alt="Не удалось отобразить preview файла"
          />
        </div>
      </div>
    );
  }
}

DossierImage.propTypes = {
  dossierFile: PropTypes.object.isRequired,
  dossierActions: PropTypes.object.isRequired,
  query: PropTypes.object.isRequired,
  contentRef: PropTypes.object.isRequired,
};

export default DossierImage;
