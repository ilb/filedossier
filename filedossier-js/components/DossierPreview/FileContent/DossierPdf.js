import React from 'react';
import PropTypes from 'prop-types';
import { Message } from 'semantic-ui-react';
import ControlsMenu, { getZoomOutScale, getZoomInScale, dragToScroll } from './ControlsMenu';
import { getFileLink } from '../../Dossier';
import PDFJS from 'pdfjs-dist';
PDFJS.workerSrc = '//cdnjs.cloudflare.com/ajax/libs/pdf.js/2.2.228/pdf.worker.js'; // TODO setup static worker from pdfjs-dist/build
PDFJS.GlobalWorkerOptions.workerSrc = PDFJS.workerSrc;

class DossierPdf extends React.Component {
  state = {
    pdf: null,
    pageNum: undefined,
    pageText: undefined,
    scale: undefined,
    rotate: 0,
    error: null,
  };

  componentDidMount () {
    const { query, dossierFile } = this.props;
    const pdfPath = getFileLink({ ...query, file: dossierFile });
    // const pdfPath = 'http://localhost:3000/static/book.pdf';
    this.initPdf(pdfPath);
  }

  UNSAFE_componentWillReceiveProps (nextProps) { // eslint-disable-line camelcase
    const oldFile = this.props.dossierFile;
    const newFile = nextProps.dossierFile;
    if (oldFile.lastModified !== newFile.lastModified) { // new file uploaded file
      const pdfPath = getFileLink({ ...nextProps.query, file: newFile });
      // const pdfPath = 'http://localhost:3000/static/test.pdf';
      this.initPdf(pdfPath);
    }
  }

  initPdf = (pdfPath) => {
    PDFJS.getDocument(pdfPath).promise.then(pdf => {
      this.drawPage({ pdf }); // initial draw
      this.initManipulations();
    }).then(null, (error) => {
      this.setState({ error });
    });

    this.resetContainerScroll();
  };

  drawPage = ({ pdf, pageNum, scale, rotate = 0 }) => {
    if (this.renderTask) { this.renderTask.cancel(); }
    const canvas = this.props.contentRef.current;
    if (!canvas) { return; }
    const canvasContainer = canvas.parentNode;
    const { width, height } = window.getComputedStyle(canvasContainer);
    const containerSize = { width: parseFloat(width), height: parseFloat(height) };
    if (!pageNum || !Number(pageNum) || pageNum < 1) { pageNum = 1; }
    if (pageNum > pdf.numPages) { pageNum = pdf.numPages; }
    pageNum = Number(pageNum);
    pdf.getPage(pageNum).then((page) => {
      let currentScale = scale || 'pageWidthOption'; // default on width
      if (currentScale === 'pageActualOption') { currentScale = 1.0; } else
      if (currentScale === 'pageWidthOption' || currentScale === 'pageFitOption') { // calc container size
        let { width: pdfWidth, height: pdfHeight } = page.getViewport({ scale: 1.0 });
        if (rotate % 180 !== 0) {
          [pdfWidth, pdfHeight] = [pdfHeight, pdfWidth]; // swap
        }
        currentScale = containerSize.width / pdfWidth; // scale by width
        if (currentScale * pdfHeight > containerSize.height) {
          if (scale === 'pageFitOption') {
            currentScale = containerSize.height / pdfHeight; // scale by height
          } else {
            currentScale = (containerSize.width - 15) / pdfWidth; // vertical scroll size
          }
        }
      }

      if (!Number(currentScale)) { throw new Error(`Invalid scale value = ${currentScale}`); }

      const rotation = page.rotate + rotate;
      const viewport = page.getViewport({ scale: currentScale, rotation });
      const canvasContext = canvas.getContext('2d');
      canvas.style.width = `${viewport.width}px`;
      canvas.style.height = `${viewport.height}px`;
      canvas.height = viewport.height;
      canvas.width = viewport.width;
      this.renderTask = page.render({
        canvasContext,
        viewport,
      });
      this.setState({ pdf, pageNum, pageText: pageNum, scale: currentScale, rotate });
    });
  };

  initManipulations = () => {
    const canvas = this.props.contentRef.current;
    if (canvas) {
      const canvasContainer = canvas.parentNode;
      // scroll
      canvasContainer.removeEventListener('DOMMouseScroll', this.onMouseScrollHandler, false);
      canvasContainer.onmousewheel = this.onMouseScrollHandler;
      canvasContainer.addEventListener('DOMMouseScroll', this.onMouseScrollHandler, false);

      // drag
      canvasContainer.onmousedown = dragToScroll;
    }
  };

  onMouseScrollHandler = (e) => {
    if (e.ctrlKey) {
      e.preventDefault();
      e.stopPropagation();
      const { scale } = this.state;
      if (scale) {
        const newScale = (e.deltaY || e.detail) > 0 ? getZoomOutScale(scale) : getZoomInScale(scale);
        this.setScale(newScale);
      }
    }
  }

  setScale = (scale) => {
    const { pdf, pageNum, rotate } = this.state;
    this.drawPage({ pdf, pageNum, scale, rotate });
  }

  setPage = (event, { value: pageNum }) => {
    this.resetContainerScroll();
    const { pdf, /* scale, */ rotate } = this.state;
    this.drawPage({ pdf, pageNum, /* scale, */ rotate }); // Note: reset scale on page selection
  }

  rotateFile = (angle) => {
    this.resetContainerScroll();
    const { pdf, pageNum, rotate } = this.state;
    let newRotate = rotate + angle;
    if (newRotate < 0) { newRotate = 270; }
    if (newRotate > 270) { newRotate = 0; }
    this.drawPage({ pdf, pageNum, rotate: newRotate }); // Note: reset scale on rotate
  };

  resetContainerScroll = () => {
    const canvas = this.props.contentRef.current;
    if (canvas) {
      const canvasContainer = canvas.parentNode;
      canvasContainer.scrollTop = canvasContainer.scrollLeft = 0;
    }
  };

  setPageText = (pageText) => {
    this.setState({ pageText });
  };

  render () {
    const { query, dossierFile, contentRef } = this.props;
    const { pdf, pageNum, pageText, scale, error } = this.state;

    return (
      <div className="dossier-pdf">
        <ControlsMenu
          query={query} dossierFile={dossierFile}
          pdf={pdf} pageNum={pageNum} pageText={pageText} scale={scale}
          setPage={this.setPage} setPageText={this.setPageText}
          setScale={this.setScale} rotateFile={this.rotateFile}
        />
        {error && <Message error visible header="Ошибка при открытии pdf файла" content={error.message} style={{ margin: 0 }}/>}
        {!error && <div className="dossier-pdf-container">
          <canvas ref={contentRef}/>
        </div>}
      </div>
    );
  }
}

DossierPdf.propTypes = {
  dossierFile: PropTypes.object.isRequired,
  dossierActions: PropTypes.object.isRequired,
  query: PropTypes.object.isRequired,
  contentRef: PropTypes.object.isRequired,
};

export default DossierPdf;
