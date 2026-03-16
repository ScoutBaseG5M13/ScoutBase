# Scoutbase API

### Swagger of Scoutbase API
https://scoutbase-dev.onrender.com/swagger-ui/index.html

### Download de YAML for Postman:
https://scoutbase-dev.onrender.com/v3/api-docs.yaml

### API Response format
##### · Status 200 OK responses from the API will always be enveloped like:

{\
&nbsp;&nbsp;&nbsp;&nbsp;"success": true,\
&nbsp;&nbsp;&nbsp;&nbsp;"message": "Success",\
&nbsp;&nbsp;&nbsp;&nbsp;"data": <span style="color: BLUE">{{ DATA }}</span>,\
&nbsp;&nbsp;&nbsp;&nbsp;"sessionId": 4bcbfa9a-c2aa-48bc-80ef-e39f5c79fbeb</span>,\
&nbsp;&nbsp;&nbsp;&nbsp;"timestamp": "2026-03-13T19:10:34.382786944"\
}

Where "data" is the requested data response.

##### · Status 400-599 responses will be enveloped like:
{\
&nbsp;&nbsp;&nbsp;&nbsp;"success": false,\
&nbsp;&nbsp;&nbsp;&nbsp;"message": <span style="color: BLUE">{{ ERROR MESSAGE }}</span>,\
&nbsp;&nbsp;&nbsp;&nbsp;"data": null,\
&nbsp;&nbsp;&nbsp;&nbsp;"sessionId": 934936cf-cfd7-4fe1-9884-e6ebe82c2ebc,\
&nbsp;&nbsp;&nbsp;&nbsp;"timestamp": "2026-03-13T19:14:41.912408322"\
}

Where "message" is the error message.