# android-kotlin-arch-boilerplate
Boilerplate Android app written in Kotlin using Android Architecture Components and Firebase/Retrofit2/Dagger2/RxJava2

The app provides a simple UI to login a user and list/create simple data items in Firebase using only Retrofit (not Firebase's Android library).
It provides the necessary base Activities/Fragments to provide a structure to the app and some Kotlin extensions that make things easier and fun. 
The app uses Android Architecture Components ViewModels as the interaction point between the UI and network (Retrofit)/local cache (Room). The UI (Activities/Fragments) is updated using change listeners in the Room model.

To run the app, first create a firebase android app, second create a user and enable sign-in method as email/password and finally create a test user from firebase console. Make sure you put your firebase's google-services.json inside the app folder. Also, update BASE_SERVICE_URL parameter with your firebase url inside app folder's build.gradle to make the app work.

Note that the project currently uses Android Architecture Components alpha libraries and implemented using Android Studio 3.0 Canary 6.

The app uses a simple user-item data structure. You can use the below Firebase database rule to only allow users reading and writing their own item objects:

```
{
  "rules": {
    "users": {
      "$uid": {
        ".read": "auth.uid == $uid",
        ".write": "auth.uid == $uid",
      
        "items" : {
          "$itemId" : {
            ".read": "auth.uid == $uid",
            ".write": "auth.uid == $uid"
          }
        }
      }
    }
  }
}
```

Used libraries:
- Firebase Auth (for signing in users)
- Retrofit (for using Firebase as the backend)
- Dagger2
- RxJava2/RxAndroid (mostly for network management)
- Android Architecture Components: ViewModel and Room
