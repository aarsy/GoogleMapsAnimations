[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-GoogleMapsAnimations-green.svg?style=true)](https://android-arsenal.com/details/1/5070)

# GoogleMapsAnimations        

"GoogleMapsAnimations" is an awesome first of its type android library for showing a ripple and radar animations on a google map, e.g show catchment area of an earthquake where ripples have been felt, give prominence to certain markers which need to be highlighted. Also add a ripple when your user is moving on the map and give a #PokemonGo type ripple effect and also add a radar type effect to show users that you are searching in certain area

Below samples show the ripple effect in action:

<img src="/gifs/Sample2.gif" > <img src="/gifs/Sample1.gif" > <img src="/gifs/Sample3.gif"> <img src="/gifs/Sample4.gif">

------    

#Download (These are not working for now. Please download the package manually until this issue is resolved)
###Using Gradle: under dependencies section:   
  
    compile 'com.github.aarsy.googlemapsanimations:googlemapsanimations:1.0.3'

### or Using Maven:
    <dependency>
        <groupId>com.github.aarsy.googlemapsanimations</groupId>
        <artifactId>googlemapsanimations</artifactId>
        <version>1.0.3</version>
        <type>pom</type>
    </dependency>

------

#Documentation

##Ripple Animation

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
	
	
##Radar Animation

###Simple Clockwise Radar animation
Just two lines of code :  
Use **.startRadarAnimation()** and **.stopRadarAnimation()** methods to start and stop Animation.     
Example is given below (Preview shown above in third sample)
  
        // mMap is GoogleMap object, latLng is the location on map from which ripple should start
              
                MapRadar mapRadar = new MapRadar(mMap, latLng, context);
		mapRadar.withDistance(2000);
		mapRadar.withOuterCircleStrokeColor(Color.parseColor("#fccd29"));
		mapRadar.withRadarColors(Color.parseColor("#00fccd29"), Color.parseColor("#fffccd29"));
			//withRadarColors() have two parameters, startColor and tailColor respectively
			//startColor should start with transparency, here 00 in front of fccd29 indicates fully transparent
			//tailColor should end with opaqueness, here f in front of fccd29 indicates fully opaque
		mapRadar.startRadarAnimation();      //in onMapReadyCallBack
        
                @Override
                protected void onStop() {
                    super.onStop();
                    if (mapRadar.isAnimationRunning()) {
                        mapRadar.stopRadarAnimation();
                    }
                }



###Advanced Clockwise and AntiClockwise Radar animation

Example is given below (Preview shown above in fourth sample)
  
        // mMap is GoogleMap object, latLng is the location on map from which ripple should start
              
                MapRadar mapRadar = new MapRadar(mMap, latLng, context);
		mapRadar.withDistance(2000);
		mapRadar.withOuterCircleStrokeColor(Color.parseColor("#fccd29"));
		mapRadar.withOuterCircleStrokewidth(7);
		mapRadar.withOuterCircleTransparency(0.4f);
		mapRadar.withClockWiseAnticlockwise(true);		//enable both side rotation
		mapRadar.withClockwiseAnticlockwiseDuration(2);
		//withClockwiseAnticlockwiseDuration(duration), here duration denotes how much cycles should animation makes in 
		//one direction
		mapRadar.withOuterCircleFillColor(Color.parseColor("#12000000"));            
		mapRadar.withRadarColors(Color.parseColor("#00fccd29"), Color.parseColor("#fffccd29"));
			//withRadarColors() have two parameters, startColor and tailColor respectively
			//startColor should start with transparency, here 00 in front of fccd29 indicates fully transparent
			//tailColor should end with opaqueness, here f in front of fccd29 indicates fully opaque
		mapRadar.withRadarSpeed(5);	//controls radar speed
		mapRadar.withRadarTransparency(0.4f);
		mapRadar.startRadarAnimation();      //in onMapReadyCallBack
		
                @Override
                protected void onStop() {
                    super.onStop();
                    if (mapRadar.isAnimationRunning()) {
                        mapRadar.stopRadarAnimation();
                    }
                }

###Update center of radar as location changes(Needed when user moves)
Just one line of code is needed:  
Use **.mapRadar.withLatLng(LatLng changedLatlng)** method anytime in future to update center of radar.    
  
  	// after implementing **LocationListener** interface to current class use:
        	@Override
        	public void onLocationChanged(Location location) {
           		mapRadar.withLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
        	}
	//See the sample for more help.
       
------

#Compatibility

**Minimum Android SDK**: This library requires a minimum API level of **11**.    

------

#License
Copyright 2016 Abhay Raj Singh Yadav(arsy).

   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions andlimitations under the License.


