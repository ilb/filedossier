<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:fo="http://www.w3.org/1999/XSL/Format"
    exclude-result-prefixes="fo xsl"
    version="1.0">

    <xsl:template name="big.jpg">
        <fo:block text-align="right">
            <!--
            путь в src относительно xml-документа, к которому применяется xsl, не самого xsl-шаблона!
            -->
            <fo:external-graphic
                src="url('../projectteam/images/big.jpg')"
                content-height="scale-to-fit"
                content-width="scale-to-fit"
                scaling="uniform"
                width="10cm" />
        </fo:block>
    </xsl:template>

</xsl:stylesheet>
