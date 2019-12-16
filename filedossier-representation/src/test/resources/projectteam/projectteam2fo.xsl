<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed to the Apache Software Foundation (ASF) under one or more
  contributor license agreements.  See the NOTICE file distributed with
  this work for additional information regarding copyright ownership.
  The ASF licenses this file to You under the Apache License, Version 2.0
  (the "License"); you may not use this file except in compliance with
  the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
-->
<!-- $Id$ -->
<xsl:stylesheet
    xmlns:pt="urn:fop:examples:embedding:xml:xml:projectteam"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:fo="http://www.w3.org/1999/XSL/Format"
    exclude-result-prefixes="fo pt"
    version="1.0">

    <xsl:import href="subdirtest1/imagetest.xsl" />
    <xsl:import href="subdirtest1/documenttest.xsl" />
    <xsl:output method="xml" version="1.0" omit-xml-declaration="no" indent="yes"/>
    <xsl:param name="versionParam" select="'1.0'"/>

    <!-- ========================= -->
    <!-- root element: projectteam -->
    <!-- ========================= -->
    <xsl:template match="pt:projectteam">
        <fo:root font-family="serif">
            <fo:layout-master-set>
                <fo:simple-page-master master-name="simpleA4" page-height="29.7cm" page-width="21cm" margin-top="2cm" margin-bottom="2cm" margin-left="2cm" margin-right="2cm">
                    <fo:region-body/>
                    <fo:region-after region-name="xsl-region-after"/>
                </fo:simple-page-master>
            </fo:layout-master-set>
            <fo:declarations>
                <x:xmpmeta xmlns:x="adobe:ns:meta/">
                    <rdf:RDF xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<!--                        <rdf:Description rdf:about=""
                                         xmlns:dc="http://purl.org/dc/elements/1.1/">
                            <dc:title>Document title</dc:title>
                            <dc:creator>Document author</dc:creator>
                            <dc:description>Document subject</dc:description>
                        </rdf:Description>-->
                        <rdf:Description rdf:about=""
                                         xmlns:xmp="http://ns.adobe.com/xap/1.0/">
                            <!-- XMP properties go here -->
                            <xmp:docId><xsl:value-of select="/pt:projectteam/pt:docId"/></xmp:docId>
                        </rdf:Description>
                    </rdf:RDF>
                </x:xmpmeta>
            </fo:declarations>
            <fo:page-sequence master-reference="simpleA4">
                <fo:static-content flow-name="xsl-region-after" display-align="after">
                    <fo:block>
                        <fo:table table-layout="fixed" width="180mm">
                            <fo:table-column column-width="27mm"/>
                            <fo:table-column column-width="33mm"/>
                            <fo:table-column column-width="80mm"/>
                            <fo:table-column column-width="40mm"/>
                            <fo:table-body>
                                <fo:table-row>
                                    <fo:table-cell text-align="center"
                                                   border-right-style="solid"
                                                   border-right-width="thin"
                                                   border-top-style="solid"
                                                   display-align="before">
                                        <fo:block>
                                            Company Logo
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell text-align="center" border-top-style="solid" height="6mm" display-align="before">
                                        <fo:block>
                                            123-00-456
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell border-top-style="solid" display-align="before">
                                        <fo:block>
                                                        &#160;
                                            <fo:instream-foreign-object>
                                                <barcode:barcode message="{/pt:projectteam/pt:docId}-#page-number#" xmlns:barcode="http://barcode4j.krysalis.org/ns">
                                                    <barcode:datamatrix>
                                                        <barcode:module-width>0.75mm</barcode:module-width>
                                                        <barcode:shape>force-rectangle</barcode:shape>
                                                    </barcode:datamatrix>
                                                </barcode:barcode>
                                            </fo:instream-foreign-object>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell border-left-style="solid" border-left-width="thin" border-top-style="solid" display-align="before">
                                        <fo:block start-indent="3em">
                                            page <fo:page-number/>
                                            <!--                                             of
                                            <fo:page-number-citation-last/>-->
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                            </fo:table-body>
                        </fo:table>

                    </fo:block>
                </fo:static-content>
                <fo:flow flow-name="xsl-region-body">
                    <fo:block font-size="16pt" font-weight="bold" space-after="5mm">Project: <xsl:value-of select="pt:projectname"/>
                    </fo:block>
                    <fo:block font-size="12pt" space-after="5mm">Version <xsl:value-of select="$versionParam"/>
                    </fo:block>
                    <fo:block font-size="10pt">
                        <fo:table table-layout="fixed" width="100%" border-collapse="separate">
                            <fo:table-column column-width="4cm"/>
                            <fo:table-column column-width="4cm"/>
                            <fo:table-column column-width="5cm"/>
                            <fo:table-body>
                                <xsl:apply-templates select="pt:member"/>
                            </fo:table-body>
                        </fo:table>
                    </fo:block>
                    <xsl:call-template name="fopconf.xml"/>
                    <xsl:call-template name="big.jpg"/>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
    <!-- ========================= -->
    <!-- child element: member     -->
    <!-- ========================= -->
    <xsl:template match="pt:member">
        <fo:table-row>
            <xsl:if test="function = 'lead'">
                <xsl:attribute name="font-weight">bold</xsl:attribute>
            </xsl:if>
            <fo:table-cell>
                <fo:block>
                    <xsl:value-of select="pt:name"/>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell>
                <fo:block>
                    <xsl:value-of select="pt:function"/>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell>
                <fo:block>
                    <xsl:value-of select="pt:email"/>
                </fo:block>
            </fo:table-cell>
        </fo:table-row>
    </xsl:template>
</xsl:stylesheet>
