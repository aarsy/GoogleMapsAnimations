[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-GoogleMapsRippleEffect-green.svg?style=true)](https://android-arsenal.com/details/1/4126)

# GoogleMapsRippleEffect
"GoogleMapsRippleEffect" is an awesome android library for developers who need to show multiple types of circular ripple effects on google map, e.g show search area to your clients and users if you are finding matches for their needs. This is the one and only ripple effect android library for google maps on github and anywhere as per google's search. Developers can also use #PokemonGo type ripple effect which will be uploaded soon. See the samples below:


![](https://github.com/arsy1995/GoogleMapsRippleEffect/blob/master/gifs/Sample2.gif)                ![](https://github.com/arsy1995/GoogleMapsRippleEffect/blob/master/gifs/Sample1.gif)


------    

#Download    
###Using Gradle:
    repositories {
    mavenCentral() // jcenter() works as well because it pulls from Maven Central
    }
    dependencies {
    	compile 'com.github.aarsy.googlemapsrippleeffect:googlemapsrippleeffect:1.0.0'    
    }
 **or** simply under dependencies section:   
  
    compile 'com.github.aarsy.googlemapsrippleeffect:googlemapsrippleeffect:1.0.0'

### or Using Maven:
    <dependency>
        <groupId>com.github.aarsy.googlemapsrippleeffect</groupId>
        <artifactId>googlemapsrippleeffect</artifactId>
        <version>1.0.0</version>
        <type>pom</type>
    </dependency>

### or Using Ivy:         
    <dependency org='com.github.aarsy.googlemapsrippleeffect' name='googlemapsrippleeffect' rev='1.0.0'>
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

