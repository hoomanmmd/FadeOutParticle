FadeOutParticle
===============  
FadeOutParticle is an animation for disappearing composables and views like Text and Icon
<h1 align="center">
<img src="/preview.gif?raw=true" alt="FadeOutParticle" />
</h1>

Installation
------------
Add it in your settings.gradle.kts at the end of repositories:
```
dependencyResolutionManagement {
	repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
	repositories {
		mavenCentral()
		maven { url = uri("https://jitpack.io") }
	}
}
```
### Compose

Add the dependency
```
implementation("com.github.hoomanmmd.FadeOutParticle:compose:2.0.6")
```
#### Usage
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

### XML
Add the dependency
```
implementation("com.github.hoomanmmd.FadeOutParticle:xml:2.0.6")
```
#### Usage
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