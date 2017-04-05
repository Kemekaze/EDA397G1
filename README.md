# EDA397G1 - The Planning Game
The project is to do an Android application that works as a version of the XP Planning game which will be integrated with Github. Java will be used for this purpose. The goal is to make the process of the planning game easier for both customers and developers and also provide metrics over completed games which could be used for further evaluation.

## Dependencies

### App
* Android target sdk: 24
* (current build is not dependent on server)

### Server

* NodeJs v6.10.2 LTS (or later)
* MongoDB v3.4

## Build Instructions

### App
* Clone repo:
```bash
$ git clone https://github.com/Kemekaze/EDA397G1.git
```
* Open EDA379G1 with Android Studio.

* Build project.


### Server

* Install [NodeJs](https://nodejs.org/en/) and [MongoDB](https://docs.mongodb.com/manual/installation/)
* Start up database
```bash
$ mongod
```
* Change directory into server.
```bash
$ cd EDA379G1/server
```
* Install node modules.
```bash
$ npm install
```
* Run server.
```bash
$ node index.js
```
* Or run tests.
```bash
$ npm test
```
