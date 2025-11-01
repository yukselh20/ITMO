import json

def json_to_yaml(data, indent=0):
    """
    Converts a given JSON object to YAML format manually.
    This function does not use any off-the-shelf libraries.
    
    :param data: JSON data to convert (in the form of Python dictionary).
    :param indent: Current indentation level.
    :return: YAML-formatted string.
    """
    yaml_output = ""
    spaces = "  " * indent  # Two spaces per indentation level

    if isinstance(data, dict):
        for key, value in data.items():
            # Add the key with a colon
            yaml_output += f"{spaces}{key}:"
            if isinstance(value, dict) or isinstance(value, list):
                yaml_output += "\n" + json_to_yaml(value, indent + 1)  # Recurse for dicts and lists
            else:
                # Add the value as a string
                yaml_output += f" {value}\n"

    elif isinstance(data, list):
        for item in data:
            # List items are indented with an extra space
            yaml_output += f"{spaces}- "
            if isinstance(item, dict) or isinstance(item, list):
                yaml_output += "\n" + json_to_yaml(item, indent + 1)  # Recurse for dicts and lists
            else:
                # Add the list item as a string
                yaml_output += f"{item}\n"
    
    return yaml_output


# Read the JSON data from a file
json_file_path = "in.json"  # Replace with the actual path to your JSON file

with open(json_file_path, "r", encoding="utf-8") as file:
    json_data = json.load(file)

# Convert the JSON data to YAML format
yaml_content = json_to_yaml(json_data)

# Write the YAML content to a file
yaml_output_path = "output.yaml"  # Path where the output YAML will be saved
with open(yaml_output_path, "w", encoding="utf-8") as file:
    file.write(yaml_content)

print(f"Conversion completed. The YAML file has been saved as '{yaml_output_path}'.")
