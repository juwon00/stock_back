import csv


# 데이터 분할 함수
def split_data(data, n):
    for i in range(0, len(data), n):
        yield data[i:i + n]


# 데이터 처리 함수
def process_data(data, candle):
    # 데이터 분할
    chunks = list(split_data(data, candle))
    dates_list, opens_list, highs_list, lows_list, closes_list, volumes_list = [], [], [], [], [], []

    for chunk in chunks:
        # 데이터 초기화
        dates, opens, highs, lows, closes, volumes = [], [], [], [], [], []

        total_volume = 0  # 거래량 합계 초기화
        for row in chunk:
            dates.append(row[0])  # 날짜 데이터 변환
            opens.append(float(row[1]))  # 시가 데이터 추가
            highs.append(float(row[2]))  # 고가 데이터 추가
            lows.append(float(row[3]))  # 저가 데이터 추가
            closes.append(float(row[4]))  # 종가 데이터 추가
            volumes.append(float(row[5]))  # 거래량 데이터 추가
            total_volume += float(row[5])  # 거래량 합계 업데이트

        # 고가와 저가 처리
        max_high = max(highs)  # 최대 고가
        min_low = min(lows)  # 최소 저가

        # 결과 저장
        dates_list.append(dates[0])
        opens_list.append(opens[0])
        highs_list.append(max_high)
        lows_list.append(min_low)
        closes_list.append(closes[-1])
        volumes_list.append(total_volume)

    return dates_list, opens_list, highs_list, lows_list, closes_list, volumes_list


def minutes30_to_hours(file_path, candle):
    # 파일 읽기
    with open(file_path, 'r') as file:
        reader = csv.reader(file)
        data = list(reader)

    # 데이터 처리
    dates, opens, highs, lows, closes, volumes = process_data(data, candle)
    return dates, opens, highs, lows, closes, volumes


def minutes60_to_hours(high, low, close, volume, time_60):
    highs_list, lows_list, closes_list, volumes_list, dates_list = [], [], [], [], []

    for i in range(0, len(high), 2):
        highs_list.append(max(high[i], high[i + 1]))
        lows_list.append(min(low[i], low[i + 1]))
        closes_list.append(close[i + 1])
        volumes_list.append(volume[i] + volume[i + 1])
        dates_list.append(time_60[i])

    return highs_list, lows_list, closes_list, volumes_list, dates_list
