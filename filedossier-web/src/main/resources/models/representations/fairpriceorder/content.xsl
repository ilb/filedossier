<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.1"
                xmlns:test="urn:ru:ilb:loancalculator:test"
                xmlns:xsl ="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs ="http://www.w3.org/2001/XMLSchema"
                xmlns:fn ="http://www.w3.org/2005/xpath-functions"
                xmlns:udf ="http://user.defined.functions"
                xmlns:table ="urn:oasis:names:tc:opendocument:xmlns:table:1.0"
                xmlns:text ="urn:oasis:names:tc:opendocument:xmlns:text:1.0"
                xmlns:office="urn:oasis:names:tc:opendocument:xmlns:office:1.0"
                xmlns:view="urn:ru:ilb:loancalculator:view"
                xmlns:exsl="http://exslt.org/common"
                extension-element-prefixes="exsl"
                exclude-result-prefixes="exsl view">

    <xsl:output method               = "xml"
                encoding             = "UTF-8"
                media-type           = "application/xml"
                indent               = "no"
                omit-xml-declaration = "no"
    />

    <xsl:variable name="data" select="document('data.xml')/root"/>

    <!-- identity template -->
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>
    <xsl:template match="table:table-cell" mode="replace">
        <xsl:param name="value"/>
        <xsl:copy>
            <xsl:apply-templates select="@*[local-name(.)!='value']"/>
            <xsl:attribute name="office:value">
                <xsl:value-of select="$value"/>
            </xsl:attribute>
            <text:p>
                <xsl:value-of select="$value"/>
            </text:p>
        </xsl:copy>
    </xsl:template>
    <!-- recalc all formulas -->
    <xsl:template match="table:table-cell[@table:formula]">
        <xsl:copy>
            <xsl:apply-templates select="@*[local-name(.)!='value' and local-name(.)!='string-value']"/>
        </xsl:copy>
    </xsl:template>


    <xsl:template match="table:table[@table:name='1 уровень']/table:table-row[position()=2]/table:table-cell[position()=3]">
        <xsl:apply-templates select="." mode="replace">
            <xsl:with-param name="value" select="$data/date"/>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="table:table[@table:name='1 уровень']/table:table-row[position()=15]/table:table-cell[position()=5]">
        <xsl:apply-templates select="." mode="replace">
            <xsl:with-param name="value" select="$data/fairPrice"/>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="table:table[@table:name='1 уровень']/table:table-row[position()=14]/table:table-cell[position()=5]">
        <xsl:apply-templates select="." mode="replace">
            <xsl:with-param name="value" select="$data/tradingVolume"/>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="table:table[@table:name='1 уровень']/table:table-row[position()=13]/table:table-cell[position()=5]">
        <xsl:apply-templates select="." mode="replace">
            <xsl:with-param name="value" select="$data/countDeals"/>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="table:table[@table:name='1 уровень']/table:table-row[position()=12]/table:table-cell[position()=5]">
        <xsl:apply-templates select="." mode="replace">
            <xsl:with-param name="value" select="$data/countDays"/>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="table:table[@table:name='1 уровень']/table:table-row[position()=6]/table:table-cell[position()=3]">
        <xsl:apply-templates select="." mode="replace">
            <xsl:with-param name="value" select="$data/initialVolume"/>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="table:table[@table:name='Данные']/table:table-row[position() >= 4 and not(position() > 33)]">
        <xsl:copy>
            <xsl:apply-templates select="*">
                <xsl:with-param name="row" select="position()-17"/>
            </xsl:apply-templates>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="table:table[@table:name='Данные']/table:table-row[position() >= 4 and not(position() > 33)]/table:table-cell[position()=3]">
        <xsl:param name="row"/>
            <xsl:apply-templates select="." mode="replace">
                <xsl:with-param name="value" select="$data/marketData[position()=$row]/countDeals"/>
      <!-- <xsl:with-param name="value" select="$row"/> -->
            </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="table:table[@table:name='Данные']/table:table-row[position() >= 4 and not(position() > 33)]/table:table-cell[position()=4]">
        <xsl:param name="row"/>
            <xsl:apply-templates select="." mode="replace">
                <xsl:with-param name="value" select="$data/marketData[position()=$row]/tradingVolume"/>
      <!-- <xsl:with-param name="value" select="$row"/> -->
            </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="table:table[@table:name='Данные']/table:table-row[position() >= 4 and not(position() > 33)]/table:table-cell[position()=5]">
        <xsl:param name="row"/>
            <xsl:apply-templates select="." mode="replace">
                <xsl:with-param name="value" select="$data/marketData[position()=$row]/weightedAverage"/>
      <!-- <xsl:with-param name="value" select="$row"/> -->
            </xsl:apply-templates>
    </xsl:template>
</xsl:stylesheet>
