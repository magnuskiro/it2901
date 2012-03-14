#! /bin/bash
pdflatex -interaction nonstopmode report.tex > outputfromcompile
makeglossaries report 
bibtex report
pdflatex -interaction nonstopmode report.tex > outputfromcompile
pdflatex -interaction nonstopmode report.tex > outputfromcompile
rm outputfromcompile
