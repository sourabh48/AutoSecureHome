# AutoSecureHome

## About This Project ( Must Read )

This is a IoT cum Android project based on Home Automation and Security. This project is done to demonstrate how to implement 
home-automation and security in your home easily.

### Equipments Required:

- ModeMCU(esp8266)
- PIR Sensors
- Flame Detectors
- Bread Board
- Connecting Wires
- Relays

### Software Required

- Android Studio
- Arduino IDE
- NodeJS

### Database Used

- Firebase

## How It Works?

- Alert Systems:

There are two kinds of alerts:

1. Intruder Alerts
2. Fire Alerts

We can add more alerts by setting notification type in the notification section of database.

### Intruder Alerts.

The PIR sensors detects motion and writes notification data to database.

#### What we can add?

We can add is a small camera to click the image of the intruder whenever a PIR sensor is triggered and save it to the Firebase Storage which can be accessed by the user through the application.

Other things that can be added is image processing.

### Flame Alerts.

The Flame sensors detects nearby flame and writes notification data to database.

###  Triggers.

We used Firebase Function to trigger Firebase Cloud Function to send Notification to the device. To send the notification we collected a device token. This device token is the unique id for a device. Whenever any new value is pushed in the "Notification" table the firebase function will create a payload according to notification type and send it to device.

### Light Controls:

To control lights we created an android application to switch on and off the electrical appliances.

### Android Application

The UI is very simple and basic and can be used by anyone.

## How to Setup?

Please refer to my other Repository "Sending Notification Using Firebase Function And Firebase Cloud Messaging".

### Note:

##### This index.js is written using beta version of firebase tools. Please refer to firebase documentation or my other Repository that I have built specially to show the updated coding.  



