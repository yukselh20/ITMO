#!/usr/bin/env bash

echo "
2C9 a
7FFF w
0005 w
FFFB w
02EE w
A000 w
E000 w
0000 w
0200 w
AF0F w # arrayin sayısını burada belirt sonrasında aşağıdan ekle
EEFB w
4EF8 w
EEF8 w
AAF6 w
F20D w
7EF1 w
F901 w
EEEF w
42CB w
F002 w
F204 w
C2DA w
AF01 w
42CF w
E2CF w
82CE w
C2D5 w
0100 w
42CA w
F002 w
F304 w
C2E4 w
AF01 w
42CF w
E2CF w
82CE w
C2D5 w
0100 w
000A w
000F w
0006 w
000C w
FFF4 w
FFF6 w
FFFB w
0004 w
0012 w
FFFF w
FFF7 w
0001 w
001E w
001A w
FFE4 w

2D0 a


sl 0

c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c
c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c
c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c
" | java -jar -Dmode=cli bcomp-ng.jar