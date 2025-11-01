# Define JSON syntax symbols
JSON_COMMA = ','
JSON_COLON = ':'
JSON_LEFTBRACKET = '['
JSON_RIGHTBRACKET = ']'
JSON_LEFTBRACE = '{'
JSON_RIGHTBRACE = '}'
JSON_QUOTE = '"'
QUOTE = '"'

# Define whitespace and syntax arrays
WHITESPACE = [' ', '\t', '\b', '\n', '\r']
SYNTAX = [JSON_COMMA, JSON_COLON, JSON_LEFTBRACKET, JSON_RIGHTBRACKET, JSON_LEFTBRACE, JSON_RIGHTBRACE]

# Lexer functions
def lex_string(string):
    json_string = ''
    if string[0] == QUOTE:
        string = string[1:]
    else:
        return None, string
    for c in string:
        if c == QUOTE:
            return json_string, string[len(json_string) + 1:]
        else:
            json_string += c
    raise Exception('End of string quote missing')

def lex_number(string):
    json_number = ''
    number_characters = [str(d) for d in range(0, 10)] + ['-', 'e', '.']
    for c in string:
        if c in number_characters:
            json_number += c
        else:
            break
    rest = string[len(json_number):]
    if not len(json_number):
        return None, string
    if '.' in json_number:
        return float(json_number), rest
    return int(json_number), rest

def lex_bool(string):
    if string.startswith("true"):
        return True, string[4:]
    elif string.startswith("false"):
        return False, string[5:]
    return None, string

def lex_null(string):
    if string.startswith("null"):
        return None, string[4:]
    return None, string

def lex(string):
    tokens = []
    while len(string):
        json_string, string = lex_string(string)
        if json_string is not None:
            tokens.append(json_string)
            continue
        json_number, string = lex_number(string)
        if json_number is not None:
            tokens.append(json_number)
            continue
        json_bool, string = lex_bool(string)
        if json_bool is not None:
            tokens.append(json_bool)
            continue
        json_null, string = lex_null(string)
        if json_null is not None:
            tokens.append(None)
            continue
        if string[0] in WHITESPACE:
            string = string[1:]
        elif string[0] in SYNTAX:
            tokens.append(string[0])
            string = string[1:]
        else:
            raise Exception("Unknown character: {}".format(string[0]))
    return tokens

# Parser functions
def parse_array(tokens):
    json_array = []
    if tokens[0] == JSON_RIGHTBRACKET:
        return json_array, tokens[1:]
    while True:
        json, tokens = parse(tokens)
        json_array.append(json)
        if tokens[0] == JSON_RIGHTBRACKET:
            return json_array, tokens[1:]
        elif tokens[0] != JSON_COMMA:
            raise Exception('Expected comma after array element')
        tokens = tokens[1:]

def parse_object(tokens):
    json_object = {}
    if tokens[0] == JSON_RIGHTBRACE:
        return json_object, tokens[1:]
    while True:
        json_key = tokens[0]
        if isinstance(json_key, str):
            tokens = tokens[1:]
        else:
            raise Exception('Expected string key in object')
        if tokens[0] != JSON_COLON:
            raise Exception('Expected colon (:) after key in object')
        tokens = tokens[1:]
        json_value, tokens = parse(tokens)
        json_object[json_key] = json_value
        if tokens[0] == JSON_RIGHTBRACE:
            return json_object, tokens[1:]
        elif tokens[0] != JSON_COMMA:
            raise Exception('Expected comma after object pair')
        tokens = tokens[1:]

def parse(tokens):
    t = tokens[0]
    if t == JSON_LEFTBRACKET:
        return parse_array(tokens[1:])
    elif t == JSON_LEFTBRACE:
        return parse_object(tokens[1:])
    else:
        return t, tokens[1:]

def loads(string):
    return parse(lex(string))[0]

# YAML conversion function
def json_to_yaml(data, indent=0):
    yaml_str = ""
    indentation = "  " * indent
    if isinstance(data, dict):
        for key, value in data.items():
            yaml_str += f"{indentation}{key}:"
            if isinstance(value, (dict, list)):
                yaml_str += "\n" + json_to_yaml(value, indent + 1)
            else:
                yaml_str += f" {value}\n"
    elif isinstance(data, list):
        for item in data:
            yaml_str += f"{indentation}- "
            if isinstance(item, (dict, list)):
                yaml_str += "\n" + json_to_yaml(item, indent + 1)
            else:
                yaml_str += f"{item}\n"
    return yaml_str

# Main function
if __name__ == "__main__":
    input_file = "in.json"
    output_file = "out.yaml"

    # Read JSON input
    with open(input_file, "r", encoding="utf-8") as f:
        json_content = f.read()

    # Parse JSON and convert to YAML
    parsed_json = loads(json_content)
    yaml_content = json_to_yaml(parsed_json)

    # Write YAML output
    with open(output_file, "w", encoding="utf-8") as f:
        f.write(yaml_content)

    print("Conversion complete!")
