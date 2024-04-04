from datetime import timedelta, datetime

from upbit.tool import send_api as sa, supplementary_indicator as si

now = datetime.now() - timedelta(hours=9.5)
now = now.strftime("%Y-%m-%d %H:%M:%S")
response = sa.send_api("/candles/minutes/30?market=KRW-BTC&count=200&to=" + now, "GET")[::-1]

close = [item["trade_price"] for item in response]
volume = [item["candle_acc_trade_volume"] for item in response]
time_30 = [item["candle_date_time_kst"] for item in response]

macd, macd_signal, macd_hist = si.cal_macd(close, 12, 26, 9)
vw_macd, vw_macd_signal, vw_macd_hist = si.cal_vw_macd(close, volume, 12, 26, 9)

print(time_30[-5:])
print(vw_macd_hist[-5:])
if (vw_macd_hist[-1] > 0 and vw_macd_hist[-2] > 0) or (vw_macd_hist[-1] < 0 and vw_macd_hist[-2] < 0):
    print("hold")
elif vw_macd_hist[-1] > 0:
    print("buy")
else:
    print("sell")
