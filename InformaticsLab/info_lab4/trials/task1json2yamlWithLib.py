import json
import yaml

# Main function to convert JSON to YAML
def json_to_yaml(input_file, output_file):
    # Read JSON data from the input file
    with open(input_file, "r", encoding="utf-8") as f:
        data = json.load(f)  # Using json.load() to parse JSON directly

    # Convert JSON data to YAML format
    yaml_content = yaml.dump(data, allow_unicode=True, sort_keys=False)

    # Write the YAML content to the output file
    with open(output_file, "w", encoding="utf-8") as f:
        f.write(yaml_content)

    print("Conversion complete!")

# Execute the function
if __name__ == "__main__":
    input_file = "in.json"    # JSON input file path
    output_file = "outWithLib.yaml"   # YAML output file path
    json_to_yaml(input_file, output_file)
