# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-keepattributes Signature
-keepattributes *Annotation*,InnerClasses
-keepattributes *JavascriptInterface*

-dontwarn com.fasterxml.jackson.**
-dontwarn com.google.common.**
-dontwarn com.kenai.jffi.**
-dontwarn jnr.posix.**
-dontwarn okio.**
-dontwarn org.bitcoinj.store.**
-dontwarn org.bouncycastle.**
-dontwarn org.slf4j.**
-dontwarn org.spongycastle.**
-dontwarn rx.internal.util.unsafe.**
-keep class com.cfwin.base.db.**
-keep class com.cfwin.base.utils.encoded.**{
*;
}
-keepclassmembernames class com.cfwin.cfwinblockchain.utils.**{
*;
}
-keep class * extends com.cfwin.base.db.AbsTableOpera{
*;
}
-keep class com.cfwin.cfwinblockchain.beans.**{
*;
}
-keep class com.cfwin.base.beans.**{
*;
}
-keep class io.github.novacrypto.**{*;}
-keep class org.bitcoin.**{*;}
-keep class org.bitcoinj.**{*;}
-keep class com.subgraph.orchid.**{*;}
-keep class net.jcip.annotations.**{*;}
-keep class javax.inject.**{*;}
-keep class com.kenai.jffi.**{*;}
-keep class com.kenai.constantine.**{*;}
-keep class jnr.constants.**{*;}
-keep class jnr.enxio.channels.**{*;}
-keep class jnr.ffi.**{*;}
-keep class jnr.posix.**{*;}
-keep class jnr.enxio.channels.**{*;}
-keep class jnr.unixsocket.**{*;}
-keep class com.kenai.jnr.x86asm.**{*;}
-keep class jnr.x86asm.**{*;}
-keep class com.google.common.**{*;}
-keep class com.google.thirdparty.publicsuffix.**{*;}
-keep class com.google.protobuf.**{*;}
-keep class com.lambdaworks.**{*;}
-keep class org.spongycastle.**{*;}
-keep class rx.internal.util.unsafe.**{*;}
-keep class okhttp3.**
-keep class okio.**
-keep class org.apache.commons.codec.**{*;}
-keep class org.bouncycastle.**{*;}
-keep class org.objectweb.asm.**{*;}
-keep class org.slf4j.**{*;}
-keep class org.web3j.**{
*;
}

-keepclassmembers class fqcn.of.javascript.interface.for.Webview {
   public *;
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, jav.lang.String);
}