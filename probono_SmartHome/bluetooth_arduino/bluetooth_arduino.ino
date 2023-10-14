#include <SoftwareSerial.h>

const int thresholdvalue = 300; // 임의로 설정한 임계값(수정예정)
SoftwareSerial mySerial(2, 3); // RX, TX

void setup() {
  pinMode(A0, INPUT); // pinMode 설정
  Serial.begin(9600); // 시리얼 통신 시작, 전송속도 9600bps
  Serial.println("Start..");
  mySerial.begin(9600); // 블루투스 통신 시작

  Serial.println("Bluetooth connected!"); 
}

void loop() {
  int value = analogRead(A0);

  mySerial.print("Sensor value = ");
  mySerial.println(value);

  if (value > thresholdvalue) {
    mySerial.println("Detected!");
    Serial.println("Detected");
    
    // Bluetooth로 데이터 전송
    mySerial.print("Bluetooth data: ");
    mySerial.println("Detected");
  } else {
    mySerial.println("Detecting...");
    Serial.println("Detecting");
    
    // Bluetooth로 데이터 전송
    mySerial.print("Bluetooth data: ");
    mySerial.println("Detecting...");
  }
  
  delay(1000);
}
