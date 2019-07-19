#!/bin/sh

USER=$(whoami)

alias python='/home/'$USER'/anaconda3/bin/python'

python target/test-classes/documentbarcode/documentbarcode
echo $?
