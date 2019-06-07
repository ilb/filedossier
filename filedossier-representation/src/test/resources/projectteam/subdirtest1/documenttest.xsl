<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:fo="http://www.w3.org/1999/XSL/Format"
    exclude-result-prefixes="fo xsl"
    version="1.0">

    <xsl:template name="fopconf.xml">
        <!--
        The current directory for xsl:import, xsl:include, and the document() function is the directory containing the transform that uses them.
        путь в document() относительно текущего шаблона!
        -->
        <!--
        <fo:block background-color="yellow">
            document():<xsl:value-of select="document('../../fopconf.xml')"/>
        </fo:block>
        -->
    </xsl:template>

</xsl:stylesheet>
