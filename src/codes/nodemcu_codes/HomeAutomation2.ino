#include <ESP8266WiFi.h>
#include <ArduinoJson.h>
#include <FirebaseArduino.h>

#define FIREBASE_HOST "hostID"
#define FIREBASE_AUTH "appsecret"

#define WIFI_SSID "your SSID"
#define WIFI_PASSWORD "your password"

int totalDevices = 8;

String chipId = "12345";

void setupFirebase() 
{
  Firebase.begin(FIREBASE_HOST,FIREBASE_AUTH);
}

void setupWifi() 
{
  WiFi.begin(WIFI_SSID , WIFI_PASSWORD);
  Serial.println("Hey i 'm connecting...");
  while (WiFi.status() != WL_CONNECTED)
  {
    Serial.print(".");
    delay(500);
  }
  Serial.println();
  Serial.println("I 'm connected and my IP address: ");
  Serial.println(WiFi.localIP());
}


void setup(){
  Serial.begin(9600);
  setupWifi();
  setupFirebase();

  pinMode(D0,OUTPUT);
  pinMode(D1,OUTPUT);
  pinMode(D2,OUTPUT);
  pinMode(D3,OUTPUT);
  pinMode(D4,OUTPUT);
  pinMode(D5,OUTPUT);
  pinMode(D6,OUTPUT);
  pinMode(D7,OUTPUT);
}

void loop()
{
  getData();
}

void getData() {

//bedroom
  String path = "Devices/" + chipId + "/Switches/Bedroom";
  FirebaseObject object = Firebase.get(path);
  
  bool bdac = object.getBool("Bedroomacswitch");
  bool bdfan = object.getBool("Bedroomfanswitch");
  bool bdrlight = object.getBool("Bedroomlightswitch");

  Serial.println("Bedroomacswitch:");
  Serial.println(bdac);
  Serial.println();
 
  Serial.println("Bedroomfanswitch:");
  Serial.println(bdfan);
  Serial.println();
 
  Serial.println("Bedroomlightswitch:");
  Serial.println(bdrlight);
  Serial.println();

  digitalWrite(D7, bdac);
  digitalWrite(D1, bdfan);
  digitalWrite(D3, bdrlight);

//hall
  String path2 = "Devices/" + chipId + "/Switches/hall";
  FirebaseObject object2 = Firebase.get(path2);

  bool hac = object2.getBool("hallacswitch");
  bool hfan = object2.getBool("hallfanswitch");
  bool hlight = object2.getBool("halllightswitch");

  Serial.println("hallacswitch:");
  Serial.println(hac);
  Serial.println();
 
  Serial.println("hallfanswitch:");
  Serial.println(hfan);
  Serial.println();
 
  Serial.println("halllightswitch:");
  Serial.println(hlight);
  Serial.println();

  digitalWrite(D2, hac);
  digitalWrite(D4, hfan);
  digitalWrite(D5, hlight);

  String path3 = "Devices/" + chipId + "/Switches/kitchen";
  FirebaseObject object3 = Firebase.get(path3);

  bool klights = object3.getBool("kitchenlightswitch");
  bool kfan = object3.getBool("kitchenfanswitch");

  Serial.println("kitchenlightswitch:");
  Serial.println(klights);
  Serial.println();
 
  Serial.println("kitchenfanswitch:");
  Serial.println(kfan);
  Serial.println();

  digitalWrite(D6, klights);
  digitalWrite(D0, kfan);
 }

