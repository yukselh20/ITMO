#!/usr/bin/env bash

echo "
2CC a
02E0 w
A000 w
E000 w
0200 w
AF80 w
0740 w
0680 w
EEFB w
AF03 w
EEF8 w
4EF5 w
EEF5 w
ABF4 w
F203 w
7EF4 w
F901 w
EEF2 w
82CE w
CEF9 w
0100 w
F600 w
FD00 w
72DE w

2D0 a

sl 0

c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c 
" | java -jar -Dmode=cli bcomp-ng.jar