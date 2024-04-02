import csv
from pprint import pprint

import supplementary_indicator as si

# CSV 파일 경로
csv_file = 'data/data_2018~.csv'

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

# x, y = -200, -1
# print(macd[x:y], len(macd))
# print(vw_macd[x:y], len(vw_macd))
# print(macd_hist[x:y], len(macd_hist))
# print(vw_macd_hist[x:y], len(vw_macd_hist))
# t_datetime = [datetime.fromisoformat(time_str) for time_str in time_30[x:y]]
# # subplot 생성
# fig, axs = plt.subplots(2, 1, figsize=(10, 8))
#
# # 첫 번째 subplot 그리기
# axs[0].plot(t_datetime, macd[x:y], label='macd')
# axs[0].plot(t_datetime, macd_signal[x:y], label='macd_signal')
# axs[0].set_title('MACD')
# axs[0].set_xlabel('Date and Time')
# axs[0].set_ylabel('Values')
# axs[0].legend()
# axs[0].xaxis.set_major_formatter(mdates.DateFormatter('%Y-%m-%d %H:%M:%S'))
# axs[0].tick_params(rotation=45)
#
# # MACD 히스토그램 그리기
# axs_macd_hist = axs[0].twinx()  # 같은 x축을 공유하면서 y축을 별도로 추가
# axs_macd_hist.bar(t_datetime, macd_hist[x:y], color='red', alpha=0.5, width=0.025, label='macd_hist')
# axs_macd_hist.set_ylabel('MACD Histogram')
# axs_macd_hist.legend()
#
# # 왼쪽 subplot에 0값 수직선 그리기
# axs[0].axhline(0, color='black', linestyle='--', linewidth=1)
#
# # 두 번째 subplot 그리기
# axs[1].plot(t_datetime, vw_macd[x:y], label='vw_macd')
# axs[1].plot(t_datetime, vw_macd_signal[x:y], label='vw_macd_signal')
# axs[1].set_title('VW_MACD')
# axs[1].set_xlabel('Date and Time')
# axs[1].set_ylabel('Values')
# axs[1].legend()
# axs[1].xaxis.set_major_formatter(mdates.DateFormatter('%Y-%m-%d %H:%M:%S'))
# axs[1].tick_params(rotation=45)
#
# # VW_MACD 히스토그램 그리기
# axs_vw_macd_hist = axs[1].twinx()  # 같은 x축을 공유하면서 y축을 별도로 추가
# axs_vw_macd_hist.bar(t_datetime, vw_macd_hist[x:y], color='blue', alpha=0.5, width=0.025, label='vw_macd_hist')
# axs_vw_macd_hist.set_ylabel('VW_MACD Histogram')
# axs_vw_macd_hist.legend()
#
# # 오른쪽 subplot에 0값 수직선 그리기
# axs[1].axhline(0, color='black', linestyle='--', linewidth=1)
#
# plt.tight_layout()  # 레이아웃 조정
# plt.show()

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
        rate = max(rate, stop_loss)
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
        rate = ((close_price[i] / purchase_price) - 1) * 100 - 0.1
        rate = max(rate, stop_loss)
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
