PORT = 8090

all:	
	javac -cp .:lib/rim-lib.jar rmi/*.java test/*.java util/*.java
	
client: all
	java -cp .:lib/rim-lib.jar test.Client ${PORT}

clean:
	find  -name *.class | xargs rm -rf
