def parse_value(value, indent=0):
    if isinstance(value, dict):
        return parse_dict(value, indent)
    elif isinstance(value, list):
        return parse_list(value, indent)
    elif isinstance(value, bool):
        return str(value).lower()
    elif isinstance(value, (int, float)):
        return str(value)
    elif value is None:
        return 'null'
    else:
        if any(char in str(value) for char in ':[]{},\n'):
            return f"'{str(value)}'"
        return str(value)

def parse_dict(obj, indent=0):
    if not obj:
        return '{}'
    
    result = []
    spaces = ' ' * indent
    
    for key, value in obj.items():
        if isinstance(value, (dict, list)) and value:
            result.append(f"{spaces}{key}:")
            result.append(parse_value(value, indent + 2))
        else:
            formatted_value = parse_value(value, indent + 2)
            result.append(f"{spaces}{key}: {formatted_value}")
            
    return '\n'.join(result)

def parse_list(arr, indent=0):
    if not arr:
        return '[]'
    
    result = []
    spaces = ' ' * indent
    
    for item in arr:
        if isinstance(item, (dict, list)) and item:
            result.append(f"{spaces}- ")
            parsed_item = parse_value(item, indent + 2)
            # Remove the initial spaces from the first line
            first_line = True
            for line in parsed_item.split('\n'):
                if first_line:
                    result.append(line)
                    first_line = False
                else:
                    result.append(f"{spaces}  {line}")
        else:
            result.append(f"{spaces}- {parse_value(item)}")
            
    return '\n'.join(result)

def json_to_yaml(json_str):
    # Simple JSON parser
    def parse_json(s, start=0):
        s = s.strip()
        if s[start] == '{':
            return parse_json_object(s, start)
        elif s[start] == '[':
            return parse_json_array(s, start)
        elif s[start] == '"':
            return parse_json_string(s, start)
        elif s[start].isdigit() or s[start] == '-':
            return parse_json_number(s, start)
        elif s.startswith('true', start):
            return True, start + 4
        elif s.startswith('false', start):
            return False, start + 5
        elif s.startswith('null', start):
            return None, start + 4
        raise ValueError(f"Invalid JSON at position {start}")

    def parse_json_object(s, start):
        obj = {}
        i = start + 1
        expect_key = True
        current_key = None
        
        while i < len(s):
            char = s[i]
            if char.isspace() or char == ',':
                i += 1
                continue
            if char == '}':
                return obj, i + 1
            
            if expect_key:
                if char != '"':
                    raise ValueError(f"Expected '\"' at position {i}")
                key, i = parse_json_string(s, i)
                while i < len(s) and s[i].isspace():
                    i += 1
                if i >= len(s) or s[i] != ':':
                    raise ValueError(f"Expected ':' at position {i}")
                current_key = key
                expect_key = False
                i += 1
            else:
                value, i = parse_json(s, i)
                obj[current_key] = value
                expect_key = True
                
        raise ValueError("Unterminated object")

    def parse_json_array(s, start):
        arr = []
        i = start + 1
        
        while i < len(s):
            char = s[i]
            if char.isspace() or char == ',':
                i += 1
                continue
            if char == ']':
                return arr, i + 1
                
            value, i = parse_json(s, i)
            arr.append(value)
            
        raise ValueError("Unterminated array")

    def parse_json_string(s, start):
        result = []
        i = start + 1
        
        while i < len(s):
            char = s[i]
            if char == '"':
                return ''.join(result), i + 1
            if char == '\\':
                i += 1
                if i >= len(s):
                    raise ValueError("Unterminated string")
                char = s[i]
                if char in '"\\':
                    result.append(char)
                elif char == 'n':
                    result.append('\n')
                else:
                    result.append('\\' + char)
            else:
                result.append(char)
            i += 1
            
        raise ValueError("Unterminated string")

    def parse_json_number(s, start):
        i = start
        while i < len(s) and (s[i].isdigit() or s[i] in '.-+eE'):
            i += 1
        num_str = s[start:i]
        try:
            if '.' in num_str or 'e' in num_str.lower():
                return float(num_str), i
            return int(num_str), i
        except ValueError:
            raise ValueError(f"Invalid number at position {start}")

    # Parse JSON string to Python object
    parsed_data, _ = parse_json(json_str)
    
    # Convert to YAML
    return parse_value(parsed_data)

# Read the JSON file
with open('in.json', 'r', encoding='utf-8') as f:
    json_content = f.read()

# Convert to YAML
yaml_content = json_to_yaml(json_content)

# Write the YAML output
with open('out2.yaml', 'w', encoding='utf-8') as f:
    f.write(yaml_content)

print("Conversion completed. Check schedule.yaml for the result.")