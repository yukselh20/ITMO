# Lab Assignment #1

Write a program in Java that performs the actions specified in the assignment variant.

## Program Requirements:

*   The program must start, execute, and produce a result correctly. It should not generate any errors. The program must be fully functional at the time of review; excuses like "it was working 5 minutes ago, at home, or in a parallel universe" are not acceptable.
*   The expression must be calculated according to the rules of mathematical expressions (observing the order of operations, etc.).
*   The program must use mathematical functions from the standard Java library.
*   The calculation of each element of the two-dimensional array must be implemented as a separate static method.
*   The result of the expression calculation must be printed to the standard output stream as a matrix with elements in the format specified in the variant. The matrix output must be implemented as a separate static method.
*   The program must be packaged into an executable JAR archive.
*   The program's execution must be demonstrated on the `helios` server.


## Assignment Variant: 75922

1.  Create a one-dimensional array `e` of type `int`. Fill it with numbers from 17 down to 5, inclusive.

2.  Create a one-dimensional array `x` of type `float`. Fill it with 19 random numbers in the range from -6.0 to 15.0.

3.  Create a two-dimensional array `r` with dimensions 13x19. Calculate its elements according to the following formula (where `x = x[j]`):
    *   If `e[i] == 16`, then `r[i][j] = (tan(e^x))^(1/3)`
    *   If `e[i]` is one of `{8, 9, 10, 12, 14, 17}`, then `r[i][j] = cos( ( ((x + 1) / x)^(1/3) )^3 )`
    *   For all other values of `e[i]`: `r[i][j] = arctan( ( e ^ ( ((pi/2 - |x|)^x)^(1/3) ) )^2 )`

4.  Print the resulting array in a format with three decimal places.

### Notes:

1.  If the variant suggests identical array names, add "1" to the name of one of them.
2.  If the calculations sometimes result in `NaN` (Not a Number), this might be the intended behavior.
