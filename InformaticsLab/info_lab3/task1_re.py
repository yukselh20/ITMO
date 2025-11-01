import re 

def search_emotions(string):
    '''
    my isu number 408078 
    408078 % 6 = 0 ==> 8
    408078 % 4 = 2 ==> -{
    408078 % 8 = 6 ==> O
    pattern = 8-{O
    '''
    pattern = r"8-\{O" # { } işaretleri regexte özel işaretlerdir ve kaçırılmaları gerekli, r ile \ (escape sequencess) dikkate alınmaz.
    return len(re.findall(pattern, string))