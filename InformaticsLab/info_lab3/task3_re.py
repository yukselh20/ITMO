import re

# 408078 % 8 = 6 ==> Variant 6 
# tüm padejlerin eklerini yaz ve istenen numaraya göre metindeki padejleri değiştir.

def replace_suffix(text, case_number):
    pattern = r"\b(\w*)(ый|ий|ая|ое|ую|ем|их|ого|ому|ым|ыми|ом|ой|ые|ых)\b"

    replacements = {
        1: 'ый', # for masculine case 1
        2: 'ый', # for masculine case 2
        3: 'ого',# for masculine case 3
        4: 'ому',# for masculine case 4
        5: 'ым', # for masculine case 5
        6: 'ом'  # for masculine case 6
    }
    
    if case_number not in replacements:
        raise ValueError("Invalid case number. Choose a number between 1 and 6.")
    new_suffix = replacements[case_number]
    return re.sub(pattern, rf'\1{new_suffix}', text, flags=re.UNICODE) # first gruop + new suffixes


