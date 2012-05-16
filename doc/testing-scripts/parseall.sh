#!/bin/sh
SCRIPT=`readlink -f $0`
DIR=`dirname "$SCRIPT"`
cd $2
for folder in *; do
	echo 
	echo ----------$folder,-----------
	cd $folder
	for fold in *; do
		echo ----------$fold,---------
		cd "$DIR"
		if [[ "$1" == *"-g" ]]; then
			python2 ParseResults.py $1 $folder$fold $2$folder/$fold/ $3
		else
			python2 ParseResults.py $1 $2$folder/$fold/ $3
		fi
		echo
	done
	cd $2
done
if [[ "$1" == *"-g" ]]; then
	cd "$DIR"
	mkdir graphs
	mv *.png graphs/
fi
