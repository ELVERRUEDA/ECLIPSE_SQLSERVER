package application.IoT_Application;

import java.util.UUID;

import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttException;

public class App {
    public static MqttAsyncClient myClient;

    public static void main(String[] args) throws MqttException {
        myClient = new MqttAsyncClient("tcp://192.168.99.206:1883", UUID.randomUUID().toString());
        MyCallback myCallback = new MyCallback();
        myClient.setCallback(myCallback);

        IMqttToken token = myClient.connect();
        token.waitForCompletion();

        System.out.println("Conectado al servidor MQTT");

        myClient.subscribe("esp32/dht11/datos", 0);

        System.out.println("Suscrito a el tópico: esp32/dht11/datos");
    }
}
