import pandas as pd
import json

# Function to flatten the JSON and convert it to a DataFrame
def convert_json_to_csv(json_file, csv_file):
    with open(json_file, 'r', encoding='utf-8') as f:
        data = json.load(f)
    
    # Prepare list to store flattened schedule data
    schedule_data = []
    
    # Iterate over days (Wednesday, Friday, etc.)
    for day, day_info in data["days"].items():
        # Iterate over each schedule entry for the day
        for entry in day_info["schedule"]:
            flattened_entry = {
                "even_week": data["even_week"],
                "group_name": data["group_name"],
                "day": day,
                "title": entry["title"],
                "class_format": entry["class_format"],
                "type": entry["type"],
                "campus": entry["campus"],
                "time": entry["time"],
                "auditory": entry["auditory"],
                "teacher": entry["teacher"]
            }
            schedule_data.append(flattened_entry)
    
    # Convert the list of flattened data to a pandas DataFrame
    df = pd.DataFrame(schedule_data)
    
    # Save the DataFrame to a CSV file
    df.to_csv(csv_file, index=False, encoding='utf-8')
    print(f"CSV file has been saved at {csv_file}")

# Get file paths from the user
json_file_path = "in.json"
csv_file_path = "output.csv"

# Call the function to convert JSON to CSV
convert_json_to_csv(json_file_path, csv_file_path)
