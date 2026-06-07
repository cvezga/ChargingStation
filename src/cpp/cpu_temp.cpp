#ifdef __cplusplus
extern "C" {
#endif

uint8_t temprature_sens_read();

#ifdef __cplusplus
}
#endif

void setup() {
  Serial.begin(115200);
}

void loop() {
  float tempC = (temprature_sens_read() - 32) / 1.8;

  //Serial.print("CPU Temperature: ");
  Serial.println(tempC);
  //Serial.println(" °C");

  delay(2000);
}
