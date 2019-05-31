<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" 
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
                xmlns="http://www.w3.org/1999/xhtml">

    <xsl:param name="absolute.path"/>
    <xsl:param name="base.path"/>
    <xsl:param name="relative.path"/>
    <xsl:param name="xslt.template"/>    

    <xsl:template name="css_and_js_includes">
        <xsl:variable name="docroot" select="substring-before($base.path,'/web/')"/>
        <xsl:variable name="webjars.path" select="concat($docroot,'/webjars')"/>
    
        <link rel="stylesheet" type="text/css" href="{$webjars.path}/Semantic-UI/2.4.1/semantic.min.css"/>

        <link rel="stylesheet" type="text/css" href="{$docroot}css/styles.css"/>
        <!--        <script type="application/javascript" src="{$docroot}jquery/jquery-3.3.1.min.js">
            <xsl:text><![CDATA[]]></xsl:text>
        </script>
        <script type="application/javascript" src="{$docroot}semantic-ui/semantic.min.js">
            <xsl:text><![CDATA[]]></xsl:text>
        </script>-->
    </xsl:template>
    <xsl:template name="debug_xslt_params">
        <table border="1">
            <caption>xslt params</caption>
            <tr>
                <td>absolute.path</td>
                <td>
                    <xsl:value-of select="$absolute.path"/>
                </td>
            </tr>
            <tr>
                <td>base.path</td>
                <td>
                    <xsl:value-of select="$base.path"/>
                </td>
            </tr>
            <tr>
                <td>relative.path</td>
                <td>
                    <xsl:value-of select="$relative.path"/>
                </td>
            </tr>
            <tr>
                <td>xslt.template</td>
                <td>
                    <xsl:value-of select="$xslt.template"/>
                </td>
            </tr>

        </table>
    </xsl:template>
</xsl:stylesheet>
