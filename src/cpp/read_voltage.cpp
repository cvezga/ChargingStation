#define SCT_PIN 34

void setup() {
  Serial.begin(115200);

  analogReadResolution(12);  // 0-4095
  analogSetPinAttenuation(SCT_PIN, ADC_11db);
}

void loop() {
  const int samples = 2000;
  double sum = 0;

  // Find real bias automatically
  for (int i = 0; i < samples; i++) {
    sum += analogRead(SCT_PIN);
    delayMicroseconds(200);
  }

  double offset = sum / samples;

  double sumSq = 0;

  for (int i = 0; i < samples; i++) {
    double adc = analogRead(SCT_PIN);
    double centered = adc - offset;
    double voltage = centered * (3.3 / 4095.0);
    sumSq += voltage * voltage;
    delayMicroseconds(200);
  }

  double vrms = sqrt(sumSq / samples);
  double current = vrms * 100.0;   // SCT013 100A/50mA + 20 ohm burden

  //Serial.print("Offset ADC: ");
  //Serial.print(offset);

  //Serial.print("  Vrms: ");
  Serial.println(vrms, 4);

  //Serial.print(" V  Current: ");
  //Serial.print(current, 3);
  //Serial.println(" A");

  delay(1000);
}