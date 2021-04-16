#!/HDD/Code/Python/Scrapy/env/bin/python
""" Extract the curren pensing sv """

import json
import requests
from bs4 import BeautifulSoup

URL = 'http://ec2-34-211-116-208.us-west-2.compute.amazonaws.com:8080/'

with requests.session() as session:

    res = session.post(URL + 'login', data={'usr': 'Erick', 'pass': 'earroyo'})
    loginSoup = BeautifulSoup(res.content, 'html.parser')

    assert loginSoup.title.string != 'Iniciar sesion', 'Error al iniciar sesion'
    del loginSoup

    soup = BeautifulSoup(
            session.get(URL + 'temporal').content,
            'html.parser')

    dt = json.loads(soup.find('span', class_='d-none', id="data").attrs['data-json'])

    # filtrar valores
    with open('sv.json', 'w') as out:
        json.dump(
                list(
                    filter(
                        lambda e: e['valid'] == 1,
                        dt)
                    ), out)
