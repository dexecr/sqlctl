native-image -cp build/libs/*: --no-fallback -o build/bin/sqlctl \
 -J--add-exports=org.graalvm.nativeimage.builder/com.oracle.svm.core.configure=ALL-UNNAMED \
 -J--add-exports=org.graalvm.sdk/org.graalvm.nativeimage.impl=ALL-UNNAMED \
 -H:Features=com.dexecr.nativeimage.JdbcFeature \
 -H:Name=sqlctl \
 -H:Class=com.dexecr.Main