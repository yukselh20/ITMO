def is_valid_binary(input_str):
    return len(input_str) == 7 and all(bit in '01' for bit in input_str) 

def analyze_hamming_code(input_str):
    bits = [int(bit) for bit in input_str]
    c1 = bits[0] ^ bits[2] ^ bits[4] ^ bits[6]
    c2 = bits[1] ^ bits[2] ^ bits[5] ^ bits[6]
    c3 = bits[3] ^ bits[4] ^ bits[5] ^ bits[6]
    print(c1,c2,c3)
    error_position = (c1) + (c2 << 1) + (c3 << 2) 
    #Bit kaydırma, c2 1 birim sola kayar 1 se 10 101 se 1010 sağa ekstra 0 ekler.
    #1 1 0 olsa c1 c2 ve c3. c1 0 da 1 + 10 + 000 = 011
    print(error_position)
    
    
    if error_position != 0:
        print(f"Error detected at position: {error_position}")
        bits[error_position - 1] ^= 1 #changing corrupted bit
    else:
        print("No errors detected.")
    
    original_data = "".join(str(bits[i]) for i in [2, 4, 5, 6])
    print(f"Original data bits: {original_data}")

def main():
    while True:
        input_str = input("Enter a 7-digit binary number (only 0s and 1s): ")
        if is_valid_binary(input_str):
            break
        print("Invalid input. Please try again.")
    
    analyze_hamming_code(input_str)

if __name__ == "__main__":
    main()
