#!/bin/sh
set -e
umbrello5 --export png  --directory filedossier filedossier.xmi
xsltproc /usr/share/umbrello5/xmi2docbook.xsl filedossier.xmi  > filedossier/filedossier.docbook 
pandoc -s --from docbook --to html --output  filedossier/filedossier.html filedossier/filedossier.docbook
