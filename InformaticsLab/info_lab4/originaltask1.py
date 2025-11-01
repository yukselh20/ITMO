import json
import yaml

# Main function
if __name__ == "__main__":
    input_file = "in.json"
    output_file = "outTask1.yaml"

    # Read JSON input
    with open(input_file, "r", encoding="utf-8") as f:
        json_content = f.read()

    # Parse JSON using the built-in json library
    parsed_json = json.loads(json_content)

    # Convert JSON to YAML using PyYAML library
    yaml_content = yaml.dump(parsed_json, allow_unicode=True, default_flow_style=False)

    # Write YAML output to file
    with open(output_file, "w", encoding="utf-8") as f:
        f.write(yaml_content)

    print("Conversion complete!")
