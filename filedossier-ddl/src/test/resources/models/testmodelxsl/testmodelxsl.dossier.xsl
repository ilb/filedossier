<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
    xmlns:xsl ="http://www.w3.org/1999/XSL/Transform"
    xmlns:exsl="http://exslt.org/common"
    extension-element-prefixes="exsl"
    exclude-result-prefixes="exsl xsl"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="urn:ru:ilb:filedossier:ddl ../../../../main/resources/schemas/filedossier/ddl.xsd"
    version="1.0">
    <xsl:output
        method = "xml"
        encoding = "UTF-8"
        media-type = "application/xml"
        indent = "no"
        omit-xml-declaration = "no"
    />

    <xsl:param name="dossierMode" />

    <xsl:template match="/">
        <package xmlns="urn:ru:ilb:filedossier:ddl">
            <code>testmodel</code>
            <name>Тестовый пакет досье</name>
            <dossier>
                <code>TEST</code>
                <name>Тестовое досье</name>
                <dossierFile>
                    <code>fairpricecalc</code>
                    <name>Тест имя</name>
                    <readonly>false</readonly>
                    <required>
                        <xsl:choose>
                            <xsl:when test="$dossierMode='mode1'">true</xsl:when>
                            <xsl:otherwise>false</xsl:otherwise>
                        </xsl:choose>
                    </required>
                    <hidden>false</hidden>
                    <allowedMultiple>true</allowedMultiple>
                    <variation>
                        <mediaType>application/xml</mediaType>
                        <representation>
                            <mediaType>application/vnd.oasis.opendocument.spreadsheet</mediaType>
                            <stylesheet>representations/fairpriceorder/content.xsl</stylesheet>
                            <template>representations/fairpriceorder/template.ods</template>
                            <schema></schema>
                        </representation>
                    </variation>
                </dossierFile>
                <dossierFile>
                    <code>jurnals</code>
                    <name>Test name 1</name>
                    <readonly>false</readonly>
                    <required>true</required>
                    <hidden>false</hidden>
                    <allowedMultiple>true</allowedMultiple>
                    <attribute>
                        <code>number</code>
                        <name>Номер документа</name>
                    </attribute>

                    <variation>
                        <mediaType>application/xml</mediaType>
                        <representation>
                            <mediaType>application/pdf</mediaType>
                            <stylesheet>https://devel.net.ilb.ru/meta/schemas/doctemplates/jurnals/percentsheet.xsd</stylesheet>
                            <schema>https://devel.net.ilb.ru/meta/stylesheets/doctemplates/jurnals/percentsheet.xsl</schema>
                            <meta>https://devel.net.ilb.ru/meta/&amp;uid=doctree:11f462ebdb14a5673ff41a5c75c5176552fad343:</meta>
                        </representation>
                    </variation>
                </dossierFile>
            </dossier>
        </package>
    </xsl:template>
</xsl:stylesheet>
