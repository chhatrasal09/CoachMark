# CoachMark
[![](https://jitpack.io/v/chhatrasal09/CoachMark.svg)](https://jitpack.io/#chhatrasal09/CoachMark)

This library focus on giving tour about your application and its features. You can highlight the where to tap to enable or disable the feature. Show the first time user, how to use the feature by showcasing the feature step by step. This sample app will demostrate how to use this library

### Getting started

Add this in your root `build.gradle` file (not your module build.gradle file):
```gradle

allprojects {
	repositories {
        maven { url "https://jitpack.io" }
    }
}

```
Then, add the library to your module `build.gradle`
```gradle

dependencies {
    implementation 'com.github.chhatrasal09:CoachMark:v0.1'
}

```

### Requirements
<ul>
<li>minSdk : 19</li>
<li>compileSDK : 28</li>
</ul>

### Usages
There is a sample provided which shows how to use the library in a more advanced way, but for completeness, here is all that is required to get CoachMark running

```kotlin

    CoachMarkOverlay.Builder(context)
                .setOverlayTargetView(targetView)
                .setInfoViewBuilder(
                    CoachMarkInfo.Builder(context)
                        .setInfoText("TextString)
                        .setMargin(30, 30, 30, 30)
                )
                .setSkipButtonBuilder(
                    CoachMarkSkipButton.Builder(context)
                        .setButtonClickListener(object : CoachMarkSkipButton.ButtonClickListener {
                            override fun onSkipButtonClick(view: View) {
                                (window.decorView as ViewGroup).removeView(view)
                                coachMarkSequence.clearList()
                                button?.setText("Text")
                        })
                )
                .build()
                .show(viewToAttachCoachMark)
                
```


### Screenshots
![](/screenshots/view-1.gif) ![](/screenshots/view-2.gif)

## License
```licence

Copyright 2019 Chhatrasal Singh Bundela

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
   
```

