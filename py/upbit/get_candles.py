import numpy as np
import requests
import json
import talib
import math


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
        print("response status %r" % response.status_code)
        # print(json.dumps(response.json(), indent=3))
        return response.json()
    except Exception as ex:
        print(ex)


total_rate = 0
buy = False
purchase_price = 0
for date in range(16, 27):  # i일 부터 j일 까지 데이터
    print(date)
    if 1 <= date < 10:
        d = "0" + str(date)
    else:
        d = str(date)

    # 15:00:00 -> 한국시간으로 00:00:00
    response = send_api("/candles/minutes/30?market=KRW-BTC&count=200&to=2024-02-" + d + " 15:00:00", "GET")[::-1]
    # print(json.dumps(response, indent=3))

    # 호출 예시
    # response = send_api("/candles/minutes/15?market=KRW-BTC&count=200&to=2024-03-18 15:00:00", "GET")[::-1]
    # print(json.dumps(response, indent=3))

    open = np.array([item["opening_price"] for item in response])
    high = np.array([item["high_price"] for item in response])
    low = np.array([item["low_price"] for item in response])
    close = np.array([item["trade_price"] for item in response])
    volume = np.array([item["candle_acc_trade_volume"] for item in response])
    time = np.array([item["candle_date_time_kst"] for item in response])

    macd, signal, macd_hist = np.round(talib.MACD(close, fastperiod=12, slowperiod=26, signalperiod=9), decimals=2)
    adx = np.round(talib.ADX(high=high, low=low, close=close, timeperiod=14), decimals=2)
    rsi = np.round(talib.RSI(close, timeperiod=14), decimals=2)
    slowk, slowd = np.round(talib.STOCH(high, low, close, fastk_period=14, slowk_period=3, slowd_period=3), decimals=2)


    # def macd_turn(i, macd):
    #     if macd[i - 4] > macd[i - 3] > macd[i - 2] > macd[i - 1] and macd[i - 1] < macd[i]:
    #         print("macd1")
    #         return True
    #     elif macd[i - 4] > macd[i - 3] > macd[i - 2] and macd[i - 2] < macd[i - 1] < macd[i]:
    #         print("macd2")
    #         return True
    #     # elif macd[i - 4] > macd[i - 3] > macd[i - 2] > macd[i - 1] > macd[i]:
    #     #     print("macd3")
    #     #     return True
    #     return False
    #
    #
    # def adx_turn(i, adx):
    #     if adx[i - 4] < adx[i - 3] < adx[i - 2] < adx[i - 1] and adx[i - 1] > adx[i]:
    #         print("adx1")
    #         return True
    #     elif adx[i - 4] < adx[i - 3] < adx[i - 2] and adx[i - 2] > adx[i - 1] > adx[i]:
    #         print("adx2")
    #         return True
    #     return False
    #
    #
    # def buy_condition(i, buy, macd, signal, adx, rsi, slowk, time, volume):
    #     if not buy and macd_turn(i, macd) and adx_turn(i, adx) and adx[i] > 30:
    #         return True
    #     return False
    #
    #
    # def sell_condition(i, buy, macd, signal, macd_hist, adx, rsi, slowk, time, volume):
    #     if buy and macd[i - 1] > signal[i - 1] and macd[i] <= signal[i]:
    #         return True
    #
    #     return False

    def buy_condition(i, buy, macd, signal, macd_hist, adx, rsi, slowk, time, volume):
        if not buy and macd_hist[i - 1] < 0 and macd_hist[i] > 0:
            return True

        return False


    def sell_condition(i, buy, macd, signal, macd_hist, adx, rsi, slowk, time, volume):
        if buy and macd_hist[i] - macd_hist[i - 1] < 0:
            return True

        return False


    rate_of_return = 0
    for i in range(152, len(macd)):
        # print(close[i], "    ", macd[i], "   ", signal[i], "    ", macd_hist[i], "    ", adx[i], "    ", rsi[i], "    ",
        #       slowk[i], "    ", time[i], "    ", i)
        if math.isnan(macd[i - 4]):
            print("nan")
            continue

        if buy_condition(i, buy, macd, signal, macd_hist, adx, rsi, slowk, time, volume):
            print()
            print(time[i], "find", close[i], "   ")
            buy = True
            purchase_price = close[i]

        if sell_condition(i, buy, macd, signal, macd_hist, adx, rsi, slowk, time, volume):
            print()
            print(time[i], "sell", close[i], "   ")
            buy = False
            rate_of_return += ((close[i] / purchase_price) - 1) * 100 - 0.1  # 매수 매도 수수료 각 0.05%
            print(rate_of_return)
            print()

    total_rate += rate_of_return

print("==== total_rate: ", total_rate, "====")
