import json

def json_to_yaml(data, indent=0):
    """Convert the parsed JSON data to YAML format manually."""
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

def main():
    input_file = "in.json"
    output_file = "out0.yaml"

    # Read JSON input
    with open(input_file, "r", encoding="utf-8") as f:
        json_content = f.read()

    # Parse JSON
    parsed_json = json.loads(json_content)

    # Convert to YAML
    yaml_content = json_to_yaml(parsed_json)

    # Write YAML output
    with open(output_file, "w", encoding="utf-8") as f:
        f.write(yaml_content)

    print("Conversion complete!")

if __name__ == "__main__":
    main()