#!/HDD/Code/Python/Scrapy/env/bin/python
""" Extract the curren pensing sv """

import json
from os import environ as env
import requests
from flask import Flask
from bs4 import BeautifulSoup

URL = env['URL']

app = Flask(__name__)


@app.route('/accounts')
def get_accs():
    with requests.session() as session:

        res = session.post(URL + 'login', data={'usr': 'Erick', 'pass': 'earroyo'})
        loginSoup = BeautifulSoup(res.content, 'html.parser')

        assert loginSoup.title.string != 'Iniciar sesion', 'Error al iniciar sesion'
        del loginSoup

        soup = BeautifulSoup(
                session.get(URL + 'temporal').content,
                'html.parser')

        dt = json.loads(soup.find('span', class_='d-none', id="data").attrs['data-json'])
    return json.dumps(dt)

def save():
    """ filtrar valores """
    with open('sv.json', 'w') as out:
        json.dump(
                list(
                    filter(
                        lambda e: e['valid'] == 1,
                        dt)
                    ), out)
