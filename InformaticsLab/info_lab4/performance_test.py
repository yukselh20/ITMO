import time
import json
import yaml
from originaltask0 import loads as loads0, json_to_yaml as to_yaml0
from originaltask2 import loads as loads2, json_to_yaml as to_yaml2
from originaltask3 import loads as loads3, json_to_yaml as to_yaml3

def test_parser(parser_func, yaml_converter, json_content, iterations=100):
    total_time = 0
    for _ in range(iterations):
        start_time = time.time()
        parsed = parser_func(json_content)
        yaml_converter(parsed)
        total_time += time.time() - start_time
    return (total_time / iterations) * 1000  # Time in milliseconds

def main():
    # Read JSON input
    with open("in.json", "r", encoding="utf-8") as f:
        json_content = f.read()
    
    # Define tests
    tests = [
        ("Task 0 (Manual)", loads0, to_yaml0),
        ("Task 1 (Built-in)", json.loads, yaml.dump),
        ("Task 2 (Regex)", loads2, to_yaml2),
        ("Task 3 (Grammar)", loads3, to_yaml3)
    ]
    
    # Run performance tests
    print(f"\n{'Parser Type':<20} {'Average Time (ms)':<15} {'Relative Speed':<15}")
    print("-" * 50)
    
    # Measure and display results
    results = [(name, test_parser(parser, yaml_converter, json_content)) for name, parser, yaml_converter in tests]
    
    # Find fastest implementation for relative speed comparison
    fastest_time = min(avg_time for _, avg_time in results)
    
    for name, avg_time in results:
        relative_speed = avg_time / fastest_time
        print(f"{name:<20} {avg_time:>13.3f}ms {relative_speed:>14.2f}x")

if __name__ == "__main__":
    main()
