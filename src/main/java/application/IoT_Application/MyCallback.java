package application.IoT_Application;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MyCallback implements MqttCallback {

    private static final String DB_URL =   "jdbc:sqlserver://SB-BOGOTA\\SQLEXPRESS:1433;databaseName=MonitoreoSystem;encrypt=true;trustServerCertificate=true";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "sa";

    @Override
    public void connectionLost(Throwable cause) {
        System.out.println("Connection lost: " + cause.getMessage());
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String payload = new String(message.getPayload());
        System.out.println("Received message: " + payload);
        processAndSaveData(payload);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
       
    }

    private void processAndSaveData(String message) {
        
        String[] data = message.split(",");
        if (data.length == 4) {
            try {
                float temperature = Float.parseFloat(data[0].split(":")[1].trim());
                float humidity = Float.parseFloat(data[1].split(":")[1].trim());
                float soilHumidity = Float.parseFloat(data[2].split(":")[1].trim());
                boolean relayState = Integer.parseInt(data[3].split(":")[1].trim()) == 1;

                saveToDatabase(temperature, humidity, relayState, soilHumidity);
            } catch (NumberFormatException e) {
                System.err.println("Error parsing data: " + e.getMessage());
            }
        } else {
            System.err.println("Invalid data format: " + message);
        }
    }

    private void saveToDatabase(float temperature, float humidity, boolean relayState, float soilHumidity) {
        String insertSQL = "INSERT INTO datos_esp32 (Temperature, Humedad, Estado_Relay, HumedadSuelo) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {

            pstmt.setFloat(1, temperature);
            pstmt.setFloat(2, humidity);
            pstmt.setBoolean(3, relayState);
            pstmt.setFloat(4, soilHumidity);
            pstmt.executeUpdate();

            System.out.println("Data saved to database");

        } catch (SQLException e) {
            System.err.println("Error saving to database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
