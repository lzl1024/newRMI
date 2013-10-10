PORT = 8090
DOWNLOADPORT = 8014

all:	
	javac -cp .:lib/http-20070405.jar interfaces/*.java message/*.java registry/*.java rmi/*.java test/*.java util/*.java
	
server: all
	java -cp .:lib/http-20070405.jar rmi.Server ${PORT} ${DOWNLOADPORT}
	
jar: all
	jar cvf rim-lib.jar ./registry/*.class ./message/*.class ./interfaces/Remote.class
	
clean:
	find  -name *.class | xargs rm -rf >> a.out
	find . -name *.class | xargs rm -rf >> a.out
	rm a.out
