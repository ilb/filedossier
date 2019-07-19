#!/bin/sh
USER=$(whoami)
alias python='/home/'$USER'/anaconda3/bin/python3'

python target/test-classes/signaturedetector/signaturedetector/

echo $?
