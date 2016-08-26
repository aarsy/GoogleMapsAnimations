[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-GoogleMapsRippleEffect-green.svg?style=true)](https://android-arsenal.com/details/1/4126)

# GoogleMapsRippleEffect        

"GoogleMapsRippleEffect" is an awesome first of its type android library for showing a ripple on a google map, e.g show catchment area of an earthquake where ripples have been felt, give prominence to certain markers which need to be highlighted. Also add a ripple when your user is moving on the map and give a #PokemonGo type ripple effect. The example details of the same will be added soon. 

Below samples show the ripple effect in action:


![](https://github.com/arsy1995/GoogleMapsRippleEffect/blob/master/gifs/Sample2.gif)                ![](https://github.com/arsy1995/GoogleMapsRippleEffect/blob/master/gifs/Sample1.gif)


------    

#Download    
###Using Gradle: under dependencies section:   
  
    compile 'com.github.aarsy.googlemapsrippleeffect:googlemapsrippleeffect:1.0.1'

### or Using Maven:
    <dependency>
        <groupId>com.github.aarsy.googlemapsrippleeffect</groupId>
        <artifactId>googlemapsrippleeffect</artifactId>
        <version>1.0.1</version>
        <type>pom</type>
    </dependency>

### or Using Ivy:         
    <dependency org='com.github.aarsy.googlemapsrippleeffect' name='googlemapsrippleeffect' rev='1.0.1'>
      	<artifact name='$AID' ext='pom'></artifact>
    </dependency>

------

#Documentation

###Default Ripple animation
Just two lines of code :  
Use **.startRippleMapAnimation() and .stopRippleMapAnimation() methods to start and stop Animation.**     
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
				
------

###Compatibility

**Minimum Android SDK**: This library requires a minimum API level of **11**.    

