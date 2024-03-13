import FinanceDataReader as fdr

stocks = fdr.StockListing('KRX')[['Code', 'Name', 'Market']]
print(stocks)
stocks.to_csv("KRX2.csv", header=False, index=False)
