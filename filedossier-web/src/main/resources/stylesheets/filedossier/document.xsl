<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:xsd="http://www.w3.org/2001/XMLSchema"
    xmlns:view="urn:ru:ilb:${rootArtifactId}:view"
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
    <xsl:param name="absolute.path"/>
    <xsl:param name="base.path"/>
    <xsl:param name="relative.path"/>
    <xsl:param name="xslt.template"/>    
    
    <xsl:param name="documentId"/>
    <xsl:strip-space elements="*" />

    <xsl:template match="/">
        <html xml:lang="ru">
            <head>
                <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
                <title>Document</title>
            </head>
            <body>
                <h1>Document <xsl:value-of select="$documentId"/></h1>
                <xsl:apply-templates />
                
                
                <table border="1">
                    <caption>xslt params</caption>
                    <tr>
                        <td>absolute.path</td>
                        <td><xsl:value-of select="$absolute.path"/></td>
                    </tr>
                    <tr>
                        <td>base.path</td>
                        <td><xsl:value-of select="$base.path"/></td>
                    </tr>
                    <tr>
                        <td>relative.path</td>
                        <td><xsl:value-of select="$relative.path"/></td>
                    </tr>
                    <tr>
                        <td>xslt.template</td>
                        <td><xsl:value-of select="$xslt.template"/></td>
                    </tr>
                    
                </table>
                
            </body>
        </html>
    </xsl:template>
    <xsl:template match="view:document">
        <table border="1">
            <tr>
                <td>id</td>
                <td><xsl:value-of select="view:id"/></td>
            </tr>
            <tr>
                <td>displayName</td>
                <td><xsl:value-of select="view:displayName"/></td>
            </tr>
            <tr>
                <td>description</td>
                <td><xsl:value-of select="view:description"/></td>
            </tr>
            <tr>
                <td>docDate</td>
                <td><xsl:value-of select="view:docDate"/></td>
            </tr>
            
        </table>
    </xsl:template>
    
</xsl:stylesheet>
