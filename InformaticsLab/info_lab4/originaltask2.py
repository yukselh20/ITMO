import re

def loads(string):
    """Parse JSON string into Python data structure manually."""
    string = string.strip()
    if string.startswith('{'):
        return parse_object(string[1:].strip())
    elif string.startswith('['):
        return parse_array(string[1:].strip())
    raise ValueError("Invalid JSON input")

def parse_object(string):
    """Parse a JSON object (dictionary)."""
    obj = {}
    while string:
        string = string.lstrip()
        if string.startswith('}'): 
            return obj, string[1:].strip()
        key, string = parse_string(string)
        string = string.lstrip()
        if not string.startswith(':'):
            raise ValueError("Expected ':' after key in object")
        value, string = parse_value(string[1:].strip())
        obj[key] = value
        string = string.lstrip()
        if string.startswith('}'):
            return obj, string[1:].strip()
        elif string.startswith(','):
            string = string[1:]
        else:
            raise ValueError("Expected ',' or '}' in object")
    raise ValueError("Unterminated object")

def parse_array(string):
    """Parse a JSON array (list)."""
    arr = []
    while string:
        string = string.lstrip()
        if string.startswith(']'):
            return arr, string[1:].strip()
        value, string = parse_value(string)
        arr.append(value)
        string = string.lstrip()
        if string.startswith(']'):
            return arr, string[1:].strip()
        elif string.startswith(','):
            string = string[1:]
        else:
            raise ValueError("Expected ',' or ']' in array")
    raise ValueError("Unterminated array")

def parse_string(string):
    """Parse a JSON string using regex."""
    string_pattern = r'^"((?:[^"\\]|\\.)*)"'
    match = re.match(string_pattern, string)
    if not match:
        raise ValueError("Invalid string format")
    return match.group(1), string[match.end():].strip()

def parse_number(string):
    """Parse a JSON number using regex."""
    number_pattern = r'^-?\d+(?:\.\d+)?(?:[eE][+-]?\d+)?'
    match = re.match(number_pattern, string)
    if not match:
        raise ValueError("Invalid number format")
    num_str = match.group(0)
    if '.' in num_str or 'e' in num_str or 'E' in num_str:
        return float(num_str), string[match.end():].strip()
    return int(num_str), string[match.end():].strip()

def parse_value(string):
    """Parse any JSON value using regex for primitives."""
    string = string.strip()
    if string.startswith('"'):
        return parse_string(string)
    elif string.startswith('{'):
        return parse_object(string[1:].strip())
    elif string.startswith('['):
        return parse_array(string[1:].strip())
    elif re.match(r'^true\b', string):
        return True, string[4:].strip()
    elif re.match(r'^false\b', string):
        return False, string[5:].strip()
    elif re.match(r'^null\b', string):
        return None, string[4:].strip()
    else:
        return parse_number(string)

def json_to_yaml(data, indent=0):
    """Convert parsed JSON data to YAML format."""
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

if __name__ == "__main__":
    input_file = "in.json"
    output_file = "outTask2.yaml"

    # Read JSON input
    with open(input_file, "r", encoding="utf-8") as f:
        json_content = f.read()

    # Parse JSON and convert to YAML
    parsed_json, _ = loads(json_content)
    yaml_content = json_to_yaml(parsed_json)

    # Write YAML output
    with open(output_file, "w", encoding="utf-8") as f:
        f.write(yaml_content)

    print("Conversion complete!")