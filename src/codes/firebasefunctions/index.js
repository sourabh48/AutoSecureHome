'use strict'

const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.sendNotification = functions.database.ref('/Notification/{user_id}/{notification_id}').onWrite(event => {

    const user_id = event.params.user_id;
    const notification_id = event.params.notification_id;
    
    console.log('We have a notification to : ', user_id);

    if (!event.data.val()) {
        return console.log('A Notification has been deleted from the database : ', notification_id);
    }

    const notify_type = admin.database().ref(`/Notification/${user_id}/${notification_id}`).once('value');

    return notify_type.then(notificationtyperesult => {

         const notification_type = notificationtyperesult.val().type; 
          console.log('Notification : ', notification_type);

          if(notification_type == "dustbin_alert")
          {
                const deviceToken = admin.database().ref(`/Users/${user_id}/device_token`).once('value');
                return deviceToken.then(result => {

                    const token_id = result.val();
                    const payload = {
                        notification: {

                            title: "ALERT",
                            body: "Dustbin is Full!",
                            icon: "default"
                        },
                    };

                    return admin.messaging().sendToDevice(token_id, payload).then(response => {
                        console.log('This was the notification Feature');
                    });
                });

          } else if (notification_type == "burgler_alert") {
            const deviceToken = admin.database().ref(`/Users/${user_id}/device_token`).once('value');
            return deviceToken.then(result => {
                const token_id = result.val();
                const payload = {
                    notification: {
                        title: "ALERT",
                        body: "You have an Intruder at your Home!",
                        icon: "default"
                    },
                };

                return admin.messaging().sendToDevice(token_id, payload).then(response => {
                    console.log('This was the notification Feature');
                });
            });
          }else if (notification_type == "fire_alert") {
            const deviceToken = admin.database().ref(`/Users/${user_id}/device_token`).once('value');
            return deviceToken.then(result => {
                const token_id = result.val();
                const payload = {
                    notification: {
                        title: "ALERT",
                        body: "Your House is on Fire!",
                        icon: "default"
                    },
                };

                return admin.messaging().sendToDevice(token_id, payload).then(response => {
                    console.log('This was the notification Feature');
                });
            });
          }

 
        });
    });
