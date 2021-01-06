# spring-jms-mongo

Make sure you have activemq installed and running. 
Check by visiting `http://localhost:8161/`

Run elasticsearch, kibana in seperate windows. 
Make sure to change your activemq jar file path in the `logstash.conf` file replace entry corresponding to `require_jars`.
Run logstash with `logstash.conf` file in this repo.
You might have to install a plugin like - `bin/logstash-plugin install logstash-input-jms` from your logstash installed directory. 

Go to your Kibana discover tab and create an index - `logstash-*`

Make sure you have MongoDB running.

Now, everything should be setup. 

Run the spring app using `mvn spring-boot:run`

Hopefully, everything runs fine. Now, visit `localhost:8090/available`, this sends a message to the queue. Check your queue by visiting `localhost:8161`, you should see a queue with the name `helloworld.q`.

Now, visit you kibana discover tab, you should see the message there as well. This completes the app -> activemq -> logstash -> elasticsearch -> kibana cycle. 

Now, visit, `localhost:8090/save`, this should save a few entries in mongodb. Access mongoshell to check if it was store. DB name would be `test` and collection name `customer`.

Visit localhost:8090/client to access the client page.

We now need to play around with this and create a crud app inside this project.
