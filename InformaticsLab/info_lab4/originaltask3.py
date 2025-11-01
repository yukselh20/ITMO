from lark import Lark, Transformer

# Define JSON grammar
JSON_GRAMMAR = """
    ?value: dict
          | list
          | string
          | number
          | "true"  -> true
          | "false" -> false
          | "null"  -> null

    dict: "{" [pair ("," pair)*] "}"
    pair: string ":" value

    list: "[" [value ("," value)*] "]"

    string: ESCAPED_STRING
    number: SIGNED_NUMBER

    %import common.ESCAPED_STRING
    %import common.SIGNED_NUMBER
    %import common.WS
    %ignore WS
"""

class JsonTransformer(Transformer):
    """Transform parse tree into Python data structure."""
    def string(self, s):
        return s[0][1:-1]  # Remove quotes
    
    def number(self, n):
        return float(n[0]) if '.' in n[0] else int(n[0])
    
    def true(self, _):
        return True
    
    def false(self, _):
        return False
    
    def null(self, _):
        return None
    
    def list(self, items):
        return list(items)
    
    def pair(self, key_value):
        return (key_value[0], key_value[1])
    
    def dict(self, pairs):
        return dict(pairs)

def json_to_yaml(data, indent=0):
    """Convert parsed JSON data to YAML format."""
    yaml_str = ""
    indentation = "  " * indent
    
    if isinstance(data, dict):
        if not data:
            return "{}\n"
        for key, value in data.items():
            yaml_str += f"{indentation}{key}:"
            if isinstance(value, (dict, list)):
                yaml_str += "\n" + json_to_yaml(value, indent + 1)
            else:
                yaml_str += f" {_format_value(value)}\n"
    elif isinstance(data, list):
        if not data:
            return "[]\n"
        for item in data:
            yaml_str += f"{indentation}- "
            if isinstance(item, (dict, list)):
                yaml_str += "\n" + json_to_yaml(item, indent + 1)
            else:
                yaml_str += f"{_format_value(item)}\n"
    return yaml_str

def _format_value(value):
    """Format primitive values for YAML output."""
    if isinstance(value, str):
        if any(c in value for c in '\n\r\t\'"'):
            return f'"{value.replace('"', '\\"')}"'
        return value
    elif value is None:
        return 'null'
    return str(value)

def loads(json_str):
    """Parse JSON string into Python data structure using formal grammar."""
    parser = Lark(JSON_GRAMMAR, start='value', parser='lalr')
    transformer = JsonTransformer()
    tree = parser.parse(json_str)
    return transformer.transform(tree)

if __name__ == "__main__":
    input_file = "in.json"
    output_file = "outTask3.yaml"

    # Read JSON input
    with open(input_file, "r", encoding="utf-8") as f:
        json_content = f.read()

    try:
        # Parse JSON using formal grammar
        parsed_json = loads(json_content)
        
        # Convert to YAML
        yaml_content = json_to_yaml(parsed_json)
        
        # Write YAML output
        with open(output_file, "w", encoding="utf-8") as f:
            f.write(yaml_content)
            
        print("Conversion complete!")
    except Exception as e:
        print(f"Error during conversion: {str(e)}")