import numpy as np
import requests
import json
import talib


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
        return response.json()
    except Exception as ex:
        print(ex)


d = "23"
response = send_api("/candles/minutes/1?market=KRW-BTC&count=200&to=2024-03-" + d + " 15:00:00", "GET")[::-1]

open = np.array([item["opening_price"] for item in response])
high = np.array([item["high_price"] for item in response])
low = np.array([item["low_price"] for item in response])
close = np.array([item["trade_price"] for item in response])
volume = np.array([item["candle_acc_trade_volume"] for item in response])
time_30 = np.array([item["candle_date_time_kst"] for item in response])

sma_60 = np.round(talib.SMA(close, timeperiod=60), decimals=1)
sma_120 = np.round(talib.SMA(close, timeperiod=120), decimals=1)
sma_200 = np.round(talib.SMA(close, timeperiod=200), decimals=1)
upper, middle, lower = np.round(talib.BBANDS(close, timeperiod=20), decimals=1)
macd, signal, macd_hist = np.round(talib.MACD(close, fastperiod=12, slowperiod=26, signalperiod=9), decimals=1)
adx = np.round(talib.ADX(high=high, low=low, close=close, timeperiod=14), decimals=1)
rsi = np.round(talib.RSI(close, timeperiod=14), decimals=1)
slowk, slowd = np.round(talib.STOCH(high, low, close, fastk_period=14, slowk_period=3, slowd_period=3), decimals=1)

print(time_30[-1], open[-1], high[-1], low[-1], close[-1], volume[-1], sma_60[-1], sma_120[-1], sma_200[-1], upper[-1],
      middle[-1], lower[-1], macd[-1], signal[-1], macd_hist[-1], adx[-1], rsi[-1], slowk[-1], slowd[-1])
