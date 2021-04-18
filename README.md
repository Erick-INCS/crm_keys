# GRSC-CRM Database reconstruction (as microservices)
This project contains two web services with the objetive of complete the records of the clients of GR Soluciones Computacionales.

## Pyget
Python Web Service for data collection (Web scraping).

## SQL Generator
Scala Web Service that traduces the data fetched by `Pyget WS` and inserts the corresponding data in the CRM database.
