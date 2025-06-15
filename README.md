Sigue estos pasos para poner en marcha el proyecto:

### 1. **Configuración de la Base de Datos (SQL Server)**

* Asegúrate de tener una instancia de SQL Server accesible.
* Crea una nueva base de datos llamada, por ejemplo, `iot_data_db`.
* Ejecuta el siguiente script SQL para crear la tabla de telemetría:
    ```sql
    CREATE TABLE SensorReadings (
        ID INT PRIMARY KEY IDENTITY(1,1),
        Timestamp DATETIME DEFAULT GETDATE(),
        Temperature DECIMAL(5,2),
        Humidity DECIMAL(5,2),
        RelayState BIT
    );
    -- Puedes añadir una tabla para el historial de comandos del relé si lo implementas
    -- CREATE TABLE RelayCommands ( ... );
    ```
* Configura las credenciales de acceso para tu aplicación Java.

### 2. **Configuración del Broker MQTT**

* Si usas un broker local (ej. Mosquitto), asegúrate de que esté instalado y ejecutándose.
* Si usas un servicio en la nube (ej. HiveMQ Cloud, AWS IoT), obtén las credenciales y la URL del broker.
* Asegúrate de que los puertos (por defecto 1883 para MQTT, 8883 para MQTTS) estén abiertos y accesibles.

### 3. **Firmware de la ESP32 (Visual Studio Code + PlatformIO)**

* Abre la carpeta `ESP32_Firmware` (o como hayas nombrado tu proyecto de PlatformIO) en Visual Studio Code.
* Abre el archivo `src/main.cpp`.
* **Actualiza las credenciales Wi-Fi:**
    ```cpp
    const char* ssid = "[TU_SSID_WIFI]";
    const char* password = "[TU_PASSWORD_WIFI]";
    ```
* **Actualiza la configuración del Broker MQTT:**
    ```cpp
    const char* mqtt_server = "[DIRECCION_IP_O_HOST_DE_TU_BROKER_MQTT]";
    const int mqtt_port = 1883; // O el puerto que uses (ej. 8883 para MQTTS)
    // const char* mqtt_user = "[USUARIO_MQTT]"; // Si tu broker requiere autenticación
    // const char* mqtt_pass = "[PASSWORD_MQTT]"; // Si tu broker requiere autenticación
    const char* mqtt_topic_publish_temp_hum = "iot/esp32/[TU_DEVICE_ID]/data";
    const char* mqtt_topic_subscribe_relay = "iot/esp32/[TU_DEVICE_ID]/control/relay";
    ```
* Asegúrate de tener instalados los drivers USB-to-Serial de tu ESP32 (CP210x o CH340).
* Selecciona la placa correcta en PlatformIO (`esp32dev` o `TTGO T-Display`).
* Compila y sube el código a tu ESP32.

### 4. **Aplicación Backend Java (Eclipse IDE)**

* Abre tu proyecto `ECLIPSE_SQLSERVER` en Eclipse IDE.
* Verifica las dependencias de Maven en tu archivo `pom.xml`. Asegúrate de que las librerías para MQTT (ej. `Eclipse Paho MQTT Client`) y para SQL Server (ej. `Microsoft SQL Server JDBC Driver`) estén incluidas.
* **Configura la conexión a la base de datos:**
    * Localiza el archivo donde se maneja la conexión a SQL Server (ej. `src/main/java/com/tuproyecto/db/DBConnection.java` o un archivo de propiedades).
    * Actualiza los detalles de conexión:
        ```java
        // Ejemplo de JDBC URL para SQL Server
        String dbUrl = "jdbc:sqlserver://[TU_DIRECCION_SQL_SERVER]:1433;databaseName=iot_data_db;encrypt=false;trustServerCertificate=true;";
        String dbUser = "[TU_USUARIO_SQL]";
        String dbPass = "[TU_PASSWORD_SQL]";
        ```
* **Configura el cliente MQTT en Java:**
    * Localiza el código que inicializa el cliente MQTT (ej. `src/main/java/com/tuproyecto/mqtt/MqttClientApp.java`).
    * Actualiza la dirección del broker y los tópicos de suscripción:
        ```java
        String broker = "tcp://[DIRECCION_IP_O_HOST_DE_TU_BROKER_MQTT]:1883";
        String clientId = "JavaBackendClient";
        String subscribeTopic = "iot/esp32/+/data"; // Para suscribirse a todos los datos de los dispositivos
        String publishRelayTopic = "iot/esp32/[TU_DEVICE_ID]/control/relay";
        ```
* Compila y ejecuta la aplicación Java. Puedes hacer clic derecho en el archivo principal (con el método `main`) y seleccionar `Run As > Java Application`.

---

## 🚦 Uso del Sistema

1.  Asegúrate de que la ESP32 esté encendida y conectada a la red Wi-Fi.
2.  Verifica que el broker MQTT esté activo y accesible.
3.  Ejecuta la aplicación Java en Eclipse.
4.  La ESP32 comenzará a enviar datos de temperatura y humedad al broker MQTT.
5.  La aplicación Java recibirá estos datos y los almacenará en SQL Server.
6.  Puedes enviar comandos al relé desde la aplicación Java (si la implementaste con esa funcionalidad) o usando una herramienta externa de MQTT (ej. MQTT Explorer) publicando mensajes al tópico de control del relé.

---

## ⚠️ Consideraciones Adicionales

* **Seguridad:** Para entornos de producción, se recomienda implementar SSL/TLS para las conexiones MQTT y a la base de datos, así como autenticación robusta.
* **Manejo de Errores:** Asegúrate de implementar un manejo de errores robusto tanto en el firmware de la ESP32 como en la aplicación Java.
* **Escalabilidad:** Para múltiples dispositivos, considera una estrategia de tópicos MQTT más elaborada y un diseño de base de datos optimizado.

---

## 📧 Contacto

Si tienes preguntas o sugerencias, no dudes en contactarme:
elver rueda
elver.s.rueda.b@gmail.com
www.linkedin.com/in/elverrueda

---

## 📜 Licencia

Este proyecto está bajo la Licencia MIT License, Apache 2.0 License. Ver el archivo `LICENSE` para más detalles.

---
