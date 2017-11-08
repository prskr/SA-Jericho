#!/bin/bash
TIMES=${1:-"0"}
TARGET=${2:-"thesis"}

[ ! -d out/ ] && mkdir out/

if [ ! -f out/thesis.pdf ]
then
    pdflatex -synctex=1 -interaction=nonstopmode -output-directory out -shell-escape $TARGET.tex
fi
cd out/
makeglossaries $TARGET
cd ../
pdflatex -synctex=1 -interaction=nonstopmode -output-directory out -shell-escape $TARGET.tex


OUT=$(diff $TARGET.bib out/$TARGET.bib)
RESULT=$?
if [ $RESULT -ne "0" ];
then
    cp -f "$TARGET.bib" out/
    cd out/
    bibtex $TARGET.aux
    cd ../
    pdflatex -synctex=1 -interaction=nonstopmode -output-directory out -shell-escape $TARGET.tex
fi

for i in $(seq 1 $TIMES); do
    pdflatex -synctex=1 -interaction=nonstopmode -output-directory out -shell-escape $TARGET.tex
done
