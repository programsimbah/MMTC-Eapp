## Add project specific ProGuard rules here.
## You can control the set of applied configuration files using the
## proguardFiles setting in build.gradle.
##
## For more details, see
##   http://developer.android.com/guide/developing/tools/proguard.html
#
#-keep class com.pengembangsebelah.stmmappxo.**{*;}
## If your project uses WebView with JS, uncomment the following
## and specify the fully qualified class name to the JavaScript interface
## class:
##-keepclassmembers class fqcn.of.javascript.interface.for.webview {
##   public *;
##}
#-dontobfuscate
#-keep class com.pengembangsebelah.stmmappxo.model.** {*;}
#-keep class com.koushikdutta.urlimageviewhelper.** {*;}
#-keep class com.leocardz.** {*;}
#-keep class com.jakewharton.** {*;}
#-keep class com.github.bumptech.glide.** {*;}
#
## Uncomment this to preserve the line number information for
## debugging stack traces.
##-keepattributes SourceFile,LineNumberTable
#
## If you keep the line number information, uncomment this to
## hide the original source file name.
##-renamesourcefileattribute SourceFile
#
##firebase
#-keep class com.google.firebase.**{*;}
#-keepattributes Signature
#-keepattributes *Annotation*
##facebook start
#
#-keep class com.facebook.**{*;}
#-keepattributes Signature
#
## ads -start
#
#-keep public class com.google.android.gms.ads.** {
#    public *;
#}
#
#-keep public class com.google.ads.** {
#    public *;
#}
#
#
##play service google
##
##-keep class * extends java.util.ListResourceBundle {
##    protected Object[][] getContents();
##}
##-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
##    public staic final ** NULL;
##}
##
##-keepnames @com.google.android.gms.common.annotation.KeepName class *
##-keepclassmembernames class * {
##    @com.google.android.gms.common.annotation.KeepName *;
##}
##
##-keepnames class * implements android.os.Parcelable {
##    public static final ** CREATOR;
##}
#
##twitwer -start
#
#-dontwarn twitter4j.**
#-keep class twitter4J.** {*;}
#
#
## inmobi - start
#-keepattributes SourceFile,LineNumberTable
#-keep class com.inmobi.** { *; }
#-dontwarn com.inmobi.**
#-keep public class com.google.android.gms.**
#-dontwarn com.google.android.gms.**
#-dontwarn com.squareup.picasso.**
#-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient{public *;}
#-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info{public *;}
#
##skip the Picasso library classes
#-keep class com.squareup.picasso.** {*;}
#-dontwarn com.squareup.picasso.**
#-dontwarn com.squareup.okhttp.**
#
##skip Moat classes
#-keep class com.moat.** {*;}
#-dontwarn com.moat.**
#
##skip AVID classes
#-keep class com.integralads.avid.library.** {*;}
##inmobi nd
