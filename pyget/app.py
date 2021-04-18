#!/HDD/Code/Python/Scrapy/env/bin/python
""" Extract the curren pensing sv """

import json
from os import environ as env
import requests
from flask import Flask
from bs4 import BeautifulSoup

if ('URL' in env and 'CRM_USR' in env and 'CRM_PASS' in env):

    URL = env['URL']
    USR = env['CRM_USR']
    PSS = env['CRM_PASS']


    app = Flask(__name__)

    @app.route('/accounts')
    def get_accs():
        """ method that fetchs the pendign data in the CRM """
        with requests.session() as session:

            res = session.post(URL + 'login', data={'usr': USR, 'pass': PSS})
            login_soup = BeautifulSoup(res.content, 'html.parser')

            msg = 'Error al iniciar sesion'
            assert login_soup.title.string != 'Iniciar sesion', msg
            del login_soup

            soup = BeautifulSoup(
                    session.get(URL + 'temporal').content,
                    'html.parser')

            data = json.loads(soup.find(
                'span',
                class_='d-none',
                id="data"
            ).attrs['data-json'])

        return json.dumps(data)

else:
    print('Please be sure of have the variables ' +
          '"USR", "CRM_USR", "CRM_PASS" declared.')


def save(data):
    """ filtrar valores """
    with open('sv.json', 'w') as out:
        json.dump(
                list(
                    filter(
                        lambda e: e['valid'] == 1,
                        data)
                    ), out)
