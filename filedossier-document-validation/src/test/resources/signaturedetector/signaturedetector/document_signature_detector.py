import cv2
import numpy as np
import sizes


class DocumentSignatureDetector:
	_DPI = 300

	def __init__(self, j_doc):
		self.j_doc = j_doc

	def find_signatures(self):
		doc = cv2.imread(self.j_doc["pdf_page"])
		scan = cv2.imread(self.j_doc["scan_page"])

		self._DPI = sizes.calc_page_dpi(scan.shape[:2])

		matched = self._match_scanned_page(doc, scan)
		# self._display_image(matched)

		difference_contours = self._diff_pages(doc, matched)
		color_contours = self._test_color(matched)

		signature_rectangles = self._create_signatures_rectangles(self.j_doc)
		signatures_mask = self._create_rect_mask(doc.shape, signature_rectangles)
		# self._display_image(signatures_mask)

		color_mask = self._create_contours_mask(doc.shape, color_contours)
		difference_mask = self._create_contours_mask(doc.shape, difference_contours)

		# self._display_image(cv2.bitwise_and(signatures_mask, color_mask))
		# self._display_image(cv2.bitwise_and(signatures_mask, difference_mask))

		signatures_checking = []

		# Заполняем по найденным цветным элементам.
		img = cv2.bitwise_and(signatures_mask, color_mask)
		for s in signature_rectangles:
			x, y, w, h = s
			crop_img = img[y:y + h, x:x + w]

			if np.sum(crop_img) != 0:
				signatures_checking.append({"detected": True})
			else:
				signatures_checking.append({"detected": False})

		# Дополняем по разности изображений.
		img = cv2.bitwise_and(signatures_mask, difference_mask)
		for i, s in enumerate(signature_rectangles):
			x, y, w, h = s
			crop_img = img[y:y + h, x:x + w]

			if np.sum(crop_img) != 0:
				signatures_checking[i] = {"detected": True}

		return {"signatures": signatures_checking}

	def _display_image(self, img):
		cv2.namedWindow("img", cv2.WINDOW_NORMAL)
		cv2.resizeWindow("img", 600, 800);
		cv2.imshow("img", img)
		cv2.waitKey()

	def _match_scanned_page(self, document, scanned):
		obj = document
		img = scanned

		img = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
		cv2.adaptiveThreshold(img, 255, cv2.ADAPTIVE_THRESH_GAUSSIAN_C, cv2.THRESH_BINARY, 5, 15, img)

		obj = cv2.GaussianBlur(obj, (15, 15), 0)
		img = cv2.GaussianBlur(img, (15, 15), 0)

		OBJ_RESIZE_RATIO = 1.5
		IMG_RESIZE_RATIO = 1.3

		h, w, _ = obj.shape
		if w > 800:
			OBJ_RESIZE_RATIO = w / 800

		h, w = img.shape
		if w > 800:
			IMG_RESIZE_RATIO = w / 800

		RESIZE_RATIO = OBJ_RESIZE_RATIO if OBJ_RESIZE_RATIO < IMG_RESIZE_RATIO else IMG_RESIZE_RATIO

		h, w, _ = obj.shape
		obj = cv2.resize(obj, (int(w / RESIZE_RATIO), int(h / RESIZE_RATIO)))

		h, w = img.shape
		img = cv2.resize(img, (int(w / RESIZE_RATIO), int(h / RESIZE_RATIO)))

		try:
			hom = self._get_homography(obj, img)
		except:
			return np.zeros(document.shape, np.uint8)

		s = np.identity(3, dtype=float)
		s[0, 0] = RESIZE_RATIO
		s[1, 1] = RESIZE_RATIO

		h2 = np.dot(np.dot(s, hom), np.linalg.inv(s))
		h, w, _ = document.shape

		img1_warp = cv2.warpPerspective(scanned, h2, (w, h), flags=cv2.INTER_LINEAR | cv2.WARP_INVERSE_MAP)
		return img1_warp

	def _diff_pages(self, doc, matched):
		ITERATIONS = 6

		m1 = doc
		m2 = matched

		m1 = cv2.cvtColor(m1, cv2.COLOR_BGR2GRAY)
		m2 = cv2.cvtColor(m2, cv2.COLOR_BGR2GRAY)

		cv2.adaptiveThreshold(m1, 255, cv2.ADAPTIVE_THRESH_GAUSSIAN_C, cv2.THRESH_BINARY_INV, 5, 15, m1)
		cv2.adaptiveThreshold(m2, 255, cv2.ADAPTIVE_THRESH_GAUSSIAN_C, cv2.THRESH_BINARY_INV, 5, 15, m2)

		kernel = np.ones((3, 3), np.uint8)

		m1 = cv2.dilate(m1, kernel, iterations=ITERATIONS)
		m2 = cv2.dilate(m2, kernel, iterations=ITERATIONS)

		combined = cv2.bitwise_xor(m1, m2)

		combined = cv2.erode(combined, kernel, iterations=ITERATIONS)

		if cv2.getVersionMajor() == 3:
			# OpenCV 3.4
			_, contours, _ = cv2.findContours(combined, cv2.RETR_LIST, cv2.CHAIN_APPROX_SIMPLE)
		else:
			# OpenCV 4.1.0
			contours, _ = cv2.findContours(combined, cv2.RETR_LIST, cv2.CHAIN_APPROX_SIMPLE)

		combined = cv2.cvtColor(combined, cv2.COLOR_GRAY2BGR)

		good_contours = []

		for i, k in enumerate(contours):
			_, _, w, h = cv2.boundingRect(k)

			if w > sizes.mmToPix(3, self._DPI) and h > sizes.mmToPix(4, self._DPI):
				combined = cv2.drawContours(combined, contours, i, (0, 0, 255), cv2.FILLED)
				good_contours.append(k)

		return good_contours

	def _test_color(self, img):
		b = cv2.bitwise_not(cv2.extractChannel(img, 0))
		g = cv2.bitwise_not(cv2.extractChannel(img, 1))
		r = cv2.bitwise_not(cv2.extractChannel(img, 2))

		cv2.threshold(b, 64, 255, cv2.THRESH_BINARY, dst=b)
		cv2.threshold(g, 64, 255, cv2.THRESH_BINARY, dst=g)
		cv2.threshold(r, 64, 255, cv2.THRESH_BINARY, dst=r)

		sign = cv2.bitwise_or(cv2.bitwise_or(cv2.bitwise_xor(b, r), cv2.bitwise_xor(b, g)), cv2.bitwise_xor(r, g))

		sign = cv2.GaussianBlur(sign, (11, 11), 0.0);

		cv2.threshold(sign, 16, 255, cv2.THRESH_BINARY, dst=sign)

		if cv2.getVersionMajor() == 3:
			# OpenCV 3.4.0
			_, contours, _ = cv2.findContours(sign, cv2.RETR_LIST, cv2.CHAIN_APPROX_SIMPLE)
		else:
			# OpenCV 4.1.0
			contours, _ = cv2.findContours(sign, cv2.RETR_LIST, cv2.CHAIN_APPROX_SIMPLE)

		combined = cv2.cvtColor(sign, cv2.COLOR_GRAY2BGR)
		good_contours = []

		for i, k in enumerate(contours):
			x, y, w, h = cv2.boundingRect(k)

			if w > sizes.mmToPix(3, self._DPI) and h > sizes.mmToPix(5, self._DPI):
				combined = cv2.drawContours(combined, contours, i, (0, 0, 255), thickness=cv2.FILLED)
				good_contours.append(k)

		return good_contours

	def _create_signatures_rectangles(self, j_doc):
		signatures = []

		dpi = self._DPI

		for s in j_doc["signatures"]:
			left = sizes.mmToPix(s["left"], dpi)
			top = sizes.mmToPix(s["top"], dpi)
			right = sizes.mmToPix(s["right"], dpi)
			bottom = sizes.mmToPix(s["bottom"], dpi)

			rectangle = (int(left), int(top), int(right-left), int(bottom-top))

			signatures.append(rectangle)

		return signatures

	def _create_rect_mask(self, size, rectangles):
		zero = np.zeros(size, np.uint8)

		for rect in rectangles:
			x, y, w, h = rect
			cv2.rectangle(zero, (int(x), int(y)), (int(x+w), int(y+h)), (255, 255, 255), cv2.FILLED)

		return zero

	def _create_contours_mask(self, size, contours):
		rectangles = []

		for contour in contours:
			rect = cv2.boundingRect(contour)
			rectangles.append(rect)

		zero = self._create_rect_mask(size, rectangles)
		return zero

	def _get_homography(self, obj, img):
		MIN_MATCH_RATION = 0.1

		min_hessian = 400
		detector = cv2.xfeatures2d.SURF_create(min_hessian)

		keypoints_object, descriptors_object = detector.detectAndCompute(obj, ())
		keypoints_scene, descriptors_scene = detector.detectAndCompute(img, ())

		matcher = cv2.DescriptorMatcher.create(1)
		knn_matches = matcher.knnMatch(descriptors_object, descriptors_scene, k=2)

		# store all the good matches as per Lowe's ratio test.
		good_matches = []
		ratio_thresh = 0.75

		for m, n in knn_matches:
			if m.distance < ratio_thresh * n.distance:
				good_matches.append(m)

		if len(knn_matches) == 0:
			raise Exception('no matches')
		else:
			if len(good_matches) / len(knn_matches) < MIN_MATCH_RATION:
				raise Exception('no matches')

		src_pts = np.float32([keypoints_object[m.queryIdx].pt for m in good_matches]).reshape(-1, 1, 2)
		dst_pts = np.float32([keypoints_scene[m.trainIdx].pt for m in good_matches]).reshape(-1, 1, 2)

		M, mask = cv2.findHomography(src_pts, dst_pts, cv2.RANSAC, 5.0)

		# matches_mask = mask.ravel().tolist()
		# h, w, d = obj.shape
		# pts = np.float32([[0, 0], [0, h - 1], [w - 1, h - 1], [w - 1, 0]]).reshape(-1, 1, 2)
		# dst = cv2.perspectiveTransform(pts, M)
		# img2 = cv2.polylines(img, [np.int32(dst)], True, 255, 3, cv2.LINE_AA)
		# draw_params = dict(matchColor=(0, 255, 0),  # draw matches in green color
		# 	singlePointColor=None,
		# 	matchesMask=matches_mask,  # draw only inliers
		# 	flags=2)
		# img3 = cv2.drawMatches(obj, keypoints_object, img, keypoints_scene, good_matches, None, **draw_params)
		# self._display_image(img3)

		return M
