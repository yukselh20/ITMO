import re

def loads(string):
    """Parse JSON string into Python data structure using regex."""
    string = string.strip()
    if string.startswith('{'):
        return parse_object(string[1:].strip())
    elif string.startswith('['):
        return parse_array(string[1:].strip())
    raise ValueError("Invalid JSON input")

def parse_object(string):
    """Parse a JSON object (dictionary) using regex."""
    obj = {}
    pattern = r'\s*"(.*?)"\s*:\s*(.*?)(,|})'  # Regex pattern for key-value pairs in an object
    
    while string:
        string = string.lstrip()  # Remove leading whitespace
        
        if string.startswith('}'):
            return obj, string[1:].strip()  # End of object
        
        match = re.match(pattern, string)
        if match:
            key = match.group(1)  # Key (without quotes)
            value_str = match.group(2).strip()  # Value string without surrounding spaces
            value, remaining_string = parse_value(value_str)
            obj[key] = value
            string = remaining_string.strip()  # Update string with remaining content after parsing
        else:
            raise ValueError(f"Invalid object format: {string}")
            
    raise ValueError("Unterminated object")

def parse_array(string):
    """Parse a JSON array (list) using regex."""
    arr = []
    pattern = r'(\s*(.*?))(,|\])'  # Regex pattern for array elements
    
    while string:
        string = string.lstrip()  # Remove leading whitespace
        
        if string.startswith(']'):
            return arr, string[1:].strip()  # End of array
        
        match = re.match(pattern, string)
        if match:
            value_str = match.group(2).strip()  # Value string without surrounding spaces
            value, remaining_string = parse_value(value_str)
            arr.append(value)
            string = remaining_string.strip()  # Update string with remaining content after parsing
        else:
            raise ValueError(f"Invalid array format: {string}")
            
    raise ValueError("Unterminated array")

def parse_string(string):
    """Parse a JSON string using regex."""
    match = re.match(r'"(.*?)"', string)
    if not match:
        raise ValueError("Unterminated string")
    return match.group(1), string[match.end():].strip()

def parse_number(string):
    """Parse a JSON number (int or float) using regex."""
    match = re.match(r'([-+]?\d*\.?\d+(?:[eE][-+]?\d+)?)', string)
    if not match:
        raise ValueError("Invalid number format")
    num_str = match.group(1)
    if '.' in num_str or 'e' in num_str or 'E' in num_str:
        return float(num_str), string[match.end():].strip()
    else:
        return int(num_str), string[match.end():].strip()

def parse_value(string):
    """Parse any JSON value using regex."""
    if string.startswith('"'):
        return parse_string(string)
    elif string.startswith('{'):
        return parse_object(string[1:].strip())
    elif string.startswith('['):
        return parse_array(string[1:].strip())
    elif string.startswith('true'):
        return True, string[4:].strip()
    elif string.startswith('false'):
        return False, string[5:].strip()
    elif string.startswith('null'):
        return None, string[4:].strip()
    else:
        return parse_number(string)

# YAML conversion function remains the same
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

# Main function remains the same
if __name__ == "__main__":
    input_file = "in.json"
    output_file = "outTask3.yaml"

    # Read JSON input
    data = []
    with open(input_file, "r", encoding="utf-8") as f:
        json_content = f.read()

    # Parse JSON and convert to YAML
    parsed_json, _ = loads(json_content)
    yaml_content = json_to_yaml(parsed_json)

    # Write YAML output
    with open(output_file, "w", encoding="utf-8") as f:
        f.write(yaml_content)

    print("Conversion complete!")
