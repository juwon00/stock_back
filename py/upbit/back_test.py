from pprint import pprint

from upbit.tool import supplementary_indicator as si, minute30_to_hour as m30toh

# CSV 파일 경로
csv_file = 'coin_data/data_2018~.csv'

# candle -  1:30분, 2:1시간, 4:2시간, 8:4시간, 48:1일 ...
time_30, open_price, high_price, low_price, close_price, volume = m30toh.minutes30_to_hours(csv_file, 4)

# macd, macd_signal, macd_hist = si.cal_macd(close_price, 12, 26, 9)
# vw_macd, vw_macd_signal, vw_macd_hist = si.cal_vw_macd(close_price, volume, 12, 26, 9)
#
# buy = False
# purchase_price = 0
# purchase_time = 0
# money = 10000
# stop_loss = -0.5
# first_money = money
# rate_and_date = []
# for i in range(5, len(macd_hist)):
#
#     if not buy and macd_hist[i] > 0:
#         purchase_price = close_price[i]
#         purchase_time = time_30[i]
#         buy = True
#
#     elif buy and macd_hist[i] < 0:
#         rate = ((close_price[i] / purchase_price) - 1) * 100 - 0.1
#         # rate = max(rate, stop_loss)
#         money = round(money + (money * rate / 100), 2)
#         buy = False
#         rate_and_date.append((rate, purchase_time, time_30[i]))
#         # print(purchase_time, time_30[i], rate, money)
#         # print()
#
# rate_and_date.sort()
# pprint(rate_and_date[:30])
# rate_and_date.sort(reverse=True)
# pprint(rate_and_date[:30])
#
# print()
# print(first_money, money)
# print("total:", round(((money / first_money) - 1), 5) * 100, len(rate_and_date),
#       sum(1 for tpl in rate_and_date if tpl[0] == -0.5))
#
# buy = False
# purchase_price = 0
# purchase_time = 0
# money = 10000
# stop_loss = -0.5
# first_money = money
# rate_and_date = []
# for i in range(5, len(vw_macd_hist)):
#
#     if not buy and vw_macd_hist[i] > 0:
#         purchase_price = close_price[i]
#         purchase_time = time_30[i]
#         buy = True
#
#     elif buy and vw_macd_hist[i] < 0:
#         rate = ((close_price[i] / purchase_price) - 1) * 100
#         # rate = max(rate, stop_loss)
#         money = round(money + (money * rate / 100), 2)
#         buy = False
#         rate_and_date.append((rate, purchase_time, time_30[i]))
#         # print(purchase_time, time_30[i], rate, money)
#         # print()
#
# rate_and_date.sort()
# pprint(rate_and_date[:30])
# rate_and_date.sort(reverse=True)
# pprint(rate_and_date[:30])
#
# print()
# print(first_money, money)
# print("total:", round(((money / first_money) - 1), 5) * 100, len(rate_and_date),
#       sum(1 for tpl in rate_and_date if tpl[0] == -0.5))


super_trend = si.cal_super_trend(high_price, low_price, close_price, 10, 3)

buy = False
purchase_price = 0
purchase_time = 0
money = 10000
stop_loss = -3.2
first_money = money
rate_and_date = []
for i in range(1, len(super_trend)):

    # 매수 시기
    if not buy and super_trend[i][1] == 1 and super_trend[i - 1][1] == -1:
        purchase_price = close_price[i]
        purchase_time = time_30[i]
        buy = True

    # 손절이 아닌 매도 시기
    elif buy and super_trend[i][1] == -1 and super_trend[i - 1][1] == 1:
        rate = ((close_price[i] / purchase_price) - 1) * 100 - 0.1
        money = round(money + (money * rate / 100), 2)
        buy = False
        rate_and_date.append((rate, purchase_time, time_30[i]))

    # 손절 매도 시기
    elif buy and ((close_price[i] / purchase_price) - 1) * 100 <= stop_loss:
        rate = ((close_price[i] / purchase_price) - 1) * 100 - 0.1
        money = round(money + (money * rate / 100), 2)
        buy = False
        rate_and_date.append((rate, purchase_time, time_30[i]))

rate_and_date.sort()
pprint(rate_and_date[:20])
rate_and_date.sort(reverse=True)
pprint(rate_and_date[:20])

total_rate = round(((money / first_money) - 1), 5) * 100
average = sum(r[0] for r in rate_and_date) / len(rate_and_date)
stop_loss_count = sum(1 for tpl in rate_and_date if tpl[0] < stop_loss)
win_count = sum(1 for num in rate_and_date if num[0] > 0)
lose_count = len(rate_and_date) - win_count - stop_loss_count
win_rate = round(win_count / len(rate_and_date) * 100, 1)

print()
print(first_money, money)
print("총 수익률:", total_rate, "평균 수익률:", average)
print("총 거래수:", len(rate_and_date))
print("수익 거래수:", win_count, "손실 거래수:", lose_count, "손절:", stop_loss_count, "승률:", win_rate)
