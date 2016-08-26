
cp out/artifacts/plugin_zip/build-agents-diff.zip /Users/gilescope/.BuildServer/plugins/
killall java
rm /Users/gilescope/TeamCity/logs/*
../../../TeamCity/bin/startup.sh
tail -f /Users/gilescope/TeamCity/logs/catalina.out 
 
