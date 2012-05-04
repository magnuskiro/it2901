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
        case "$1" in
        *-g*)
            GRAPHNAME=$folder$fold
            ;;
        *)
            GRAPHNAME=""
            ;;
        esac
        python2 ParseResults.py $1 $GRAPHNAME $2$folder/$fold/ $3
		echo
	done
	cd $2
done
cd "$DIR"
mkdir graphs
mv *.png graphs/