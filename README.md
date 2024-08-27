# TraceIp

TraceIp is a simple Spring Boot app that allows you to trace any given IP address.

The two available endpoints are: ```/lookup``` and ```/stats```. The first one allows you to trace an IP address and the second one allows you to see the statistics of the traced IP addresses. ```/stats``` is available as GET and POST method while ```/lookup``` is available as a basic form from any browser.


To run the app, first go to ```src/main/resources/application.properties``` and add your dev.me api key in ```dev.me.api.key``` and your ipapi key in ```ipapi.key```. You need to install docker, then go to the root directory of the project and run the following command:

```bash
docker-compose up
```

And that's it! A redis instance, a MongoDB instance will be created and the app will build and run.

The app will be available at ```http://localhost:8090```. You should browse to ```http://localhost:8090/lookup``` to perform the ip address lookup or ```http://localhost:8090/stats``` to see the statistics.