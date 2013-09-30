PORT = 8090

all:	
	javac interfaces/*.java message/*.java registry/*.java rmi/*.java test/*.java util/*.java
	
server: all
	java rmi.Server ${PORT}
	
jar: all
	jar cvf rim-lib.jar ./registry/*.class ./message/*.class ./interfaces/Remote.class
	
clean:
	find  -name *.class | xargs rm -rf
	find  -name *.jar | xargs rm -rf
