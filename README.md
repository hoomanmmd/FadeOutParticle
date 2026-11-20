FadeOutParticle
===============  
FadeOutParticle is an animation for disappearing composables and views like Text and Icon
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
implementation 'com.github.hoomanmmd:FadeOutParticle:2.0.1'
```

Usage
-----
### Compose
```
val state = rememberFadeOutParticleState()
Text("Fade out Particle", Modifier.fadeOutParticle(state))
Button(
    onClick = { 
        state.start()
    },
    ) {
        Text("Start")
    }
)
```

#### XML
```  
<com.github.hoomanmmd.fadeoutparticle.xml.FadeOutParticleFrameLayout
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