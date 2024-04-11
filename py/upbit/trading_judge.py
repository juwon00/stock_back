from datetime import timedelta, datetime

from tool import send_api as sa, supplementary_indicator as si, minute30_to_hour as m60toh

now = datetime.now() - timedelta(hours=10)  # 00시 부터 2시간 마다 매일
now = now.strftime("%Y-%m-%d %H:%M:%S")
response = sa.send_api("/candles/minutes/60?market=KRW-BTC&count=200&to=" + now, "GET")[::-1]

close = [item["trade_price"] for item in response]
high = [item["high_price"] for item in response]
low = [item["low_price"] for item in response]
volume = [item["candle_acc_trade_volume"] for item in response]
time_60 = [item["candle_date_time_kst"] for item in response]

high_120, low_120, close_120, volume_120, time_120 = m60toh.minutes60_to_hours(high, low, close, volume, time_60)
super_trend = si.cal_super_trend(high_120, low_120, close_120, 10, 3)

previous = super_trend[-2][1]
current = super_trend[-1][1]

if previous == current:
    print("hold", close[-1])
elif previous == 1 and current == -1:
    print("sell", close[-1])
elif previous == -1 and current == 1:
    print("buy", close[-1])
