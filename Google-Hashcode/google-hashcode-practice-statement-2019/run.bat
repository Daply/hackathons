md bin
javac -sourcepath ./src -d bin src/com/pizzacutter/Solver.java
java -classpath ./bin com.pizzacutter.Solver
echo delete bin
rd /s bin
PAUSE