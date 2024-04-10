import csv
from collections import defaultdict

# 파일 경로
file_path = "../data/data_2018~.csv"

# 날짜별 데이터 개수를 저장할 딕셔너리
daily_data_counts = defaultdict(int)

# 파일 읽기
with open(file_path, 'r', newline='') as file:
    reader = csv.reader(file)
    for row in reader:
        date = row[0][:10]  # 날짜 추출
        daily_data_counts[date] += 1

# 결과 출력
for date, count in sorted(daily_data_counts.items()):
    if count != 48:
        print(f"{date}: {count}개의 데이터")
