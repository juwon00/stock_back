from datetime import datetime, timedelta

import FinanceDataReader as fdr

now = datetime.now().date()

stock = fdr.DataReader("005930", now - timedelta(days=0))

print(stock)

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
