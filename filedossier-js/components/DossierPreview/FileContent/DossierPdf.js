import React from 'react';
import PropTypes from 'prop-types';
import { Message, Loader } from 'semantic-ui-react';
import ControlsMenu, { getZoomOutScale, getZoomInScale, calcScaleNum, dragToScroll } from './ControlsMenu';
import PDFJS from 'pdfjs-dist';
PDFJS.workerSrc = '//cdnjs.cloudflare.com/ajax/libs/pdf.js/2.2.228/pdf.worker.js'; // TODO setup static worker from pdfjs-dist/build
PDFJS.GlobalWorkerOptions.workerSrc = PDFJS.workerSrc;

class DossierPdf extends React.Component {
  state = {
    pdf: null,
    currentPage: undefined,
    pageText: undefined,
    scaleValue: 'pageWidthOption', /* for selection */
    scaleNum: null,
    rotate: 0,
    rotateLoading: null,
    error: null,
    pdfLoading: false,
  };

  componentDidMount () {
    const { dossierFile } = this.props;
    this.initPdf(dossierFile);

    const canvasContainer = this.props.contentRef.current;
    canvasContainer.addEventListener('scroll', this.scrollUpdated, true);
  }

  UNSAFE_componentWillReceiveProps (nextProps) { // eslint-disable-line camelcase
    const oldFile = this.props.dossierFile;
    const newFile = nextProps.dossierFile;
    if (oldFile.path !== newFile.path) { // new file uploaded
      this.initPdf(newFile);
    }
  }

  initPdf = ({ path, rotate }) => {
    // path = 'http://localhost:3000/static/test0.pdf';
    this.setState({ pdf: null, pdfLoading: true, currentPage: 1, pageText: 1 });
    this.clearCanvases();
    const loadingTask = PDFJS.getDocument(path);
    loadingTask.promise.then(
      pdf => {
        this.setState({ pdf, pdfLoading: false }, () => {
          this.drawPages({ pdf, rotate }); // initial draw
        });
        this.initManipulations();
      },
      error => {
        this.setState({ error, pdfLoading: false });
      }
    );

    this.resetContainerScroll();
  };

  getPage = (pdf, pageNum) => {
    return new Promise(resolve => {
      pdf.getPage(pageNum).then(resolve);
    });
  }

  clearCanvases = () => {
    const canvasContainer = this.props.contentRef.current;
    const canvases = canvasContainer && canvasContainer.querySelectorAll('canvas');
    if (canvasContainer && canvases.length > 0) {
      for (let i = 0; i < canvases.length; i++) {
        canvasContainer.removeChild(canvases[i]);
      }
    }
  };

  drawPages = async ({ pdf, scale, rotate = 0, callback }) => {
    this.clearCanvases();
    const canvasContainer = this.props.contentRef.current;
    const { width, height } = window.getComputedStyle(canvasContainer);
    const containerSizes = { width: parseFloat(width), height: parseFloat(height) };

    const pages = [];
    const elementSizes = { width: 0, height: 0 };
    // find max width and height of pdf pages
    for (let i = 1; i <= pdf.numPages; i++) {
      const page = await this.getPage(pdf, i);
      const { width, height } = page.getViewport({ scale: 1.0 });
      if (width > elementSizes.width) { elementSizes.width = width; }
      if (height > elementSizes.height) { elementSizes.height = height; }
      pages.push(page);
    }

    const scaleNum = calcScaleNum({ scale, rotate, containerSizes, elementSizes });
    // set all canvas sizes
    for (let i = 0; i < pages.length; i++) {
      const canvas = document.createElement('canvas');
      canvas.id = `pdfPage${i + 1}`;
      const rotation = pages[i].rotate + rotate;
      const viewport = pages[i].getViewport({ scale: scaleNum, rotation });
      this.setElementSize(canvas, viewport);
      canvasContainer.appendChild(canvas);
    }
    this.setState({ pdf, scaleValue: scale, scaleNum, rotate });

    if (callback) { callback(); } // callback on all sizes setted

    // fill render pool with pages ond start render loop
    this.renderPagesPool = [...Array(pdf.numPages + 1).keys()].slice(1);
    while (this.renderPagesPool && this.renderPagesPool.length > 0) {
      const canvas = canvasContainer.querySelector(`canvas#pdfPage${this.renderPagesPool[0]}`);
      await this.drawPage({ pdf, pageNum: this.renderPagesPool[0], scale: scaleNum, rotate, canvas });
      this.renderPagesPool.shift();
    }
  };

