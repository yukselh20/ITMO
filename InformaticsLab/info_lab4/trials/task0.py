def parse_string(string):
    """Parse a JSON string using regex."""
    match = re.match(r"(\"(?:[^\\']|\\['\\/bfnrt]|\\u[0-9a-fA-F]{4})*?\")\s*(.*)", string)
    if not match:
        raise ValueError("Unterminated string")
    return match.group(1), string[match.end():].strip()

def parse_number(string):
    """Parse a JSON number (int or float) using regex."""
    match = re.match(r"(-?(?:0|[1-9]\d*)(?:\.\d+)?(?:[eE][+-]?\d+)?)\s*(.*)", string)
    if not match:
        raise ValueError("Invalid number format")
    num_str = match.group(1)
    if '.' in num_str or 'e' in num_str or 'E' in num_str:
        return float(num_str), string[match.end():].strip()
    else:
        return int(num_str), string[match.end():].strip()