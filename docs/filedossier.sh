#!/bin/sh
set -e
FILE=`basename -s .sh $0`
umbrello5 --export png  ${FILE}.xmi
xsltproc --nonet /usr/share/umbrello5/xmi2docbook.xsl ${FILE}.xmi  > ${FILE}.docbook
pandoc -s --toc --from docbook --to html --output  ${FILE}.html ${FILE}.docbook
