import constants


def mmToInch(mm):
    return mm / constants.MM_IN_INCH()


def inchToMm(inch):
    return inch * constants.MM_IN_INCH()


def mmToPix(mm, dpi):
    return mm * dpi / constants.MM_IN_INCH()


def pixToMm(pix, dpi):
    return pix / dpi * constants.MM_IN_INCH()


def calc_page_dpi(sizes: list) -> float:
    a, b = sizes
    w_pix = a if a < b else b
    return w_pix / mmToInch(constants.A4_PAGE_WIDTH_MM())
