#!/usr/bin/env bash

# Bu betik, BComp CLI modunda programiniza ait
# bellek verilerini yukleyip trace ciktisi almak icin ornek teskil eder.

# Aşagıda echo içerisinde:
#  - Bellek yükleme komutları (2CC a, 02E0 w, vb.)
#  - Programı çalıştırma/izleme komutları (ru, s c, vs.)
#  - BComp CLI bunları sırasıyla okuyup uygular.

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

# Adresi 2D0'a set et (program baslangicina gelecek sekilde)
2D0 a

# Ornegin 'sl 0' step-level izleme baslatabilir
# (Dokumandaki komut aciklamasina bagli olarak)
sl 0

# Programi calistir (run)
ru

# Adim adim izleme icin step cycle gibi bir komut
s c

# Burada dilediginiz ek komutlari ekleyebilirsiniz:
# ornegin register goster, memory dump, vb.

" | java -jar -Dmode=cli ../bcomp-ng.jar