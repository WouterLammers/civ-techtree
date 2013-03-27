set SDLJ=<path to sdljava.jar>
set PATH=%PATH%;<path to sdljava.dll and other sdl.dlls>
java -Djava.library.path=%SDLJ% -cp %SDLJ%/sdljava.jar;civtree.jar civ_techtree/Techtree
