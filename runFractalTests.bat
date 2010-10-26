javac -sourcepath ./gaima/src/fractal/* -d ./bin

java -Xmx1500M -cp "./bin/" fractal.BuildFractal 10000 10000 ./newImages/test1 MANDELBROT

rem java -Xmx1500M -jar ./bin/BuildFractal.jar 10000 10000 ./newImages/test2 MANDELBROT
