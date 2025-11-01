import re

def find_pattern(string):
    # 408078 % 6 = 0 ==> Variant 0 
    
    lines = string.split("/")

    if len(lines) != 3:
        return "The number of lines should be 3"

    # Her dizedeki hece sayısını regex ile kontrol et
    syllable_patterns = [r"^([^aeiou]*[aeiou]){5}[^aeiou]*$", r"^([^aeiou]*[aeiou]){7}[^aeiou]*$", r"^([^aeiou]*[aeiou]){5}[^aeiou]*$"]
    # ilk carrot cümle başı için ikinci carrot ise none anlamına gelir.
    for line, pattern in zip(lines, syllable_patterns): # line ile syllable zipleyerek her bir line için her bir syllable elemanını eşler(for ile). ve karşılaştırır.
        if not re.match(pattern, line, re.IGNORECASE): # line ile verilen pattern eşleşir ise (case yok sayılıp) true eşleşmezse none dödürür. 
            return "Not Haiku!" # ifin içindeyiz. 
    # yukardakilere girmiyor o zaman haiku. 
    return "Haiku!"
