#include <WiFi.h>
#include <HTTPClient.h>

#ifdef __cplusplus
extern "C" {
#endif

uint8_t temprature_sens_read();

#ifdef __cplusplus
}
#endif

String ssid = "HONORcv";       // Aquí se guardará el SSID dinámico
String password = "cv123456";   // Aquí se guardará la contraseña dinámica
//https://app.datacake.de/cvezga-iot/devices/d/13ad1eb0-463c-4dfb-9d89-19e79c61ca20/t/configuration
const char* serverName = "https://api.datacake.co/integrations/api/73fc0e72-e743-4e20-aa90-4cfba7187039/";  // Reemplaza con tu endpoint
long lastReconnect = 0;

void setup() {
  Serial.begin(115200);
  reconnectWiFi(); // Intenta la conexión inicial
}

void loop() {

    if (WiFi.status() != WL_CONNECTED) {

        if (millis() - lastReconnect > 30000) {

            reconnectWiFi();
            lastReconnect = millis();
        }

        return;
    }

    uint8_t temp = temprature_sens_read();
    Serial.print("Temperatura CPU: ");
    Serial.print(temp);
    Serial.println(" °C");

    HTTPClient http;
    http.begin(serverName);

    http.addHeader("Content-Type", "application/json");

    /*
     {
            "device":"dc419ad1-d947-43b4-8484-e403afe22e92",
            "temperature":23.34
     }
     */

    String jsonData = "{ \"device\":\"dc419ad1-d947-43b4-8484-e403afe22e92\", \"temperature\": ";
    jsonData += String(temp);
    jsonData += " }";

    int httpResponseCode = http.POST(jsonData);

    if (httpResponseCode > 0) {
      String response = http.getString();
      Serial.print("Respuesta del servidor: ");
      Serial.println(response);
    } else {
      Serial.print("Error en la petición: ");
      Serial.println(httpResponseCode);
    }

    http.end();


  delay(5000); // Espera 5 minutos (300,000 ms) antes del siguiente envío
}

// Función para reconectar al WiFi
void reconnectWiFi() {
  Serial.print("Conectando a WiFi...");

  WiFi.disconnect(true);
  delay(1000);

  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid.c_str(), password.c_str());

  // Espera hasta conectarse
  int intentos = 0;
  while (WiFi.status() != WL_CONNECTED && intentos < 20) {
    delay(500);
    Serial.print(".");
    intentos++;
  }

  if (WiFi.status() == WL_CONNECTED) {
    Serial.println("\nConectado a WiFi");
  } else {
    Serial.println("\nFallo al conectar al WiFi. Reintentando...");
    // Aquí puedes optar por un reinicio o esperar más tiempo antes de intentar de nuevo
  }
}

