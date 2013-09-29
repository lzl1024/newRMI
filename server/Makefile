all:	
	javac interfaces/*.java message/*.java registry/*.java rmi/*.java test/*.java util/*.java
	
server: all
	java rmi.Server 8090
	
clean:
	find  -name *.class | xargs rm -rf
