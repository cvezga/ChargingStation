#define SCT_PIN 34

void setup() {
  Serial.begin(115200);
  analogReadResolution(12);
  analogSetPinAttenuation(SCT_PIN, ADC_11db);
}

void loop() {
  const int samples = 3000;

  double sum = 0;
  int minAdc = 4095;
  int maxAdc = 0;

  for (int i = 0; i < samples; i++) {
    int adc = analogRead(SCT_PIN);
    sum += adc;

    if (adc < minAdc) minAdc = adc;
    if (adc > maxAdc) maxAdc = adc;

    delayMicroseconds(200);
  }

  double offset = sum / samples;

  double sumSq = 0;

  for (int i = 0; i < samples; i++) {
    int adc = analogRead(SCT_PIN);
    double centered = adc - offset;
    double voltage = centered * (3.3 / 4095.0);
    sumSq += voltage * voltage;

    delayMicroseconds(200);
  }

  double vrms = sqrt(sumSq / samples);
  double current = vrms * 100.0;  // 20 ohm burden

  if (current < 0.50) current = 0; // noise filter

  Serial.print("Offset ADC: ");
  Serial.print(offset);

  Serial.print("  Min: ");
  Serial.print(minAdc);

  Serial.print("  Max: ");
  Serial.print(maxAdc);

  Serial.print("  Vrms: ");
  Serial.print(vrms, 4);

  Serial.print(" V  Current: ");
  Serial.print(current, 3);
  Serial.println(" A");

  delay(1000);
}