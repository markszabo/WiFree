# WiFree

An Android app to test for the default password vulnerability in Cisco routers provided by UPC. It is based on the [research of Search Lab](http://www.search-lab.hu/advisories/secadv-20150720).

This is for educational and research purpose only! Connecting to someone's network without prior authorization is illegal (in most countries).

The app is in a very early stage of development, so feel free to contribute.

# Features that (should) work:

* Listing wifi networks (click on 'Scan Wifi Networks')
* Add networks to crack list (click on the list of wifi networks)
* Run the offline password calculation algorithm in the background (click on 'Start crack service in the background'). Be aware, that his will prevent your phone from sleeping, so it will drain your battery faster than usually. Also sometimes android kills the background process, so if the progress bar is not updating in the notification, simply press 'Start crack service in the background' again. The state of cracking is saved periodically, so it will continue where it stopped. 
* Display wifi network with calculated passwords (click on 'Go to crack list'). Usually ~60 passwords/network are found.
* Try connecting with the possible passwords one by one (click on a network in the 'Crack list'). Please make sure you are in wifi range. This feature is under testing right now, so might not work properly.
* If matching password found, store and display it in the 'Crack List'.

* All the data is stored in the database of the app.

# Todo:

* Testing of the wifi connection feature
* Remove Toast messages used for testing
* Better, more intuitive UI
* Proper user-error handling (right now you can add non-UPC networks etc.)

# Useful links during development:

* Using an ArrayAdapter with ListView https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
* Database for storage https://developer.android.com/training/basics/data-storage/databases.html 