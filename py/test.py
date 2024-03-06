import FinanceDataReader as fdr
from datetime import datetime

now = datetime.now().date()
print(now)

stock = fdr.DataReader('005930', now)
print(stock)
