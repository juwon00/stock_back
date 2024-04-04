import csv
from pprint import pprint

from upbit.tool import supplementary_indicator as si

# CSV 파일 경로
csv_file = 'data/2024/data_2024_03.csv'

# 데이터를 저장할 리스트 초기화
time_30 = []
open_price = []
high_price = []
low_price = []
close_price = []
volume = []

# CSV 파일 읽기
with open(csv_file, 'r', newline='') as file:
    reader = csv.reader(file)
    for row in reader:
        # 각 열의 데이터 추출하여 해당 리스트에 저장
        time_30.append(row[0])
        open_price.append(float(row[1]))
        high_price.append(float(row[2]))
        low_price.append(float(row[3]))
        close_price.append(float(row[4]))
        volume.append(float(row[5]))

macd, macd_signal, macd_hist = si.cal_macd(close_price, 12, 26, 9)
vw_macd, vw_macd_signal, vw_macd_hist = si.cal_vw_macd(close_price, volume, 12, 26, 9)

buy = False
purchase_price = 0
purchase_time = 0
money = 10000
stop_loss = -0.5
first_money = money
rate_and_date = []
for i in range(5, len(macd_hist)):

    if not buy and macd_hist[i] > 0:
        purchase_price = close_price[i]
        purchase_time = time_30[i]
        buy = True

    elif buy and macd_hist[i] < 0:
        rate = ((close_price[i] / purchase_price) - 1) * 100 - 0.1
        # rate = max(rate, stop_loss)
        money = round(money + (money * rate / 100), 2)
        buy = False
        rate_and_date.append((rate, purchase_time, time_30[i]))
        # print(purchase_time, time_30[i], rate, money)
        # print()

rate_and_date.sort()
pprint(rate_and_date[:30])
rate_and_date.sort(reverse=True)
pprint(rate_and_date[:30])

print()
print(first_money, money)
print("total:", round(((money / first_money) - 1), 5) * 100, len(rate_and_date),
      sum(1 for tpl in rate_and_date if tpl[0] == -0.5))

buy = False
purchase_price = 0
purchase_time = 0
money = 10000
stop_loss = -0.5
first_money = money
rate_and_date = []
for i in range(5, len(vw_macd_hist)):

    if not buy and vw_macd_hist[i] > 0:
        purchase_price = close_price[i]
        purchase_time = time_30[i]
        buy = True

    elif buy and vw_macd_hist[i] < 0:
        rate = ((close_price[i] / purchase_price) - 1) * 100
        # rate = max(rate, stop_loss)
        money = round(money + (money * rate / 100), 2)
        buy = False
        rate_and_date.append((rate, purchase_time, time_30[i]))
        # print(purchase_time, time_30[i], rate, money)
        # print()

rate_and_date.sort()
pprint(rate_and_date[:30])
rate_and_date.sort(reverse=True)
pprint(rate_and_date[:30])

print()
print(first_money, money)
print("total:", round(((money / first_money) - 1), 5) * 100, len(rate_and_date),
      sum(1 for tpl in rate_and_date if tpl[0] == -0.5))


pprint(rate_and_date)