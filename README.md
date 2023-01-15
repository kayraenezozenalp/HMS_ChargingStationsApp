# 1901649

https://user-images.githubusercontent.com/89274981/212564026-438883be-43e0-4b05-a635-c13afc548a21.mp4


This project involved the development of an application utilizing the Model-View-ViewModel (MVVM) architecture, written in the Kotlin programming language
, and integrated with Huawei services. The purpose of the application is to display charging stations on a map 
and allow for the user to view detailed information about a selected station by clicking on its marker.

Implement the Huawei services:
To integrate Huawei servers into my project, the first step was to open a developer account with Huawei and obtain approval for the project
. I then created a new project within the Huawei developer portal, AppGalleryConnect, and added the SHA-256 Fingerprint of the corresponding project
, which was built using the "Generate Signed Package or APK" feature in Android Studio, to the project within AppGalleryConnect. To establish a connection between the two projects
, the agconnect-services.json file was added to the root directory of the application in my Android Studio project.
 The final step in the process was to include the necessary additions to the gradle build files and to incorporate the necessary application code as outlined in the Huawei documentation.

	
LoginFragment:In  the loginFragment class handles the login functionality of an Android app using the Huawei Identity Service.Once the user has successfully signed in, their username is stored and the app navigates to the next fragment using the Navigation component.If the user cannot log in successfully, the TextView with "Network Error" appears on the screen.
Updates the UI using ViewModel and LiveData, and navigates to the next screen after successful login. Additionally, it uses data binding and constants for request codes.

LoginViewModel:The LoginViewModel class has two properties: a private MutableLiveData property "_isSignIn" for tracking user sign-in state 
and a val property "_username" for holding the username of the signed-in user.The _isSignIn property checks whether the user is logged in or not, 
and directs them to the SearchFragment if they are logged in. The "userSignedIn()" function updates the _isSignIn property with a passed-in Boolean value.


Fragment_login.xml:In the fragment_login.xml layout, 
the Huawei ID Authentication button is utilized with the identifier "loginbutton" and a TextView element is included with the identifier "networkerror."


SearchFragment:SearchFragment uses the Huawei Mobile Services (HMS) location kit.we first ask the user for permission to get his location. 
Then we send the user to the map screen according to the values ​​entered by the user.
the app includes a logout feature that can be accessed through the menu bar located in the top right corner of the screen.
 When the user selects the logout option, 
they are returned to the login screen and their current session is terminated.


SearchViewModel:The Search ViewModel class has one propertie: a private MutableLiveData property "_isSignOut" for tracking user sign-out state.
The isSignOut property checks whether the user is logged out or not, 
and directs them to the LoginFragment if they are logged out.The "userSignedOut()" function updates the _isSignOut property with a passed-in Boolean value

Fragment_Search.xml:In the fragment_search.xml layout,In order to facilitate the selection of specific locations on the map for which the user desires to retrieve data
, a spinner element with the id "countryCodeSpinner" is provided to allow the user to select the desired country. Additionally,
 an EditText element with the id "editTextTownName" is supplied for the user to input the maximum distance from their current location to the desired stations.
 Upon clicking the imageView element with the id "locationSearch", the user's current location is obtained and subsequently displayed within the TextViews.
 The action of clicking the TextView element with the id "Clear" results in the deletion of any previously input values ​​from the user interface.
 Finally, upon clicking the button element with the id "searchButton", the entered data is transmitted to the Map screen for further processing.

MapFragment:We send a request to the "getFromData API" function in MapFragment to pull data from the API with the values ​​entered by the user from the Search Fragment, and since the data is an async situation, we call this request inside GlobalScope.launch and call it in suspend blocks. Then we call observeLiveData() in order to throw the incoming data into a list and to display the progressbar to the user interface while the map is being loaded. We get the data from the API from the "stationList" that we defined in the MapViewModel and assign them to the "markerList" in order to be able to mark onMapReady(). If the map is on the loading screen, we make the progressbar visible. If an error occurs while loading the map, the textview with the "maperror" id appears on the screen. In onMapReady(), we add the marker information that we have put into the markerlist to the map in a different marker color according to their LevelIDs. Then, the user's location marker is added to the map. If the markers are clicked, the InfoWindow of the marker opens. If they click on the InfoWindow, they send it to the DetailFragment to see the details of the stations.We're sending a custom parseable nullable value called "detail" to the DetailFragment. In addition to the values ​​we will show in the location details in detail, the values ​​we will show for the equipment detail create a list called EquipmentList in order to get the values ​​in the equipment, put them, take the values ​​in this list and send this list to the DetailFragment after adding it to the detail.

fragment_map.xml:In the fragment_map.xml layout,create huaweiMapView with id is "huaweiMapView",create progressbar with id is "mapLoading" and create maperror textview with id is "maperror"

MapViewModel:It contains a function getFromDataAPI which takes in several parameters such as key, distanceunit, 
countrycode, latitude, longitude, and distance. This function makes an API call to the MapAPIService class, 
passing these parameters and subscribe to the response. On success, it updates the stationList MutableLiveData and on error, it calls the error function.

It also uses the RxJava library to handle the API calls and updates to the app's UI in a reactive manner, which allows the app to respond to changes in the data and user interactions in a efficient and consistent way.
MapAPI:The MapAPI uses Retrofit library to make HTTP requests to the API and handle the responses.Sends a request including key, distanceunit, countrycode, latitude, longitude, distance queries and retrieves data from the service according to these data.
.The MapAPI also uses RxJava library to handle the API calls and updates to the MapAPI's UI in a reactive manner
, which allows the app to respond to changes in the data and user interactions in a efficient and consistent way.

MapAPIService:This code is a service class that creates an instance of Retrofit client and uses it to call the charging station service API defined in the MapAPI interface. 
It has a function getData which takes in several parameters and makes a call to the getChargeStations function of the MapAPI interface by passing these parameters 
and returns the response as a Single<ChargingResponse> object. The class also contains a constant BASE_URL which is the base URL of the API endpoint. 
The Retrofit client is built with this URL and is configured to use Gson for JSON data conversion and RxJava for handling API calls and responses in a reactive manner.

nav_graph:This nav_graph.xml was created to manage the navigation and switching between fragments. It also defines and sends the necessary parameters as arguments between fragments.

DetailFragment:The Detail Fragment first pushes the data received from the MapFragment to the textview fields in the fragment detail.xml.
 Then it passes the data received from the MapFragment to the DetailAdapter and displays the data to be shown in the RecyclerView according to the number of data received from the MapFragment. Finally, it converts the nearest address found among the received data into speech by using one of the features of the Huawei ML kit, TextToSpeech.

fragment_detail.xml:In the fragment_detail.xml file, it shows the data in the map fragment. This screen is divided into Location Details and Equipment Details. The location details are displayed using the text views in fragment_detail.xml and when the image button with the id "playButton" is pressed, 
the data at the nearest address is voiced with the "TextToSpeech" in the Huawe Ml kit . The equipment detail section prints the equipment details of the stations we created with the Equipment_item.xml linearlayouts into the recyclerview in the fragment_detail.xml section according to the number that will come from the mapfragment.

DetailAdapter:This is an adapter class for a RecyclerView. It is used to display a list of equipment items in the app's UI by inflating the layout "equipment_item" and rendering it in the RecyclerView. The adapter takes in a list of equipment items as an argument and uses it to populate the RecyclerView by overriding the methods onCreateViewHolder, onBindViewHolder, and getItemCount. The adapter also has a inner class DetailViewHolder which holds references to the TextViews in the equipment_item layout and assigns them to the corresponding variables in its constructor.








