# -*- coding: utf-8 -*-
"""
Created on Fri Jun 21 10:30:18 2019

@author: kaa
"""

import pdf2image
from pylibdmtx.pylibdmtx import decode


class DocumentBarcodeReader:
	_DEFAULT_DPI = 200	
	
	def __init__(self, j_doc, dpi=_DEFAULT_DPI):
		self.j_doc = j_doc
		self.dpi = dpi
		
	def get_barcodes(self):
		images = pdf2image.convert_from_path(self.j_doc["source"]["document"], dpi=self.dpi)
		area = self.j_doc["source"]["barcode"]["area"]
		outdir = self._getOutputDirectory(self.j_doc)

		barcodeimages = []
		i = 0

		for image in images:
			i = i + 1
			
			text = decode(image.crop(self._getBarcodeArea(area, image.size)))
			file_path = outdir + self._getFilePrefix() + str(i) + ".png"
	
			if len(text) == 1:
				barcode = (text[0].data).decode("utf-8")
			else:
				barcode = None

			barcodeimages.append({"file": file_path, "barcode": barcode})

			if len(outdir) != 0:
				image.save(file_path)

		return {"pages": barcodeimages}


	# Возвращает имя каталога, при необходимости добавляя завершающий '/'.
	# Если имя не задано, то пустую строку.
	def _getOutputDirectory(self, j_doc):
		try:
			outdir = j_doc["pages"]["directory"]
		except:
			outdir = ""

		if (len(outdir) != 0):
			if not outdir.endswith("/"):
				outdir = outdir + "/"

		return outdir


	# Возвращает область поиска штрихкода в пикселях.
	# area - область поиска в %% от ширины и высоты изображения.
	# image_size - размер изображения в пикселях.
	def _getBarcodeArea(self, area, image_size):
		w, h = image_size
		return (int(area["left"] * w / 100), int(area["top"] * h / 100), int(area["right"] * w / 100), int(area["bottom"] * h / 100))


	def _getFilePrefix(self):
		try:
			file_prefix = self.j_doc["pages"]["prefix"]
		except:
			file_prefix = ""

		return file_prefix
