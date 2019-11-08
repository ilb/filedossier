import React from 'react';
import PropTypes from 'prop-types';
import { Input, Menu, Dropdown } from 'semantic-ui-react';
import DossierInfo from './DossierInfo';

const DEFAULT_SCALE_DELTA = 1.1;
const MIN_SCALE = 0.1;
const MAX_SCALE = 10;

export const getZoomInScale = (scale) => {
  let newScale = (scale * DEFAULT_SCALE_DELTA).toFixed(2);
  newScale = Math.ceil(newScale * 10) / 10;
  newScale = Math.min(MAX_SCALE, newScale);
  return newScale;
};

export const getZoomOutScale = (scale) => {
  let newScale = (scale / DEFAULT_SCALE_DELTA).toFixed(2);
  newScale = Math.floor(newScale * 10) / 10;
  newScale = Math.max(MIN_SCALE, newScale);
  return newScale;
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
  query, dossierFile,
  pdf, pageNum, setPage, pageText, setPageText,
  scale, setScale,
  rotateFile,
}) {
  return (
    <Menu inverted className="file-controls">
      <Menu.Item position="left" className="file-name" title={dossierFile.name}>
        <span>{dossierFile.name}</span>
      </Menu.Item>

      {pdf && pdf.numPages && pageNum && <Menu.Menu position="left">
        <Menu.Item link icon="triangle left" value={pageNum - 1} onClick={setPage} disabled={pageNum <= 1}/>
        <Menu.Item link icon="triangle right" value={pageNum + 1} onClick={setPage} disabled={pageNum >= pdf.numPages}/>
        <Menu.Item>
          <Input name="pageText"
            value={pageText || ''} style={{ width: '3rem' }}
            onChange={(e, { value }) => { setPageText((value || '').replace(/\D/g, '')); }}
            onFocus={(e) => { e.currentTarget.select(); }}
            onBlur={() => { if (pageText != pageNum) { setPageText(pageNum); } }} // eslint-disable-line eqeqeq
            onKeyDown={(e) => {
              if (e.keyCode === 13 && pageText && Number(pageText)) {
                setPage(null, { value: pageText });
              }
            }}
          />
          <span className="page-count-label">из {pdf.numPages}</span>
        </Menu.Item>
      </Menu.Menu>}

      {scale && <Menu.Menu style={{ display: 'flex', margin: 'auto' }}>
        <Menu.Item link icon="minus" onClick={() => { setScale(getZoomOutScale(scale)); }} disabled={scale <= MIN_SCALE}/>
        <Menu.Item link icon="plus" onClick={() => { setScale(getZoomInScale(scale)); }} disabled={scale >= MAX_SCALE}/>
        <Menu.Item className="no-padding">
          <Dropdown item className="scale-selection" value={null}
            text={scale ? `${Math.round(scale * 100)}%` : ''}
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
        {scale && <Menu.Item link icon="undo" onClick={rotateFile.bind(this, -90)}/>}
        {scale && <Menu.Item link icon="undo" onClick={rotateFile.bind(this, 90)} className="mirror-horizontal"/>}

        <Dropdown item icon="content" className="dossier-info">
          <Dropdown.Menu>
            <DossierInfo query={query} dossierFile={dossierFile}/>
          </Dropdown.Menu>
        </Dropdown>
      </Menu.Menu>
    </Menu>
  );
}

ControlsMenu.propTypes = {
  query: PropTypes.object.isRequired,
  dossierFile: PropTypes.object.isRequired,
  pdf: PropTypes.object,
  pageNum: PropTypes.number,
  setPage: PropTypes.func,
  pageText: PropTypes.oneOfType([PropTypes.number, PropTypes.string]),
  setPageText: PropTypes.func,
  scale: PropTypes.number,
  setScale: PropTypes.func.isRequired,
  rotateFile: PropTypes.func.isRequired,
};

export default ControlsMenu;
