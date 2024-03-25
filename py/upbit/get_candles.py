import numpy as np
import requests
import json
import talib
import time


def send_api(path, method):
    API_HOST = "https://api.upbit.com/v1"
    url = API_HOST + path
    headers = {'Content-Type': 'application/json', 'charset': 'UTF-8', 'Accept': '*/*'}
    body = {}

    try:
        if method == 'GET':
            response = requests.get(url, headers=headers)
        elif method == 'POST':
            response = requests.post(url, headers=headers, data=json.dumps(body, ensure_ascii=False, indent="\t"))
        # print("response status %r" % response.status_code)
        # print(json.dumps(response.json(), indent=3))
        return response.json()
    except Exception as ex:
        print(ex)


# 2017-10-01 ~ 2024-03-22 까지의 데이터 수집

open_arr = np.array([])
high_arr = np.array([])
low_arr = np.array([])
close_arr = np.array([])
volume_arr = np.array([])
time_30_arr = np.array([])

for year in range(24, 25):  # 기본 (17, 25)
    print("year", year)

    if year == 17:
        s = 10
    else:
        s = 1

    for month in range(3, 4):  # 기본 (s, 13)
        if 1 <= month < 10:
            m = "0" + str(month)
        else:
            m = str(month)
        print("month", m)

        odd_month = [1, 3, 5, 7, 9, 11]
        even_month = [4, 6, 8, 10, 12]

        if month in odd_month:
            end = 32
        elif month in even_month:
            end = 31
        elif year == 20 or year == 24:
            end = 30
        else:
            end = 29

        for date in range(1, 6):  # 기본 (1, end)
            if year == 24 and month == 3 and date >= 23:
                break
            if year == 24 and month >= 4:
                break

            time.sleep(0.1)
            if 1 <= date < 10:
                d = "0" + str(date)
            else:
                d = str(date)
            print("date", date)

            # 15:00:00 -> 한국시간으로 00:00:00
            response = send_api(
                "/candles/minutes/30?market=KRW-BTC&count=48&to=20" + str(year) + "-" + m + "-" + d + " 15:00:00",
                "GET")[
                       ::-1]
            # print(json.dumps(response, indent=3))

            open = [item["opening_price"] for item in response]
            high = [item["high_price"] for item in response]
            low = [item["low_price"] for item in response]
            close = [item["trade_price"] for item in response]
            volume = [item["candle_acc_trade_volume"] for item in response]
            time_30 = [item["candle_date_time_kst"] for item in response]

            open_arr = np.append(open_arr, open)
            high_arr = np.append(high_arr, high)
            low_arr = np.append(low_arr, low)
            close_arr = np.append(close_arr, close)
            volume_arr = np.append(volume_arr, volume)
            time_30_arr = np.append(time_30_arr, time_30)

print(close_arr)

sma_60 = np.round(talib.SMA(close_arr, timeperiod=60), decimals=1)
sma_120 = np.round(talib.SMA(close_arr, timeperiod=120), decimals=1)
sma_200 = np.round(talib.SMA(close_arr, timeperiod=200), decimals=1)
upper, middle, lower = np.round(talib.BBANDS(close_arr, timeperiod=20), decimals=1)
macd, signal, macd_hist = np.round(talib.MACD(close_arr, fastperiod=12, slowperiod=26, signalperiod=9), decimals=1)
adx = np.round(talib.ADX(high=high_arr, low=low_arr, close=close_arr, timeperiod=14), decimals=1)
rsi = np.round(talib.RSI(close_arr, timeperiod=14), decimals=1)
slowk, slowd = np.round(talib.STOCH(high_arr, low_arr, close_arr, fastk_period=14, slowk_period=3, slowd_period=3),
                        decimals=1)

# for i in range(len(close_arr)):
#     print(time_30_arr[i], open_arr[i], high_arr[i], low_arr[i], close_arr[i], volume_arr[i], sma_60[i], sma_120[i],
#           sma_200[i], upper[i], middle[i], lower[i], macd[i], signal[i], macd_hist[i], adx[i], rsi[i], slowk[i],
#           slowd[i])

result = np.vstack((time_30_arr, open_arr, high_arr, low_arr, close_arr, volume_arr, sma_60, sma_120, sma_200, upper,
                    middle, lower, macd, signal, macd_hist, adx, rsi, slowk, slowd))
print(result)

result = result.T
print(result)

np.savetxt('data.csv', result, delimiter=',', fmt='%s')
