[![Build Status](https://travis-ci.com/Kemekaze/EDA397G1.svg?token=xwshWmggh1TkzihJDoDT&branch=master)](https://travis-ci.com/Kemekaze/EDA397G1)
# EDA397G1 - The Planning Game
The project is to do an Android application that works as a version of the XP Planning availSession which will be integrated with Github. Java will be used for this purpose. The goal is to make the process of the planning availSession easier for both customers and developers and also provide metrics over completed availSessions which could be used for further evaluation.

## Dependencies

### App
* Android target sdk: 24
* Server: xr-plan.se

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
* If you want to use a local server edit the server ip address at :
EDA397G1\app\src\main\java\chalmers\eda397g1\Resources\Constants.java
```java
Constants.SERVER_IP = "YOUR_IP_ADDRESS"
```
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

## Structure
### Android
### Send a request to the server
```java
RequestEvent requestEvent = new RequestEvent(EVENTSTRING, JSON_OBJECT);
EventBus.getDefault().post(requestEvent);
```
Where EVENTSTRING is a Constant located in Constants.SockeSocketEvents
```java
public class Constants {
  public class SocketEvents {
      ...
      public static final String AUTHENTICATE_GITHUB = "authenticate.github";
      ...
  }
}
```
JSON_OBJECT is a JSON object with your query to the server. \n
A complete call can look like
```java
JSONObject obj = ... your JSONObject
RequestEvent requestEvent = new RequestEvent(Constants.SocketEvents.AUTHENTICATE_GITHUB,obj);
EventBus.getDefault().post(requestEvent);
```
### Getting a response from the server
in SocketService
we need to define our reponse for the request
```java
private void setupListeners(){
....
socket.on(Constants.SocketEvents.AUTHENTICATE_GITHUB, eventAuthenticatedGithub);
...
}
```
where we have the same EVENTSTRING and then a callback listener (eventAuthenticatedGithub)
```java
private Emitter.Listener eventAuthenticatedGithub = new Emitter.Listener() {
    @Override
    public void call(Object... args) {
        Log.i(TAG, "eventAuthenticatedGithub");
        for(int i = 0; i<args.length; i++)
            Log.i(TAG,  args[i].toString());
        // send the data back to the activity
        // by creating a new LoginEvent (just for this case, different events for different events)
        // All events should extend the class Event
        EventBus.getDefault().post(new LoginEvent(args));
    }
};
```
In your activity you will have a method with the anontaion @Subscribe for handling the EventBus.getDefault().post() event
```java

@Subscribe
public void loginEvent(LoginEvent event)
{

}
//This will be usually handled in a different thread the you can use
@Subscribe(threadMode = ThreadMode.MainThread)
```
For more about the Eventbus visit:
https://github.com/greenrobot/EventBus
### Server
to be added