  drawPage = ({ pdf, pageNum, scale, rotate = 0, canvas }) => {
    return new Promise((resolve, reject) => {
      if (this.renderTask) { this.renderTask.cancel(); this.renderTask = null; }

      pdf.getPage(pageNum).then((page) => {
        const rotation = page.rotate + rotate;
        const viewport = page.getViewport({ scale, rotation });
        const canvasContext = canvas.getContext('2d');
        this.setElementSize(canvas, viewport);
        this.renderTask = page.render({ canvasContext, viewport });
        this.renderTask.promise.then(
          () => { this.renderTask = null; resolve(); },
          (error) => { this.renderTask = null; reject(error); },
        );
      });
    });
  };

  setElementSize = (element, sizes) => {
    element.style.width = `${sizes.width}px`;
    element.style.height = `${sizes.height}px`;
    element.height = sizes.height;
    element.width = sizes.width;
  };

  initManipulations = () => {
    const canvasContainer = this.props.contentRef.current;
    if (canvasContainer) {
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
      const { scaleNum } = this.state;
      if (scaleNum) {
        const newScaleNum = (e.deltaY || e.detail) > 0 ? getZoomOutScale(scaleNum) : getZoomInScale(scaleNum);
        this.setScale(newScaleNum);
      }
    }
  }

  setScale = async (scale) => {
    if (this.state.scaleNum === scale) { return; }
    const isAllowed = await this.waitForPreviousRender();
    if (!isAllowed) { return; }

    const { pdf, rotate, currentPage } = this.state;
    await this.drawPages({
      pdf, scale, rotate,
      callback: () => { this.setPage(null, { value: currentPage }); },
    });
  }

  waitForPreviousRender = async () => {
    if (this.waiter) { return false; }
    while (this.renderTask) { // while some other page renders
      this.renderPagesPool = []; // clear render pool (cancel all other renderings)
      // this.renderTask.cancel(); // TODO didn't work as expected
      this.waiter = true;
      await new Promise(resolve => setTimeout(resolve, 10)); // and wait
    }
    this.waiter = false;
    return true;
  };

  setPage = (event, { value }) => {
    const pageNum = Number(value);
    const { pdf } = this.state;
    if (!pdf || pageNum > pdf.numPages) {
      if (document.activeElement) { document.activeElement.blur(); }
    } else {
      const canvasContainer = this.props.contentRef.current;
      const canvas = canvasContainer.querySelector(`canvas#pdfPage${pageNum}`);
      canvas.scrollIntoView({ block: 'start' });
      this.setState({ currentPage: pageNum, pageText: pageNum });
    }
  }

  rotateFile = async (angle) => {
    const isAllowed = await this.waitForPreviousRender();
    if (!isAllowed) { return; }
    const { dossierFile, dossierActions } = this.props;
    const { pdf, scaleValue, rotate, currentPage } = this.state;
    let newRotate = rotate + angle;
    if (newRotate < 0) { newRotate = 270; }
    if (newRotate > 270) { newRotate = 0; }
    this.setState({ rotateLoading: angle < 0 ? 'CCW' : 'CW' }); // counterclockwise / clockwise
    await Promise.all([
      dossierActions.saveFileRotation({ file: dossierFile, angle }),
      this.drawPages({
        pdf, scale: scaleValue, rotate: newRotate, // NOTE: use scaleValue on rotate
        callback: () => { this.setPage(null, { value: currentPage }); },
      }),
    ]);
    this.setState({ rotateLoading: null });
  };

  resetContainerScroll = () => {
    const canvasContainer = this.props.contentRef.current;
    if (canvasContainer) {
      canvasContainer.scrollTop = canvasContainer.scrollLeft = 0;
    }
  };

  setPageText = (pageText) => {
    this.setState({ pageText });
  };

