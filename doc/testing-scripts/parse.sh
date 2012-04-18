#!/bin/sh
cd $1
for folder in *; do
	cd $folder
	for fold in *; do
		echo
		echo $folder
		cd '/home/qos/server/Result Scripts/'
		python ParseResults.py -t -m $1$folder/$fold/ 2 3 4 --core
		break
	done
	cd $1
done
