import sys
from datetime import datetime

import FinanceDataReader as fdr

data = sys.argv[1:]
code = data[0]

now = datetime.now().date()

stock = fdr.DataReader(code, now)

open = stock.iloc[0, 0]
high = stock.iloc[0, 1]
low = stock.iloc[0, 2]
close = stock.iloc[0, 3]
volume = stock.iloc[0, 4]

print(open)
print(high)
print(low)
print(close)
print(volume)
