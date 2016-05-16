
all:
	sbt compile

clean:
	sbt clean
	rm -rf generated
