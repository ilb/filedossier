import React from 'react';
import PropTypes from 'prop-types';
import { Image, Button } from 'semantic-ui-react';
import { getFileLink } from '../../Dossier';

class DossierImage extends React.Component {
  state = {
    img: null,
    src: null,
    fileDate: null,
    rotate: 0,
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
    const { img } = this.state;
    let rotate = this.state.rotate + angle;
    if (rotate < 0) { rotate = 270; }
    if (rotate > 270) { rotate = 0; }
    this.setupRotatedImageSize(img, rotate);
  };

  imageOnLoadHandler = (event) => {
    const img = event.currentTarget;
    this.setState({ img });
    this.setupRotatedImageSize(img, 0);
    this.initImageManipulations(img, 0);
  };

  setupRotatedImageSize = async (img, rotate) => {
    if (img) {
      this.setState({ rotate });
      await new Promise(resolve => { setTimeout(resolve, 10); });
      // reset
      img.style.height = null;
      img.style.minHeight = null;
      img.style.width = null;
      img.style.minWidth = null;
      img.style.maxWidth = null;
      img.style.marginTop = null;
      img.style.marginLeft = null;

      if (rotate % 180 !== 0) {
        const { clientWidth, clientHeight } = img;
        const newHeight = clientWidth;
        const newWidth = clientWidth * clientWidth / clientHeight;

        img.style.height = newHeight + 'px';
        img.style.minHeight = newHeight + 'px';
        img.style.width = newWidth + 'px';
        img.style.minWidth = newWidth + 'px';
        img.style.maxWidth = newWidth + 'px';

        img.style.marginTop = (newWidth - newHeight) / 2 + 'px';
        img.style.marginLeft = -(newWidth - newHeight) / 2 + 'px';
      }
    }
  };

  initImageManipulations = (img/* , rotate */) => {
    if (img) {
      const imgContainer = img.parentNode;
      // scroll
      imgContainer.removeEventListener('DOMMouseScroll', this.menuMouseScroll, false);
      imgContainer.onmousewheel = this.menuMouseScroll;
      imgContainer.addEventListener('DOMMouseScroll', this.menuMouseScroll, false);

      // drag
      img.onmousedown = this.onMouseDownHandler;
    }
  };

  menuMouseScroll = (e) => {
    e.preventDefault();
    e.stopPropagation();
    const img = e.currentTarget.querySelector('img');
    const coefficient = (e.deltaY || e.detail) > 0 ? 10 / 11 : 11 / 10;
    let { width, height } = img.getBoundingClientRect();
    const rotated = img.classList.contains('dossier-img-rotate90') || img.classList.contains('dossier-img-rotate270');
    if (rotated) {
      [width, height] = [height, width]; // swap
    }
    const newWidth = width * coefficient;
    const newHeight = height * coefficient;
    if (newWidth && (newWidth < 50 || newWidth > 10000)) { return; }
    img.style.width = `${newWidth}px`;
    img.style.minWidth = `${newWidth}px`;
    img.style.maxWidth = `${newWidth}px`;
    img.style.height = `${newHeight}px`;
    img.style.minHeight = `${newHeight}px`;

    if (rotated) {
      img.style.marginTop = (newWidth - newHeight) / 2 + 'px';
      img.style.marginLeft = -(newWidth - newHeight) / 2 + 'px';
    }
  }

  onMouseDownHandler = (e) => {
    e.preventDefault();
    e.stopPropagation();
    const img = e.currentTarget;
    const imgContainer = img.parentNode;
    const startScrollTop = imgContainer.scrollTop || 0;
    const startscrollLeft = imgContainer.scrollLeft || 0;
    const startX = e.pageX;
    const startY = e.pageY;

    document.onmousemove = (e) => {
      imgContainer.scrollTop = startScrollTop + startY - e.pageY;
      imgContainer.scrollLeft = startscrollLeft + startX - e.pageX;
    };

    document.onmouseup = () => {
      document.onmousemove = null;
      document.onmouseup = null;
    };
  }

  render () {
    const { src, rotate } = this.state;
    return (
      <div className="dossier-img">
        <div className="dossier-rotate-buttons">
          <Button basic size="tiny" icon="repeat" onClick={this.rotateFile.bind(this, -90)} attached="right"/>
          <Button basic size="tiny" icon="repeat" onClick={this.rotateFile.bind(this, 90)} attached="right"/>
        </div>
        <div className="dossier-img-container">
          <Image fluid src={src}
            className={`dossier-img-rotate${rotate}`}
            onLoad={this.imageOnLoadHandler}
            // alt="Не удалось отобразить preview файла"
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
};

export default DossierImage;
