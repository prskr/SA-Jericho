DISS  = thesis

$(DISS): $(DISS).ps


$(DISS).ps: $(DISS).tex chapters.tex abstract.tex $(DISS).bib append.tex title.tex toc.tex
	-latex $(DISS)
	-bibtex -min-crossrefs=10 $(DISS)
	-makeindex $(DISS)
	-latex $(DISS)
	-latex $(DISS)
	-dvips $(DISS)

$(DISS).pdf: $(DISS).ps
	ps2pdf13 -dPDFSETTINGS=/prepress $(DISS).ps

clean:
	-rm $(DISS).ps
	-rm $(DISS).pdf
	-rm *~ $(DISS).{log,aux,bbl,blg,dvi,idx,ilg,ind,lof,lot,not,toc}
	-rm *.{log,aux,bbl,blg,dvi,idx,ilg,ind,lof,lot,not,toc}


