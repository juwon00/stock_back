import csv
from datetime import datetime

csv_file = 'data/data_2018~.csv'

# 데이터를 저장할 리스트 초기화
time_30 = []

with open(csv_file, 'r', newline='') as file:
    reader = csv.reader(file)
    for row in reader:
        # 각 열의 데이터 추출하여 해당 리스트에 저장
        time_30.append(row[0])


# 날짜를 파싱하여 datetime 객체로 변환
datetime_list = [datetime.fromisoformat(time_str) for time_str in time_30]

# 달이 바뀌는 인덱스와 년도가 바뀌는 인덱스 찾기
change_points = []
prev_month = None
prev_year = None
for i, dt in enumerate(datetime_list):
    if prev_month is None:
        prev_month = dt.month
    elif dt.month != prev_month:
        change_points.append(i)
        prev_month = dt.month
    if prev_year is None:
        prev_year = dt.year
    elif dt.year != prev_year:
        change_points.append(i)
        prev_year = dt.year

change_points.sort()  # 인덱스를 정렬하여 순서대로 저장

print("달과 년이 바뀌는 인덱스:", change_points)

print(change_points[0])
for i in range(1, len(change_points)):
    print(change_points[i] - change_points[i - 1])
