import task1_re, task2_re, task3_re

task = input("Enter task number (1-3) you want to run: ")
if task == '1':
    print(task1_re.search_emotions(input("Enter your string to find the number of emotions: ")))
elif task == '2':
    print(task2_re.find_pattern(input("Enter a poem whose lines are seperated with '/': ")))
elif task == '3':
    print(task3_re.replace_suffix(text=input("Enter your text to replace adjective cases with desired case number(text): "),case_number = int(input("Case number(1-6): "))))
else:
    print("Can't resolve task number")