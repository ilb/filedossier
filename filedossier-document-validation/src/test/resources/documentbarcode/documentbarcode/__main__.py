# -*- coding: utf-8 -*-
"""
Created on Fri Jun 14 12:53:54 2019

@author: kudrin

Usage:
cat <json_file> | python __main__.py

Dependeicies:

pdf2image
pylibdmtx

"""

import sys
import select
import json

from document_barcode_reader import DocumentBarcodeReader


def main():
    if select.select([sys.stdin], [], [], 0.0)[0]:
        j_doc = json.load(sys.stdin)
    else:
        print(j_doc, file=sys.stderr)
        raise Exception('Pipe is empty.')

    reader = DocumentBarcodeReader(j_doc)
    print(json.dumps(reader.get_barcodes()))


if __name__ == '__main__':
    main()
