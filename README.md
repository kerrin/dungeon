
http://dist.springsource.com/release/STS/3.7.3.RELEASE/dist/e4.5/spring-tool-suite-3.7.3.RELEASE-e4.5.2-win32.zip
Copy "sts-3.7.3.RELEASE" folder in zip to somewhere on your filesystem.

http://download.jboss.org/jbossas/7.0/jboss-as-7.0.2.Final/jboss-as-web-7.0.2.Final.zip
Copy "jboss-as-web-7.0.2.Final" folder in zip to somewhere on your filesystem.

Gitbash
Run the exe to install

Make sure you have JDK 7 installed:
http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html

You will need something to create and load a public/private key (PPK). I use Putty:
http://www.chiark.greenend.org.uk/~sgtatham/putty/download.html
You will need pagent, puttygen and plink
Create a key and save both the public and private keys, then send me the contents of the PUBLIC key from the text area of file and load the private key in pagent.

Download Maven:
http://www.mirrorservice.org/sites/ftp.apache.org/maven/maven-3/3.3.9/binaries/apache-maven-3.3.9-bin.zip
Copy "apache-maven-3.3.9" folder in zip to somewhere on your filesystem.

Open a command prompt window (cmd) and run:
```
# Save the host when prompted
plink -v git@github.com

# (where 'C:\putty\plink.exe' is the location of the plink.exe)
setx GIT_BASH C:\putty\plink.exe 
# (where 'C:\Program Files\Java\jdk1.7.0_79' is where you installed the JDK)
setx JAVA_HOME "C:\Program Files\Java\jdk1.7.0_79" 
# (where 'C:\apache-maven-3.3.9' is where you copied the maven folder, note the addition on "/bin')
setx PATH "%PATH%;C:\apache-maven-3.3.9\bin"
ssh-add <id_rsa> file

In gitbash:
# Make a git folder and move in to it
mkdir git
cd git

# Checkout the repository
git clone git@github.com:kerrin/dungeon.git
```

Download and Install MYSQL 5.5:
32 Bit: https://dev.mysql.com/downloads/mysql/5.5.html

Create a database called "dungeon_db"
Create a user with access to the database
Open the following configuration file in the dungeon project you checked out from git:
dungeon\src\main\resources\application.eclipse.properties
Modify the following configuration lines to match your settings:
  db.url
  db.username
  db.password
  
Copy the "application.eclipse.properties" over "application.properties"

Start up STS (Eclipse):
  Set the workspace folder (the default one is fine)
  If you get prompted to update at the lower right of the window, do so
  
  In the servers tab press the link "No servers are available...."Select the "JBoss Community"/"RedHat Jboss/WildFly" folder and then "JBoss AS 7.0"
  Press "Next" twice
  Set the home directory by pressing "Browse" and finding the location you copied the "jboss-as-web-7.0.2.Final" folder to
  Press "Finish"
  
  Open the  "Window" menu and select "preferences"
  Select "Java->Installed JRE
  Press "Add" and set the "JRE home" to the JDK directory (default location is "C:\Program Files\Java\jdk1.7.0_79")
  Press "Finish"
  Select the JDK check box
  Press "OK"
  
  Right click in "Package Explorer" panel
  Select "Import->Maven (folder)->Existing Maven Projects" and press "Next"
  Browse to the root folder where you checked out the "dungeon" with git. e.g. "C:/git/dungeon"
  Press "Finish"
  
  Drag the project down to JBoss
  Start up jboss
  In a browser got to "http://localhost:8080/dungeon/"
