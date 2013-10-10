PORT = 8090
Server = localhost
DOWNLOADPORT = 8014

all:	
	javac -cp .:lib/rim-lib.jar rmi/*.java test/*.java util/*.java
	
client: all
	java -cp .:lib/rim-lib.jar test.Client ${PORT} ${Server} ${DOWNLOADPORT}

clean:
	find . -name *.class | xargs rm -rf >> a.out
	find -name *.class | xargs rm -rf >> a.out
	rm a.out
