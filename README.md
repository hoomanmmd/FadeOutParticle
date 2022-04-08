FadeOutParticle
===============  
FadeOutParticle is an animation for disappearing views like TextViews
<h1 align="center">
<img src="/preview.gif?raw=true" alt="FadeOutParticle" />
</h1>

Installation
------------
Add it in your root build.gradle at the end of repositories:
```
allprojects {
    repositories {
    	...
		maven { url 'https://jitpack.io' }
	}
} 
```
Add the dependency
```
implementation 'com.github.hoomanmmd:fadeoutparticle:1.0.0'
```

Usage
-----  
```  
<com.appgozar.fadeoutparticle.FadeOutParticleFrameLayout  
 android:id="@+id/fade_out_particle"
 android:layout_width="wrap_content"
 android:layout_height="wrap_content">
	<TextView 
		  android:layout_width="wrap_content"
		  android:layout_height="wrap_content"
		  android:text="Fade out Particle" 
		  />
 </com.appgozar.fadeoutparticle.FadeOutParticleView>  
```

To start animation
```
fadeOutParticleFrameLayout.startAnimation()
```

Reset to initial state
```
fadeOutParticleFrameLayout.reset()
```