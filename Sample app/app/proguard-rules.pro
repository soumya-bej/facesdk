# -------------------------------------------------------------------
# App-level ProGuard rules
# These apply to the app module only — NOT the SDK.
# The SDK's own rules are in face-auth-sdk/proguard-rules.pro
# -------------------------------------------------------------------

# R8-generated: suppress warning for bnd annotation library
# (transitive dependency from one of the SDK's libraries — not used at runtime)
-dontwarn aQute.bnd.annotation.spi.ServiceProvider

# aalto-xml StAX implementation — Jackson DataFormat XML uses ServiceLoader to find this.
# Without this rule, R8 strips it as unreferenced and the app crashes:
# "Provider com.bea.xml.stream.MXParserFactory not found"
-keep class com.fasterxml.aalto.** { *; }
-dontwarn com.fasterxml.aalto.**
-dontwarn org.codehaus.stax2.**
-keep class com.uidai.faceauth.auths.AuthParams { public *; }

# Keep the app's own classes (Activities, Fragments etc.)
-keep class com.example.faceauthapp.** { *; }

# Keep line numbers for readable crash stack traces
-keepattributes SourceFile, LineNumberTable
-renamesourcefileattribute SourceFile
