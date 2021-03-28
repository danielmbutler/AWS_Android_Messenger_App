# AWS_Android_Messenger_App

Messenger app with AWS amplify backend and realtime database , supports realtime chat between 2 users (including sending images), notifications and timestamps, profile setup and a users screen to display all signed up users.

Login Page -> Register, Sign in
Main Activity -> 3 Fragments -> Contacts, Messages, settings (Sign Out)
ChatLog Activity to display chat
Message Database -> Amplify Datastore
Authentication - Amazon Cognito
Media/ Storage - S3

## Database Models

ChatMessage
```
public final class ChatMessage implements Model {
  public static final QueryField ID = field("ChatMessage", "id");
  public static final QueryField MESSAGE_TXT = field("ChatMessage", "messageTxt");
  public static final QueryField FROMID = field("ChatMessage", "fromid");
  public static final QueryField TOID = field("ChatMessage", "toid");
  public static final QueryField TIMESTAMP = field("ChatMessage", "timestamp");
  public static final QueryField READ_RECEIPT = field("ChatMessage", "readReceipt");
```
LatestMessage
```
public static final QueryField ID = field("LatestMessage", "id");
public static final QueryField MESSAGE_TXT = field("LatestMessage", "messageTxt");
public static final QueryField FROMID = field("LatestMessage", "fromid");
public static final QueryField TOID = field("LatestMessage", "toid");
public static final QueryField TIMESTAMP = field("LatestMessage", "timestamp");
public static final QueryField READ_RECEIPT = field("LatestMessage", "readReceipt");
```
User
```
public static final QueryField ID = field("User", "id");
public static final QueryField USERNAME = field("User", "Username");
public static final QueryField PROFILE_PHOTO_URL = field("User", "ProfilePhotoUrl");
public static final QueryField IS_PROFILE_COMPLETE = field("User", "isProfileComplete");
public static final QueryField EMAIL = field("User", "email");
```

## Architecure
Activities/Fragments -> ViewModel , listeners and Authentication, -> Repository -> Database
![alt text](https://github.com/danielmbutler/AWS_Android_Messenger_App/blob/master/assets/Architecture%20Diagram.PNG)


### Authentication Classes and DB listener
Amplify Auth Class to provide live data of Authentication details which is observed from the activities and fragments

```

//CLASS TO provide livedata for Amplify Authentitication properties
	

	class AmplifyAuth {
	

	    private  val TAG = "AmplifyAuth"
	

	    val SignupValue         : MutableLiveData<Boolean> = MutableLiveData()
	    val ConfirmationCode    : MutableLiveData<Boolean> = MutableLiveData()
	    val UserCreated         : MutableLiveData<Boolean> = MutableLiveData()
	    val UsernameEdited      : MutableLiveData<Boolean> = MutableLiveData()
	    val ProfileImageEdited  : MutableLiveData<Boolean> = MutableLiveData()
	    val SignInValue         : MutableLiveData<Boolean> = MutableLiveData()
	
```

DB listener classes to provide real time data to the UI for new messages
```
class DatabaseListener {
	

	    val message          : MutableLiveData<ChatMessage> = MutableLiveData()
	    val latestmessage    : MutableLiveData<LatestMessage> = MutableLiveData()
	

	    fun listenformessages(toid: String, Fromid: String){
	

	            Amplify.DataStore.observe(
	                ChatMessage::class.java,
	                { Log.i("MyAmplifyApp", "Observation began") },
	                {
	                    // only listen for incoming messages sent to the logged in user
	                    if (it.item().toid == Fromid && it.item().fromid == toid){
	                        Log.i("MyAmplifyApp", "Message: ${it.item().messageTxt}")
	

	                        GlobalScope.launch(Dispatchers.Main) {
	                            val chatMessage = it.item()
	

	                            Log.d("ChatlogActivity", chatMessage.messageTxt!!)
	                            message.postValue(chatMessage)
	

	                        }
	                    }
	

	                },
	                { Log.e("ChatLog-Listen", "Observation failed", it) },
	                { Log.i("ChatLog-Listen", "Observation complete") }
	            )
	        }

```

## Screenshots and Video

<p align="center">
  <img src="https://github.com/danielmbutler/AWS_Android_Messenger_App/blob/master/assets/Messenger%20View%20Notification.PNG" width="300" >
  <img src="https://github.com/danielmbutler/AWS_Android_Messenger_App/blob/master/assets/Messages%20view.PNG" width="300">
  <img src="https://github.com/danielmbutler/AWS_Android_Messenger_App/blob/master/assets/Profile%20View.PNG" width="300">
</p>
<p align="center">
 <img src="https://github.com/danielmbutler/AWS_Android_Messenger_App/blob/master/assets/Picture%20Messaging.PNG" height="600" width="600">
</p>
<p align="center">
  <img src="https://github.com/danielmbutler/AWS_Android_Messenger_App/blob/master/assets/AWS%20Messenger%20demo%20gif.gif" width="700" height="600" >
</p>

video: https://github.com/danielmbutler/AWS_Android_Messenger_App/blob/master/assets/AWS%20Messenger%20demo.mp4
AWS Amplify Android Docs: https://docs.amplify.aws/lib/q/platform/android