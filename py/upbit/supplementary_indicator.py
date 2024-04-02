import numpy as np
import talib


def cal_vwma(close, volume, period):
    vwma = [0] * (period - 1)
    for i in range(period, len(close) + 1):
        sum_close_volume = sum(c * v for c, v in zip(close[i - period:i], volume[i - period:i]))
        sum_volume = sum(volume[i - period:i])
        vwma.append(round(sum_close_volume / sum_volume, 1))
    return vwma


def cal_macd(close, fast, slow, signal):
    macd, macd_signal, macd_hist = np.round(
        talib.MACD(np.array(close), fastperiod=fast, slowperiod=slow, signalperiod=signal), decimals=1)
    macd = [round(num, 1) for num in macd]
    macd_signal = [round(num, 1) for num in macd_signal]
    macd_hist = [round(num, 1) for num in macd_hist]
    return macd, macd_signal, macd_hist


def cal_vw_macd(close, volume, short_term, long_term, signal):
    vwma_s = cal_vwma(close, volume, short_term)
    vwma_l = cal_vwma(close, volume, long_term)
    vw_macd = [round(s - l, 1) if s != 0 and l != 0 else 0 for s, l in zip(vwma_s, vwma_l)]
    vw_macd_signal = [0] * (long_term - 1) + cal_vwma(vw_macd[long_term - 1:], volume, signal)
    vw_macd_hist = [round(x - y, 1) if x != 0 and y != 0 else 0 for x, y in zip(vw_macd, vw_macd_signal)]
    return vw_macd, vw_macd_signal, vw_macd_hist
