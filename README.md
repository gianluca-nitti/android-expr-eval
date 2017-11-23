# android-expr-eval
Android application to solve math expressions. Based on [java-expr-eval](https://github.com/gianluca-nitti/java-expr-eval).

## New features
Now supports [ACTION_PROCESS_TEXT](https://medium.com/google-developers/custom-text-selection-actions-with-action-process-text-191f792d2999#.ir1ubmtgz) intent on Android 6.0+, i.e. you can evaluate an expression from any application using ExprEval and replace it with the result.
See this animation for an example:

![ACTION_PROCESS_TEXT example](http://i.imgur.com/r0xHgQH.png)

A custom intent is also available: you can use `startActivityForResult()` with `com.github.gianlucanitti.expreval.ACTION_EVAL` Intent to have an expression evaluated.
You have to `putExtra()` the expression with `"expression"` as the extra name, and the result will be returned in an extra named `"result"`.

This feature was proposed in [issue #1](https://github.com/gianluca-nitti/android-expr-eval/issues/1).

## Download/compile
See the releases page for prebuilt APK.
To build from source, clone the repository, make sure you have the Android SDK installed (and you have the ANDROID_HOME environment variable correctly set), and run
```
./gradlew assembleRelease
```
(you may need to `chmod +x gradlew` it if it's not already executable).

## Contributed translations
* [@afmachado](https://github.com/afmachado): Brazilian Portuguese

## Screenshots ([Imgur album](http://imgur.com/a/wOJfT))
![Main activity](http://i.imgur.com/DJSZBVH.png)
![Context dialog](http://i.imgur.com/uCcd0Ih.png)
![New variable dialog](http://i.imgur.com/RTTOVlu.png)
![New function dialog](http://i.imgur.com/cro2lgq.png)