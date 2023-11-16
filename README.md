# Mobile Application Development

This project was created for the Course "Mobile Application Development" at the Anhalt University of Applied Sciences. The repository contains the code and the corresponding paper for a mobile contact tracing application. This app can be used to scan QR codes when visiting specific locations. Subsequently, the app saves the name of the user, the current location and the time span of the visit to compare the data with other users in a visualized list. An area of application for this project during a pandemic could be to track how many infected people the user encountered at establishments like restaurants or cinemas.

## How to use

The repository contains an Android Studio project that can be imported. The path contact-tracing-app/server/src/ contains the corresponding database server. First, the database server has to be executed and run in the background. There may be some adjustments to IP addresses and ports necessary in the serverApp.java file for the server to work as intended, depending on the setup. When the server is running, the Android app can be executed. In the app, users need to log in with their name. After that, they can switch views between the QR scanner and the visualized list of locations where the user has scanned QR codes. By tapping a location in the list, the app shows a list of all the other users that have visited this location in the same time frame.

## Technologies

* Java
* Android Studio
* PostgreSQL

## Result Preview

In this section, some screenshots of the project results are displayed. Take a look at the corresponding paper named "Mobile-Application-Development.pdf" for detailed information about the project. Currently, the paper is only available in German, though.

### Android App Login - Error Message

<img width="385" alt="Fehler_nachname" src="https://github.com/mgagel/contact-tracing-app/assets/73076495/ce227cc9-6015-40e1-9736-4be8d9ea48dd">

### Android App QR Scanner

<img width="385" alt="Fehler_nachname" src="https://github.com/mgagel/contact-tracing-app/assets/73076495/4f47f15f-d7d1-43e5-b3f6-e80813f7613a">

### Android App Location List

<img width="385" alt="Fehler_nachname" src="https://github.com/mgagel/contact-tracing-app/assets/73076495/3c91334f-b285-4101-8154-421693340289">

### Database Entity Relationship Model

![MAE-ERM](https://github.com/mgagel/contact-tracing-app/assets/73076495/d57741b6-ed29-4d09-a886-dfd3efa26271)

### Database Client Interface

![DB-Client-Interface](https://github.com/mgagel/contact-tracing-app/assets/73076495/442bac4b-aba5-4dea-8bff-be7ede4eb97d)
