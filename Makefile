OUT_DIR=psim

vcs: build
	sbt "; run-main plasticine.TopGen --testArgs --outdir ${OUT_DIR}"
	cp -r static/fringeVCS/* ${OUT_DIR}
	make -C ${OUT_DIR}

build:
	sbt compile

vcs-clean:
	make -C ${OUT_DIR} clean

distclean: clean
	sbt clean
	rm -rf ${OUT_DIR}
