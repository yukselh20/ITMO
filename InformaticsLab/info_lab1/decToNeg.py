def dec2neg(i: int) -> str:
    """Decimal to negative base"""
    if i == 0:
        digits = ["0"]
    else:
        digits = []
        while i != 0:
            i, remain = divmod(i, -10) #divimod === bölüm (часть) ve kalanı (остаток) döndürür. i nin -10 dan bölümü ve kalanı.
       
            if remain < 0: 
                # Note that in most programming languages, the result (in integer arithmetic) of dividing a negative number by a negative number
                # is rounded towards 0, usually leaving a negative remainder. In such a case we have a = (−r)c + d = (−r)c + d − r + r = (−r)(c + 1) + (d + r). 
                # Because |d| < r, (d + r) is the positive remainder. Therefore, to get the correct result in such case, 
                # computer implementations of the above algorithm should add 1 and r = remainder to the quotient and remainder respectively.
                
                i, remain = i + 1, remain + 10
                print(i,remain)
                print("*********")
            digits.append(str(remain))
    return "".join(digits[::-1])


print(dec2neg(369))