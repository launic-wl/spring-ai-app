# Go to spring-ai-app folder
cd [path-to]/spring-ai-app/

# This project requires Java 21 
# I have used Java 21 Temurin, download from this link
https://adoptium.net/temurin/releases/?os=mac&arch=aarch64&package=jdk
# The installation for MacOS will be present in this folder
/Library/Java/JavaVirtualMachines/temurin-21.jdk/Contents/Home

# - if on MacOs and running this command shows Java 21
/usr/libexec/java_home

# Then run the following command using the project script "mvn-j21.sh"
# - this is required in order to have Java 21 only for this project and keep a different JAVA_HOME for other projects
./mvn-j21.sh spring-boot:run  
