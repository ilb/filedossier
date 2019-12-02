<?xml version="1.0"?>
  <xsl:stylesheet
      xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
      xmlns="http://www.w3.org/1999/xhtml"
      xmlns:xpil="http://www.together.at/2006/XPIL1.0"
      xmlns:xpdl="http://www.wfmc.org/2002/XPDL1.0"
      exclude-result-prefixes="xsl xpil xpdl"
      version="1.0">

  <xsl:output
      media-type="application/xhtml+xml"
      method="xml"
      encoding="UTF-8"
      indent="yes"
      omit-xml-declaration="no"
      doctype-public="-//W3C//DTD XHTML 1.1//EN"
      doctype-system="http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd" />

  <xsl:template match="/">
    <xsl:variable name="hyperlink">
      <xsl:value-of select="root/data/link"/>
    </xsl:variable>
    <html>
      <head>
       <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
       <title>Таблица</title>
       <style type="text/css">
        table {
         width: auto;
         border-collapse: collapse;
        }
        td, th {
         padding: 3px;
         border: 1px solid black;
        }
       </style>
      </head>
      <body>
        <div>
          <table>
            <caption></caption>
            <tr>
              <th>Время запроса</th>
              <th>
                <xsl:value-of select="root/requestTime"/>
              </th>
            </tr>
            <tr>
              <th>w_rec</th>
              <th><xsl:value-of select="root/RequestResult/records/w_rec"/></th>
            </tr>
            <tr>
              <th>Регион</th>
              <th><xsl:value-of select="root/RequestResult/records/w_reg_inic"/></th>
            </tr>
            <tr>
              <th>Кузов</th>
              <th><xsl:value-of select="root/RequestResult/records/w_kuzov"/></th>
            </tr>
            <tr>
              <th>Модель</th>
              <th><xsl:value-of select="root/RequestResult/records/w_model"/></th>
            </tr>
            <tr>
              <th>w_data_pu</th>
              <th><xsl:value-of select="root/RequestResult/records/w_data_pu"/></th>
            </tr>
            <tr>
              <th>VIN</th>
              <th><xsl:value-of select="root/RequestResult/records/w_vin"/></th>
            </tr>
            <tr>
              <th>Год</th>
              <th><xsl:value-of select="root/RequestResult/records/w_god_vyp"/></th>
            </tr>
            <tr>
              <th>w_vid_uch</th>
              <th><xsl:value-of select="root/RequestResult/records/w_vid_uch"/></th>
            </tr>
            <tr>
              <th>w_un_gic</th>
              <th><xsl:value-of select="root/RequestResult/records/w_un_gic"/></th>
            </tr>
            <tr>
              <th>Имя хоста</th>
              <th><xsl:value-of select="root/hostname"/></th>
            </tr>
            <tr>
              <th>VIN</th>
              <th><xsl:value-of select="root/vin"/></th>
            </tr>
          </table>

        </div>
      </body>
    </html>
  </xsl:template>
</xsl:stylesheet>
