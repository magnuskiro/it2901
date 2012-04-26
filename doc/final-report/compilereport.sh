#! /bin/bash
pdflatex -shell-escape -interaction nonstopmode report.tex > outputfromcompile
makeglossaries report 
bibtex report
pdflatex -shell-escape -interaction nonstopmode report.tex > outputfromcompile
pdflatex -shell-escape -interaction nonstopmode report.tex > outputfromcompile
rm outputfromcompile
