#!/bin/sh
cd $1
#for folder in *; do
#	echo 
#	echo ----------$folder,-----------
#	cd $folder
	for fold in *; do
		echo ----------$fold,---------
		cd '/home/qos/server/Result Scripts/'
		python ParseResults.py -t -m $1$folder/$fold/ 2 3 4
		echo
	done
	cd $1
#done
