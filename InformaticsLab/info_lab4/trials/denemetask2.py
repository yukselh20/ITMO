import re

# JSON Syntax constants
JSON_COMMA = ','
JSON_COLON = ':'
JSON_LEFTBRACKET = '['
JSON_RIGHTBRACKET = ']'
JSON_LEFTBRACE = '{'
JSON_RIGHTBRACE = '}'
JSON_QUOTE = '"'
QUOTE = '"'

WHITESPACE = [' ', '\t', '\b', '\n', '\r']
SYNTAX = [JSON_COMMA, JSON_COLON, JSON_LEFTBRACKET, JSON_RIGHTBRACKET,
          JSON_LEFTBRACE, JSON_RIGHTBRACE]

# Regex patterns
number_regex = re.compile(r"(-?(?:0|[1-9]\d*)(?:\.\d+)?(?:[eE][+-]?\d+)?)\s*(.*)", re.DOTALL)
string_regex = re.compile(r"(\"(?:[^\\']|\\['\\/bfnrt]|\\u[0-9a-fA-F]{4})*?\")\s*(.*)", re.DOTALL)
null_regex = re.compile(r"(null)\s*(.*)", re.DOTALL)
bool_regex = re.compile(r"(true|false)\s*(.*)", re.DOTALL)

def lex_string(string):
    match = string_regex.match(string)
    if not match:
        return None, string
    
    json_string, _ = match.groups()
    return eval(json_string), string[len(json_string):]

def lex_number(string):
    match = number_regex.match(string)
    if not match:
        return None, string

    json_number, rest = match.groups()
    return eval(json_number), rest

def lex_null(string):
    match = null_regex.match(string)
    if match is not None:
        _, rest = match.groups()
        return 'null', rest
    else:
        return None, string

def lex_bool(string):
    match = bool_regex.match(string)
    if match is not None:
        json_bool, rest = match.groups()
        return json_bool == 'true', rest
    else:
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
            raise ValueError(f"Unknown character: {string[0]}")

    return tokens

# Parsing functions for arrays, objects, and values
def parse_array(tokens):
    json_array = []
    while tokens:
        t = tokens[0]
        if t == JSON_RIGHTBRACKET:
            return json_array, tokens[1:]
        else:
            json_value, tokens = parse(tokens)
            json_array.append(json_value)
            if tokens and tokens[0] == JSON_COMMA:
                tokens = tokens[1:]  # Skip comma
            elif tokens and tokens[0] == JSON_RIGHTBRACKET:
                return json_array, tokens[1:]
            else:
                raise ValueError("Expected ',' or ']' in array")
    raise ValueError("Unterminated array")

def parse_object(tokens):
    json_object = {}
    while tokens:
        t = tokens[0]
        if t == JSON_RIGHTBRACE:
            return json_object, tokens[1:]
        elif isinstance(t, str):
            key = t
            tokens = tokens[1:]
            if tokens[0] != JSON_COLON:
                raise ValueError(f"Expected ':' after key {key}")
            tokens = tokens[1:]
            value, tokens = parse(tokens)
            json_object[key] = value
            if tokens and tokens[0] == JSON_COMMA:
                tokens = tokens[1:]  # Skip comma
        else:
            raise ValueError(f"Expected string key, got: {t}")
    raise ValueError("Unterminated object")

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
    """Convert the parsed JSON data to YAML format."""
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
    output_file = "outwithreg.yaml"

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
