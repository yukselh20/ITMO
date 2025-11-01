def decimal_to_negative_base(n, base):
    if base >= 0:
        raise ValueError("Base must be negative.")
    
    if n == 0:
        return "0"
    
    digits = []
    while n != 0:
        n, remainder = divmod(n, base)
        if remainder < 0:
            n += 1
            remainder -= base
        digits.append(str(remainder))
    
    return ''.join(digits[::-1])


decimal_number = 369
negative_base = -7
result = decimal_to_negative_base(decimal_number, negative_base)
print(f"{decimal_number} in base {negative_base} is {result}")
