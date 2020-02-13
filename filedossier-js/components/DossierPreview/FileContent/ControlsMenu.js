import React from 'react';
import PropTypes from 'prop-types';
import { Input, Menu, Dropdown } from 'semantic-ui-react';
import DossierInfo from './DossierInfo';

const DEFAULT_SCALE_DELTA = 1.1;
const MIN_SCALE = 0.1;
const MAX_SCALE = 10;

export const getZoomInScale = (scaleNum) => {
  let newScale = (scaleNum * DEFAULT_SCALE_DELTA).toFixed(2);
  newScale = Math.ceil(newScale * 10) / 10;
  newScale = Math.min(MAX_SCALE, newScale);
  return newScale;
};

export const getZoomOutScale = (scaleNum) => {
  let newScale = (scaleNum / DEFAULT_SCALE_DELTA).toFixed(2);
  newScale = Math.floor(newScale * 10) / 10;
  newScale = Math.max(MIN_SCALE, newScale);
  return newScale;
};

export const calcScaleNum = ({ scale, rotate, containerSizes, elementSizes }) => {
  let scaleNum = scale;
  if (scale === 'pageActualOption') { scaleNum = 1.0; } else
  if (!scale || scale === 'pageWidthOption' || scale === 'pageFitOption') { // calc by container size
    let { width: elemWidth, height: elemHeight } = elementSizes;
    if (rotate % 180 !== 0) {
      [elemWidth, elemHeight] = [elemHeight, elemWidth]; // swap
    }
    scaleNum = containerSizes.width / elemWidth; // scale by width
    if (scaleNum * elemHeight > containerSizes.height) {
      if (scale === 'pageFitOption') {
        scaleNum = containerSizes.height / elemHeight; // scale by height
      } else {
        scaleNum = (containerSizes.width - 15) / elemWidth; // vertical scroll size
      }
    }
  }

  if (!Number(scaleNum)) { throw new Error(`Invalid scale value = ${scaleNum}`); }
  return scaleNum;
};

export const dragToScroll = (e) => {
  e.preventDefault();
  e.stopPropagation();
  if (document.activeElement) { document.activeElement.blur(); }
  const container = e.currentTarget;
  const startScrollTop = container.scrollTop || 0;
  const startscrollLeft = container.scrollLeft || 0;
  const startX = e.pageX;
  const startY = e.pageY;
  const target = e.currentTarget;
  target.style.cursor = 'grabbing';
  document.body.style.cursor = 'grabbing';

  document.onmousemove = (e) => {
    container.scrollTop = startScrollTop + startY - e.pageY;
    container.scrollLeft = startscrollLeft + startX - e.pageX;
  };

  document.onmouseup = () => {
    target.style.cursor = '';
    document.body.style.cursor = '';
    document.onmousemove = null;
    document.onmouseup = null;
  };
};

function ControlsMenu ({
  dossierFile,
  pdf, currentPage, setPage, pageText, setPageText,
  scaleValue, scaleNum, setScale,
  rotateFile, rotateLoading,
}) {
  return (
    <Menu inverted className="file-controls">
      <Menu.Item position="left" className="file-name" title={dossierFile.name}>
        <span>{dossierFile.name}</span>
      </Menu.Item>

      {pdf && pdf.numPages && currentPage && <Menu.Menu position="left">
        <Menu.Item link icon="triangle left" value={currentPage - 1} onClick={setPage} disabled={currentPage <= 1}/>
        <Menu.Item link icon="triangle right" value={currentPage + 1} onClick={setPage} disabled={currentPage >= pdf.numPages}/>
        <Menu.Item>
          <Input name="pageText"
            value={pageText || ''} style={{ width: '3rem' }}
            onChange={(e, { value }) => { setPageText((value || '').replace(/\D/g, '')); }}
            onFocus={(e) => { e.currentTarget.select(); }}
            onBlur={() => { if (pageText != currentPage) { setPageText(currentPage); } }} // eslint-disable-line eqeqeq
            onKeyDown={(e) => {
              if (e.keyCode === 13 && pageText && Number(pageText)) {
                setPage(null, { value: pageText });
              }
            }}
          />
          <span className="page-count-label">из {pdf.numPages}</span>
        </Menu.Item>
      </Menu.Menu>}

      {scaleNum && <Menu.Menu style={{ display: 'flex', margin: 'auto' }}>
        <Menu.Item link icon="minus" onClick={() => { setScale(getZoomOutScale(scaleNum)); }} disabled={scaleNum <= MIN_SCALE}/>
        <Menu.Item link icon="plus" onClick={() => { setScale(getZoomInScale(scaleNum)); }} disabled={scaleNum >= MAX_SCALE}/>
        <Menu.Item className="no-padding">
          <Dropdown item className="scale-selection" value={scaleValue || 'pageWidthOption'}
            text={scaleNum ? `${Math.round(scaleNum * 100)}%` : ''}
            onChange={(e, { value }) => { setScale(value); }}
            options={[
              { key: 'pageActualOption', value: 'pageActualOption', text: 'Реальный размер' },
              { key: 'pageWidthOption', value: 'pageWidthOption', text: 'По ширине страницы' },
              { key: 'pageFitOption', value: 'pageFitOption', text: 'По размеру страницы' },
              { key: '0.5', value: 0.5, text: '50%' },
              { key: '0.75', value: 0.75, text: '75%' },
              { key: '1', value: 1, text: '100%' },
              { key: '1.25', value: 1.25, text: '125%' },
              { key: '1.5', value: 1.5, text: '150%' },
              { key: '2', value: 2, text: '200%' },
              { key: '3', value: 3, text: '300%' },
              { key: '4', value: 4, text: '400%' },
            ]}
            selectOnNavigation={false}
            selectOnBlur={false}
          />
        </Menu.Item>
      </Menu.Menu>}

      <Menu.Menu position="right">
        {scaleNum && <Menu.Item link icon="undo" onClick={rotateFile.bind(this, -90)} disabled={!!rotateLoading}/>}
        {scaleNum && <Menu.Item link icon="undo" onClick={rotateFile.bind(this, 90)} disabled={!!rotateLoading} className="mirror-horizontal"/>}

        <Dropdown item icon="content" className="dossier-info">
          <Dropdown.Menu>
            <DossierInfo dossierFile={dossierFile}/>
          </Dropdown.Menu>
        </Dropdown>
      </Menu.Menu>
    </Menu>
  );
}

ControlsMenu.propTypes = {
  dossierFile: PropTypes.object.isRequired,
  pdf: PropTypes.object,
  currentPage: PropTypes.number,
  pageText: PropTypes.oneOfType([PropTypes.number, PropTypes.string]),
  setPage: PropTypes.func,
  setPageText: PropTypes.func,
  scaleValue: PropTypes.oneOfType([PropTypes.number, PropTypes.string]),
  scaleNum: PropTypes.number,
  setScale: PropTypes.func.isRequired,
  rotateFile: PropTypes.func.isRequired,
  rotateLoading: PropTypes.string,
};

export default ControlsMenu;