  /* watchScroll = (viewArea, callback) => {
    let rAF = null;
    const debounceScroll = () => {
      if (rAF) { console.log('here'); return; }
      rAF = window.requestAnimationFrame(() => {
        rAF = null;
        callback(viewArea);
      });
    };
    viewArea.addEventListener('scroll', debounceScroll, true);
  } */

  scrollUpdated = (event) => {
    const canvasContainer = event.currentTarget;
    const { currentPage } = this.state;
    const viewTop = canvasContainer.scrollTop;
    const viewBottom = viewTop + canvasContainer.clientHeight;

    // find visible pages (canvases)
    const canvases = canvasContainer.querySelectorAll('canvas');
    const visiblePages = [];
    let lastEdge = -1;
    for (let i = 0; i < canvases.length; i++) {
      const canvas = canvases[i];
      const canvasTop = canvas.offsetTop + canvas.clientTop; // currentHeight
      const canvasHeight = canvas.clientHeight; // viewHeight
      const canvasBottom = canvasTop + canvasHeight; // viewBottom

      if (lastEdge === -1) {
        if (canvasBottom >= viewBottom) {
          lastEdge = canvasBottom;
        }
      } else if (canvasTop > lastEdge) {
        break;
      }

      if (canvasBottom <= viewTop || canvasTop >= viewBottom) {
        continue;
      }

      const hiddenHeight = Math.max(0, viewTop - canvasTop) + Math.max(0, canvasBottom - viewBottom);
      const percent = (canvasHeight - hiddenHeight) * 100 / canvasHeight | 0;

      visiblePages.push({
        pageNum: i + 1,
        percent,
      });
    }

    if (!visiblePages.length) { return; }
    // calc current page num
    let newPageNum = visiblePages[0].pageNum;
    if (visiblePages[1] && visiblePages[1].percent > visiblePages[0].percent) {
      newPageNum++;
    }
    if (newPageNum !== currentPage) {
      this.setState({ currentPage: newPageNum, pageText: newPageNum });
    }

    /*
    // TEST: change render priorities
    if (this.renderPagesPool && this.renderPagesPool.length) {
      let newRenderPool = Array.from(this.renderPagesPool); // copy
      const pageNums = visiblePages.map(page => page.pageNum).filter(pageNum => newRenderPool.indexOf(pageNum !== -1));
      if (this.isArraysEqual(pageNums, this.pageNumsCache)) { return; }
      this.pageNumsCache = pageNums;
      console.log('pageNums', pageNums);
      if (pageNums.length) {
        newRenderPool = newRenderPool.filter(pageNum => pageNums.indexOf(pageNum) === -1); // remove
        newRenderPool.splice(1, 0, ...pageNums); // insert to start
        this.renderPagesPool = newRenderPool;
      }
    }
    */
  };

  isArraysEqual (a, b) {
    if (a === b) return true;
    if (!a || !b) return false;
    if (a.length !== b.length) return false;
    for (let i = 0; i < a.length; i++) {
      if (a[i] !== b[i]) return false;
    }
    return true;
  }

  render () {
    const { dossierFile, contentRef } = this.props;
    const { pdf, currentPage, pageText, scaleValue, scaleNum, error, pdfLoading, rotateLoading } = this.state;

    return (
      <div className="dossier-pdf">
        <ControlsMenu
          dossierFile={dossierFile} pdf={pdf}
          currentPage={currentPage} pageText={pageText} setPage={this.setPage} setPageText={this.setPageText}
          scaleValue={scaleValue} scaleNum={scaleNum} setScale={this.setScale}
          rotateFile={this.rotateFile} rotateLoading={rotateLoading}
        />
        <div className="dossier-pdf-container" ref={contentRef}>
          {pdfLoading && <Loader active size="small" style={{ zIndex: 5 }}/>}
          {error && <Message error visible header="Ошибка при открытии pdf файла" content={error.message} style={{ margin: 0 }}/>}
        </div>
      </div>
    );
  }
}

DossierPdf.propTypes = {
  dossierFile: PropTypes.object.isRequired,
  dossierActions: PropTypes.object.isRequired,
  contentRef: PropTypes.object.isRequired,
};

export default DossierPdf;
