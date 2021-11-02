import re

def rearrenge_name(name):
    result= re.search(r"^([\w .]*), ([\w .]*)$",name)
    if result is None:
        return name
    return "{} {}".format(result[2],result[1])

def multi_vowel_word(text):
    pattern=r"\b\w*[aeiou]{3,}\w*\b"
    result=re.findall(pattern,text)
    return result