json_data = {
    "even_week": True,
    "group_name": "P3132",
    "days": {
        "Wednesday": {
            "date": "Wednesday",
            "schedule": [
                {
                    "title": "Информатика",
                    "class_format": "Очно",
                    "type": "Лекция",
                    "campus": "ул.Ломоносова, д.9, лит. М",
                    "time": "08:20-09:50",
                    "auditory": "Актовый зал (1216/0 (усл))",
                    "teacher": "Балакшин Павел Валерьевич"
                }
            ]
        }
    }
}

def convert_to_yaml(data, indent=0):
    yaml_str = ""
    indent_str = "  " * indent
    if isinstance(data, dict):
        for key, value in data.items():
            yaml_str += f"{indent_str}{key}:"
            if isinstance(value, (dict, list)):
                yaml_str += "\n" + convert_to_yaml(value, indent + 1)
            else:
                yaml_str += f" {str(value).lower()}\n"  # Boolean değerini küçük harflerle yaz
    elif isinstance(data, list):
        for item in data:
            yaml_str += f"{indent_str}- " + (
                "\n" + convert_to_yaml(item, indent + 1) if isinstance(item, (dict, list)) else f"{item}\n"
            )
    return yaml_str

yaml_data = convert_to_yaml(json_data)
print(yaml_data)
