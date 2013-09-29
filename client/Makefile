all:	
	javac interfaces/*.java message/*.java registry/*.java rmi/*.java test/*.java util/*.java
	
client: all
	java test.Client 8090

clean:
	find  -name *.class | xargs rm -rf
