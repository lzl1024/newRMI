PORT = 8090
Server = 128.237.229.140
all:	
	javac -cp .:lib/rim-lib.jar rmi/*.java test/*.java util/*.java
	
client: all
	java -cp .:lib/rim-lib.jar test.Client ${PORT} ${Server}

clean:
	find . -name *.class | xargs rm -rf
