set SDLJ=<path to sdljava.jar>
javac -cp %SDLJ%/sdljava.jar *.java
copy *.class civ_techtree
jar cvf civtree.jar civ_techtree
