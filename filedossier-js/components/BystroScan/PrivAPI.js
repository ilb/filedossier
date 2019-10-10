/* global privilegedAPI */
/* eslint-disable no-template-curly-in-string */
const PrivAPI = () => {};
// window.dump = (message) => { window.console.log(message); };

const myAlert = (message, showError) => { (showError || alert)(message); };

PrivAPI.getScanFilename = (fileid) => {
  let filename;
  if (navigator.userAgent.indexOf('Windows') > 0) { // mozilla only
    filename = '${TEMP}\\privilegedwebsite_${USERNAME}_scan_' + fileid + '.pdf';
  } else {
    filename = '${TMPDIR}/privilegedwebsite_${USER}_scan_' + fileid + '_0.pdf'; // "_0" для xsane который пытается сам нумеровать файлы
  }
  return filename;
};

PrivAPI.scanStart = ({ fileid, fileinputid, color, dpi, showError, showConfirm, onscanfinish }) => {
  let filename = '';
  let cmd = '';
  // TODO здесь интерфейс пользователя должен быть залочен
  window.console.log('bystroScan: scanStart called\n');
  if (!privilegedAPI || !privilegedAPI.availiable()) {
    myAlert('Действие недоступно. Отсканируйте документ и укажите отсканированный файл.', showError);
    return false;
  }
  filename = PrivAPI.getScanFilename(fileid);

  // проверяем на существование результат предыдущего сканирования
  privilegedAPI.fileExists(
    filename,
    (res) => {
      window.console.log('bystroScan: fileExistCallback called\n');
      if (navigator.userAgent.indexOf('Windows') > 0) { // mozilla only
        cmd = '"${ProgramFiles}\\BystroBank\\ILBStart\\BystroScan\\BystroScan.exe" -s ' + filename + (color ? ' -c ' + color : '') + (dpi ? ' -r ' + dpi : '');
      } else {
        // cmd = "${HOME}/mnt/x/public/BystroScan/scantools/scan.sh " + filename;
        cmd = '/opt/bystroscan/BystroScan -s ' + filename + (color ? ' -c ' + color : '') + (dpi ? ' -r ' + dpi : '');
      }
      const paOnscanfinish = (res1) => {
        window.console.log('bystroScan: onscanfinish called code="' + res1.code + '"\n');
        if (res1.code) {
          // document.getElementById(fileinputid).scanned=false;
          // зануляем поле
          const data = { id: fileinputid, value: '' };
          privilegedAPI.setElementValue(data,
            () => { // дождались. разлочиваем интерфейс и едем дальше
              myAlert('Ошибка сканирования', showError);
            }
          );
          if (onscanfinish) onscanfinish(data);
        } else {
          const val = res1.args[1];
          const data = { id: fileinputid, value: val };
          // document.getElementById(fileinputid).scanned=true;
          privilegedAPI.setElementValue(data, false);
          if (onscanfinish) onscanfinish(data);
        }
      };
      if (res.exists) {
        if (!showConfirm) {
          if (!confirm('Файл уже существует! Заменить?')) {
            return false; // отказ от повторного сканирования. разлочиваем интерфейс
          }
        }
        new Promise((resolve, reject) => {
          if (showConfirm) {
            showConfirm('Файл уже существует! Заменить?', (result) => { (result ? resolve : reject)(); });
          } else {
            return resolve();
          }
        }).then(() => {
          // удаляем старый файл
          privilegedAPI.deleteFile({ filename }, () => {
            privilegedAPI.exec(cmd, paOnscanfinish); // запускаем программу сканирования
          });
        }).catch(() => {});
      } else {
        privilegedAPI.exec(cmd, paOnscanfinish); // запускаем программу сканирования
      }
      // никого тут не ждем. колбэк указан пустой сканирование живет своей жизнью. разлочиваем интерфейс
      return false;
    }
  );
  return false;
};

// заполнение элемента <input type="file" id="fileInputId"> файлом fullFileName
PrivAPI.fileExists = (fullFileName, cbFileExist) => {
  if (privilegedAPI && privilegedAPI.availiable()) {
    // проверяем на существование результат сканирования
    privilegedAPI.fileExists(fullFileName, cbFileExist || false);
  }
  return false;
};


// идем по таймауту читать BystroScan.ini
PrivAPI.readScanFiles = () => {
  return new Promise((resolve, reject) => {
    let iniPath = '';
    if (navigator.userAgent.indexOf('Windows') > 0) { // mozilla only
      iniPath = '${APPDATA}\\BystroScan.ini';
    } else {
      iniPath = '${HOME}/.config/BystroScan.ini';
    }

    privilegedAPI.fileExists(
      iniPath,
      (res) => {
        if (res.exists) {
          // прочитать содержимое файла
          privilegedAPI.getFileContents({ filename: iniPath, charset: 'UTF-8' },
            (res1) => {
              if (res1 && res1.content) {
                const scans = res1.content.split('\n')
                  .filter(line => line && (/^image[0-9]{1,}=/).test(line)) // filter scans lines
                  .map(line => { // parse line
                    const parts = line.replace(/^image[0-9]{1,}=/, '').split('?');
                    return { path: parts[0], angle: parts[1] };
                  });
                resolve(scans);
              } else {
                reject('Ошибка чтения конфига БыстроСкана или файл пуст');
              }
            },
            error => {
              window.console.log('Ошибка чтения конфига БыстроСкана', error);
              reject('Ошибка чтения конфига БыстроСкана', error);
            },
          );
        } else {
          reject('Конфиг БыстроСкана не найден');
        }
      },
      error => {
        window.console.log('Конфиг БыстроСкана не найден', error);
        reject('Конфиг БыстроСкана не найден', error);
      },
    );
  });
};

export default PrivAPI;
