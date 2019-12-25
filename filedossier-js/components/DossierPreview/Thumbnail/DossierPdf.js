import React from 'react';
import PropTypes from 'prop-types';
import { Message, Loader } from 'semantic-ui-react';
import PDFJS from 'pdfjs-dist';
PDFJS.workerSrc = '//cdnjs.cloudflare.com/ajax/libs/pdf.js/2.2.228/pdf.worker.js'; // TODO setup static worker from pdfjs-dist/build
PDFJS.GlobalWorkerOptions.workerSrc = PDFJS.workerSrc;

class ThumbnailPdf extends React.Component {
  state = {
    pdf: null,
    error: null,
    loading: false,
  };

  componentDidMount () {
    const { dossierFile } = this.props;
    const pdfPath = dossierFile.path;
    // const pdfPath = 'http://localhost:3000/static/test0.pdf';
    this.initPdf(pdfPath);

    const canvasContainer = this.props.contentRef.current;
    canvasContainer.addEventListener('scroll', this.scrollUpdated, true);
  }

  UNSAFE_componentWillReceiveProps (nextProps) { // eslint-disable-line camelcase
    const oldFile = this.props.dossierFile;
    const newFile = nextProps.dossierFile;
    if (oldFile.path !== newFile.path) { // new file uploaded
      const pdfPath = newFile.path;
      this.initPdf(pdfPath);
    }
  }

  initPdf = (pdfPath) => {
    this.setState({ loading: true, currentPage: 1, pageText: 1 });
    const loadingTask = PDFJS.getDocument(pdfPath);
    loadingTask.promise.then(
      pdf => {
        this.setState({ pdf, loading: false });
        this.drawPage(pdf); // initial draw
      },
      error => {
        this.setState({ error, loading: false });
      }
    );
  };

  drawPage = async (pdf) => {
    if (this.renderTask) { this.renderTask.cancel(); this.renderTask = null; }
    const { contentRef, sizes } = this.props;
    const canvasContainer = contentRef.current;
    const canvas = canvasContainer.querySelector('canvas');

    pdf.getPage(1).then((page) => {
      let viewport = page.getViewport({ scale: 1.0, rotation: page.rotate });
      let scale = sizes.width / viewport.width;
      if (sizes.width / sizes.height < viewport.width / viewport.height) {
        scale = sizes.height / viewport.height;
      }
      viewport = page.getViewport({ scale, rotation: page.rotate });
      this.setElementSize(canvas, sizes);
      const canvasContext = canvas.getContext('2d');
      this.renderTask = page.render({ canvasContext, viewport });
      this.renderTask.promise.then(
        () => { this.renderTask = null; },
        (/* error */) => { this.renderTask = null; },
      );
    });
  };

  setElementSize = (element, sizes) => {
    element.style.width = `${sizes.width}px`;
    element.style.height = `${sizes.height}px`;
    element.height = sizes.height;
    element.width = sizes.width;
  };

  render () {
    const { contentRef, sizes } = this.props;
    const { error, loading } = this.state;

    return (
      <div className="thumbnail-pdf" ref={contentRef} style={{ position: 'relative', display: 'inline-block', minHeight: '46px' }}>
        <canvas width={sizes.width} height="0"/>
        {loading && <Loader active size="small"/>}
        {error && <Message error compact header="Ошибка" style={{ margin: 0, position: 'absolute', top: 0, left: 0, right: 0 }} title={error}/>}
      </div>
    );
  }
}

ThumbnailPdf.propTypes = {
  dossierFile: PropTypes.object.isRequired,
  contentRef: PropTypes.object.isRequired,
  sizes: PropTypes.shape({
    width: PropTypes.number.isRequired,
    height: PropTypes.number.isRequired,
  }).isRequired,
};

export default ThumbnailPdf;
