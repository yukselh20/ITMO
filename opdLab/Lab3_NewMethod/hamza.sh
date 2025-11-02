#!/usr/bin/env bash

echo "
0F9 a
062C w
000A w
BEEF w
0200 w
1207 w
2F40 w
F0FD w
1206 w
0680 w
E8F6 w
EEF7 w
0680 w
7EF4 w
F000 w
0200 w
1207 w
2F40 w
F0FD w
1206 w
3EEE w
E8EB w
6EEC w
7EEA w
F003 w
AAE7 w
0200 w
CEE9 w
AAE4 w
0100 w
0000 w

0FC a

sl 0
ru
s c

c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c
" | java -jar -Dmode=cli bcomp-ng.jar
