var doc = null;
if (!process.browser) {
  const fs = require('fs');
  const path = require('path');
  const DOMParser = require('xmldom').DOMParser;

  const configpath = path.resolve(path.join(process.env.HOME, '.config/context.xml'));

  if (fs.existsSync(configpath)) {
    const xml = fs.readFileSync(configpath, 'utf8');
    doc = new DOMParser().parseFromString(xml);
  }
}

module.exports = {
  lookup: function (name) {
    return doc === null ? null : require('xpath').select1("//Environment[@name='" + name + "']/@value", doc).value;
  },
};
