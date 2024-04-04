import json

import requests


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
