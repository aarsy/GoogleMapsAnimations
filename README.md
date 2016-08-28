[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-GoogleMapsRippleEffect-green.svg?style=true)](https://android-arsenal.com/details/1/4126)

# GoogleMapsRippleEffect        

"GoogleMapsRippleEffect" is an awesome first of its type android library for showing a ripple on a google map, e.g show catchment area of an earthquake where ripples have been felt, give prominence to certain markers which need to be highlighted. Also add a ripple when your user is moving on the map and give a #PokemonGo type ripple effect. The example details of the same will be added soon. 

Below samples show the ripple effect in action:

![](https://github.com/arsy1995/GoogleMapsRippleEffect/blob/master/gifs/Sample2.gif)                ![](https://github.com/arsy1995/GoogleMapsRippleEffect/blob/master/gifs/Sample1.gif)

------    

#Download    
###Using Gradle: under dependencies section:   
  
    compile 'com.github.aarsy.googlemapsrippleeffect:googlemapsrippleeffect:1.0.2'

### or Using Maven:
    <dependency>
        <groupId>com.github.aarsy.googlemapsrippleeffect</groupId>
        <artifactId>googlemapsrippleeffect</artifactId>
        <version>1.0.2</version>
        <type>pom</type>
    </dependency>

------

#Documentation

###Default Ripple animation
Just two lines of code :  
Use **.startRippleMapAnimation()** and **.stopRippleMapAnimation()** methods to start and stop Animation.     
Example is given below (Preview shown above in first sample)
  
        // mMap is GoogleMap object, latLng is the location on map from which ripple should start
              
                MapRipple mapRipple = new MapRipple(mMap, latLng, context);
                mapRipple.startRippleMapAnimation();      //in onMapReadyCallBack
        
                @Override
                protected void onStop() {
                    super.onStop();
                    if (mapRipple.isAnimationRunning()) {
                        mapRipple.stopRippleMapAnimation();
                    }
                }
        // Start Animation again only if it is not running
                if (!mapRipple.isAnimationRunning()) {
                    mapRipple.startRippleMapAnimation();
                }
     

###Advanced Ripple animation

Example is given below (Preview shown above in second sample)
  
        // mMap is GoogleMap object, latLng is the location on map from which ripple should start
              
                mapRipple = new MapRipple(mMap, latLng, context);
                mapRipple.withNumberOfRipples(3);
                mapRipple.withFillColor(Color.BLUE);
                mapRipple.withStrokeColor(Color.BLACK);
                mapRipple.withStrokewidth(10);      // 10dp
                mapRipple.withDistance(2000);      // 2000 metres radius
                mapRipple.withRippleDuration(12000);    //12000ms
                mapRipple.withTransparency(0.5f);
                mapRipple.startRippleMapAnimation();
        // Use same procedure to stop Animation and start it again as mentioned anove in Default Ripple Animation Sample

###Update center of ripple as location changes(Needed when user moves)
Just one line of code is needed:  
Use **.mapRipple.withLatLng(LatLng changedLatlng)** method anytime in future to update center of ripple.     
  
  	// after implementing **LocationListener** interface to current class use:
        	@Override
        	public void onLocationChanged(Location location) {
           		mapRipple.withLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
        	}
	//See the sample for more help.
       
------

###Compatibility

**Minimum Android SDK**: This library requires a minimum API level of **11**.    

------

###License
Copyright 2016 Abhay Raj Singh Yadav(arsy).

   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions andlimitations under the License.


