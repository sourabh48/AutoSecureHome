#include "My_DHT_Using_Opp.h"
#include <ESP8266WiFi.h>
#include <ArduinoJson.h>
#include <FirebaseArduino.h>

#define DHT11_PIN D0
dht DHT;

#define FIREBASE_HOST "host id"
#define FIREBASE_AUTH "app secret"

#define WIFI_SSID "ssid"
#define WIFI_PASSWORD "password"

int totalDevices = 3;

int current_state = 1;

float fire_sensor=A0;
float fire_value;
int present_state;
int previous_state;

String chipId = "12345";


StaticJsonBuffer<150> jsonBuffer;
JsonObject& intruderupdateData = jsonBuffer.createObject();
JsonObject& fireupdateData = jsonBuffer.createObject();

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

//pir
  pinMode( D5,INPUT);
  pinMode( D6,OUTPUT);

//fire module
  pinMode(fire_sensor,INPUT);
  
//light controls
  //setupPinsMode();
  pinMode(D1,OUTPUT);
  pinMode(D2,OUTPUT);
  pinMode(D4,OUTPUT);
}

void loop()
{
  getData();
  temperature_humidity();
  intruderalert();
  firealert();
}

void getData() {

  String path = "Devices/" + chipId + "/Switches/Bathroom";
  FirebaseObject object = Firebase.get(path);
  
  bool bexfan = object.getBool("BathroomExfanswitch");
  bool bheater = object.getBool("BathroomHeaterswitch");
  bool brlight = object.getBool("Bathroomlightswitch");

  Serial.println("BathroomExfanswitch:");
  Serial.println(bexfan);
  Serial.println();
 
  Serial.println("BathroomHeaterswitch:");
  Serial.println(bheater);
  Serial.println();
 
  Serial.println("Bathroomlightswitch:");
  Serial.println(brlight);
  Serial.println();

  digitalWrite(D1, bexfan);
  digitalWrite(D2, bheater);
  digitalWrite(D4, brlight);
}

void firealert()
{
  String user = "Devices/"+ chipId;
  FirebaseObject object = Firebase.get(user);
  String uid = object.getString("user_id");

  fire_value=analogRead(fire_sensor);
  Serial.println(fire_value);

   if(fire_value>150)
  {   
    present_state = 1; 
    Serial.println("FIRE!!!!");
    if(present_state!=previous_state)
    {
      previous_state=present_state;
     String notification = "Notification/" + uid;
     fireupdateData["from"] = chipId;
     fireupdateData["type"] = "fire_alert";
     Firebase.push(notification, fireupdateData); 
        
     Serial.println("Notification Sent");
    }
  }
  else
  {
    Serial.println("NO FIRE");
    present_state = 0;
    previous_state =0;
   }
}

void intruderalert()
{
   String user = "Devices/"+ chipId;
  FirebaseObject object = Firebase.get(user);
  String uid = object.getString("user_id");
  
  String security_key_path = "Users/" + uid ;
  FirebaseObject security_object = Firebase.get(security_key_path);
  bool security_state = security_object.getBool("security");
  
  if(security_state == 1)
  {
    Serial.println("Switched on");
    int sen = digitalRead(D5);
   
    if(sen ==1)
    {
      digitalWrite(D6, HIGH);
      Serial.println("Alert Intruder");
      if(sen != current_state)
      {
        current_state = sen;
        String notification = "Notification/" + uid;
        intruderupdateData["from"] = chipId;
        intruderupdateData["type"] = "burgler_alert";
        Firebase.push(notification, intruderupdateData); 
        
        Serial.println("Notification Sent");
      }
    }
    else
    {
      digitalWrite(D6, LOW);
      current_state = sen;
      Serial.println("Everything's fine dude!!!!");
    }  
  }
  else
  {
    Serial.println("Security off");
  }
}

void temperature_humidity()
{
  DHT.read11(DHT11_PIN);
  String path = "Devices/" + chipId + "/TemperatureSensor";

  float h = DHT.humidity;
  float t = DHT.temperature;

  Serial.print("Temperature = ");
  Serial.println(DHT.temperature);
  
  Serial.print("Humidity = ");
  Serial.println(DHT.humidity);

  Firebase.setFloat(path + "/temperature",t);
  Firebase.setFloat(path + "/humidity",h);
  
}
