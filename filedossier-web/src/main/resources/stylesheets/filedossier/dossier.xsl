<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:xsd="http://www.w3.org/2001/XMLSchema"
    xmlns:view="urn:ru:ilb:filedossier:view"
    exclude-result-prefixes="xsl xsd"
    version="1.0">

    <xsl:output
        media-type="application/xhtml+xml"
        method="xml"
        encoding="UTF-8"
        indent="yes"
        omit-xml-declaration="no"
        doctype-public="-//W3C//DTD XHTML 1.1//EN"
        doctype-system="http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd" />

    <xsl:include href="common.xsl"/>
    
    <xsl:strip-space elements="*" />

    <xsl:template match="/">
        <html xml:lang="ru">
            <head>
                <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
                <title>Досье</title>
                <xsl:call-template name="css_and_js_includes"/>
            </head>
            <body>
                <xsl:apply-templates />
            </body>
        </html>
    </xsl:template>
    <xsl:template match="view:dossier">
        <xsl:variable name="dossierCode" select="view:code"/>
        <table class="ui table">
            <caption>Досье <xsl:value-of select="view:name"/></caption>
            <tr>
                <th>Файл</th>
                <th>Действия</th>
            </tr>
            <xsl:for-each select="view:dossierFile">
                <tr>
                    <td>
                        <xsl:value-of select="view:name"/>
                        <xsl:if test="view:exists='true'">
                            <a href="{$dossierCode}/dossierfiles/{view:code}">
                                <i class="ui icon download"/>
                            </a>
                        </xsl:if>
                    </td>
                    <td>
                        
                    </td>
                </tr>
            </xsl:for-each>
        </table>
    </xsl:template>
    
</xsl:stylesheet>
