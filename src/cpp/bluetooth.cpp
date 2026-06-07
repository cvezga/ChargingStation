#include "BluetoothSerial.h"
#include <Preferences.h>

BluetoothSerial SerialBT;
Preferences prefs;

String ssid = "";
String pass = "";

void setup() {
  Serial.begin(115200);

  prefs.begin("wifi", false);

  ssid = prefs.getString("ssid", "");
  pass = prefs.getString("pass", "");

  Serial.println("Configuración cargada:");
  Serial.println("SSID: " + ssid);
  Serial.println("PASS: " + pass);

  SerialBT.begin("PetTagESP32");
  Serial.println("Bluetooth listo");
}

void loop() {
  if (SerialBT.available()) {
    String msg = SerialBT.readStringUntil('\n');
    msg.trim();

    if (msg.startsWith("SSID=")) {
      ssid = msg.substring(5);
      prefs.putString("ssid", ssid);
      SerialBT.println("SSID guardado");
    }

    if (msg.startsWith("PASS=")) {
      pass = msg.substring(5);
      prefs.putString("pass", pass);
      SerialBT.println("PASS guardado");
    }

    if (msg == "SHOW") {
      SerialBT.println("SSID: " + ssid);
      SerialBT.println("PASS: " + pass);
    }
  }
}