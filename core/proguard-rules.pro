# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Program Files (x86)\Android\android-studio\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-dontoptimize

#Amazon AWS
#-libraryjars libs/aws-android-sdk-1.7.0-core-no-third-party.jar
#-libraryjars libs/aws-android-sdk-1.7.0-s3.jar
#-dontwarn com.amazonaws.**
#-keep class com.amazonaws.services.s3.model.AmazonS3Exception { *; }
#Amazon AWS


#Crashlytics
#-libraryjars libs/crashlytics.jar
#-keep class com.crashlytics.** { *; }
#-keepattributes SourceFile,LineNumberTable
#Crashlytics


# keep all classes that might be used in XML layouts
-keep public class * extends android.view.View
-keep public class * extends android.view.ViewGroup
-keep public class * extends android.support.v4.app.Fragment

# remove all logs
# -assumenosideeffects class android.util.Log { *; }

##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
#-libraryjars libs/gson-2.3.jar
#-keepattributes Signature

# For using GSON @Expose annotation
#-keepattributes *Annotation*

# Gson specific classes
#-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
#-keep class com.google.gson.examples.android.model.** { *; }

##---------------End: proguard configuration for Gson  ----------