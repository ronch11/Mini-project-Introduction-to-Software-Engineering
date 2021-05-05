import cv2
import numpy as np


def all_borders_zero(array):
    if not array.ndim:
        raise ValueError("0-dimensional arrays not supported")
    for dim in range(array.ndim):
        view = np.moveaxis(array, dim, 0)
        for rgb in view:
            if not (rgb == [0, 0, 0]).all():
                return False
    return True


def is_equal(a, b):
    difference = cv2.subtract(a, b)
    empty = not np.any(difference)
    if empty is True:
        print("Pictures are the same")
        return
    same = all_borders_zero(difference)
    if same:
        print("Pictures are the same")
        return
    cv2.imwrite("ed.jpg", difference)
    print("Pictures are different, the difference is stored as ed.jpg")


a = cv2.imread("lightSphereSpot.png")
b = cv2.imread("lightSpherePoint.png")
is_equal(a, b)
