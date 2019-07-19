from setuptools import setup

setup(
    name = "signaturedetector",
    entry_points = {
        "console_scripts": ['signaturedetector = signaturedetector.signaturedetector:main']
    },
    author = "kudrin, kaa"
)
