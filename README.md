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

