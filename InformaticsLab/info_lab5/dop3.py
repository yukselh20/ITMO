import pandas as pd
import matplotlib.pyplot as plt
import matplotlib

# Ensure the correct backend is used
matplotlib.use('TkAgg')

# Load CSV file
df = pd.read_csv('dop2.csv', sep=';')

# Replace commas with periods
df = df.replace(',', '.', regex=True)

# Convert columns to numeric
df = df.apply(pd.to_numeric)

# Plot the boxplot
plt.figure(figsize=(12, 8))
df.boxplot()
plt.title('Boxplot of Stock Prices')
plt.ylabel('Stock Prices')
plt.xticks(rotation=45, ha='right')
plt.tight_layout()

plt.show() 

