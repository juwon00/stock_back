from datetime import timedelta, datetime

from tool import send_api as sa, supplementary_indicator as si

now = datetime.now() - timedelta(hours=9.5)  # **시 00분 01초 or **시 30분 01초에 호출
now = now.strftime("%Y-%m-%d %H:%M:%S")
response = sa.send_api("/candles/minutes/30?market=KRW-BTC&count=200&to=" + now, "GET")[::-1]

close = [item["trade_price"] for item in response]
high = [item["high_price"] for item in response]
low = [item["low_price"] for item in response]
volume = [item["candle_acc_trade_volume"] for item in response]
time_30 = [item["candle_date_time_kst"] for item in response]

super_trend = si.cal_super_trend(high, low, close, 10, 3)

previous = super_trend[-2][1]
current = super_trend[-1][1]

if previous == current:
    print("hold", close[-1])
elif previous == 1 and current == -1:
    print("sell")
elif previous == -1 and current == 1:
    print("buy", close[-1])
