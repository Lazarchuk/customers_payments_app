UI

Start UI page:
localhost:9966/

Get client accounts by id
localhost:9966/view/clients/get

Create client
http://localhost:9966/view/clients/create

Create payments
http://localhost:9966/view/payments/create

Create filter
http://localhost:9966/view/filters/create

REST
Create client, return json
localhost:9966/rest/v1/clients/create/json

Create client, return xml
localhost:9966/rest/v1/clients/create/xml

Get client accounts, return json
localhost:9966/rest/v1/clients/{id}/json

Get client accounts, return xml
localhost:9966/rest/v1/clients/{id}/xml

Create payment, return json
localhost:9966/rest/v1/payments/create/json

Create payment, return xml
localhost:9966/rest/v1/payments/create/xml

Create many payments from json
localhost:9966/rest/v1/payments/createmany/fromjson?=responseType

Create many payments from xml
localhost:9966/rest/v1/payments/createmany/fromxml?responseType=

Create filter, return json
localhost:9966//rest/v1/journal/json

Create filter, return xml
localhost:9966//rest/v1/journal/xml