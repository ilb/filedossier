#!/bin/bash
set -e
xsltproc projectteam2fo.xsl projectteam.xml > projectteam.fo
curl -d @projectteam.fo -o projectteam.pdf -X POST -H "Content-Type: application/xml" "http://devel.net.ilb.ru:8080/pdfgen/fopservlet"
#?xslt=https://devel.net.ilb.ru/meta/stylesheets/doctemplates/primary/loantreaty/credits/auto/V20140707.xsl&xsd=https://devel.net.ilb.ru/meta/schemas/doctemplates/primary/loantreaty/credits/auto/V20140707.xsd&metaUrl=https://devel.net.ilb.ru/meta/&uid=doctree:11f462ebdb14a5673ff41a5c75c5176552fad343:"