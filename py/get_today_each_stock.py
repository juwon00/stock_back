import FinanceDataReader as fdr

stock = fdr.DataReader('005930')
print(stock)

stock.to_csv("005930_2024_03_13.csv", header=False)
