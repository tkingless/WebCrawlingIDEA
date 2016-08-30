configuration from zero

Platform:
MachineRPi 2B
OS: Ubuntu Mate 16.06
Network service provider: PCCW, dynamic ip type, so need to set DDNS
Storage: 32GB

=====hosting sftp site (secure shell)
let hkjc be the client account only allowed sftp but not ssh variable

user, group and home directory permission settings:
	1.users
		sudo groupadd hkjc
		sudo useradd -s /sbin/nologin -g hkjc -d /home/hkjc hkjc
		sudo passwd hkjc #not necessary, but just set password to hkjc
	2.home directory
		sudo mkdir -p /home/hkjc
		sudo chown root:hkjc /home/hkjc/ #must be owned by root and grouped by hkjcfor sshd to chroot
		sudo chmod 750 /home/hkjc/ #group cannot writeable
http://askubuntu.com/questions/134425/how-can-i-chroot-sftp-only-ssh-users-into-their-homes

.ssh
	0. sudo su #login root
	1. create .ssh/ (mod 700, owned by hkjc and grouped hkjc) under hkjc's home directory, and touch authorized_keys (mod 600, owned by hkjc and grouped by hkjc) under .ssh/
	2. append allowed host pub key to authorized_keys
	3. generate ssh private key (ssh-keygen -t rsa) under /home/hkjc/rsaKeys for distribution to users
	4. under hkjc's home, just use chown -R hkjc .ssh/ and chgrp -R hkjc .ssh/ to set the group and ownership
ssh key settings
	1. need to give private key to filezilla, but host need pub only
http://www.tecmint.com/restrict-sftp-user-home-directories-using-chroot/

ssh settings
sshd_config
	edit /etc/ssh/sshd_config:
	1. disable password authetication (PasswordAuthentication no)
	2. disable default sftp system if exist (#Subsystem sftp /usr/lib/openssh/sftp-server)
		reference only: UsePam set to Yes, UseDNS set to No
	3. add at the very bottom:
		Subsystem sftp internal-sftp
		Match Group hkjc #hkjc is the group created, customizable
		ChrootDirectory %h
		ForceCommand internal-sftp
		AllowTcpForwarding no
		X11Forwarding no

After all done, restart ssh service

=======install mongodb
Install mongodb 2.4, check whether the mongo java driver version supports the mongodb installed
	1. sudo apt-get install
	2. sudo apt-get install mongodb-server
	3. (optional) Install specified version of mongodb
		https://docs.mongodb.com/manual/tutorial/install-mongodb-on-ubuntu/

	http://andyfelong.com/2016/01/mongodb-3-0-9-binaries-for-raspberry-pi-2-jessie/

=======Install jdk
Install by ppa
	1. sudo apt-get install software-properties-common python-software-properties
	2. sudo add-apt-repository ppa:webupd8team/java
	3. sudo apt-get update
	4. sudo apt-get install oracle-java8-installer
Configure
	1. sudo echo "export JRE_HOME="/usr/lib/jvm/java-8-oracle/jre"" >> /etc/environment
	2. logout
	3. echo $JRE_HOME (optional)


=======Install Tomcat
Install things
	1. cd ~/Downloads
	2. wget http://apache.communilink.net/tomcat/tomcat-9/v9.0.0.M9/bin/apache-tomcat-9.0.0.M9.tar.gz
	3. tar xzf apache-tomcat-9.0.0.M9.tar.gz 
	4. mv apache-tomcat-9.0.0.M9 tomcat9
	5. sudo mv tomcat9/ /opt/
	
Configuration
	1. echo "export CATALINA_HOME="/opt/tomcat9"" >> /etc/environment
	2. sudo vi /opt/tomcat9/conf/tomcat-users.xml 
		edit tomcat login user password etc.
		add <role rolename="manager-gui"/> to tomcat user
	3. sudo vi /opt/tomcat9/webapps/manager/META-INF/context.xml
		change allow="127\.\d+\.\d+\.\d+|::1|0:0:0:0:0:0:0:1" as
		allow="^.*$"
User and Group
	1. sudo groupadd tomcat
	2. sudo useradd -s /bin/false -g tomcat -d /opt/tomcat9 tomcat
	3. sudo usermod -a -G tkingless tomcat
	4. cd /opt/tomcat9; sudo chown -R tomcat ./
	5. sudo chmod g+rwx /opt/tomcat9/conf /opt/tomcat9/webapps #for tkingless convenience
	6. sudo chmod g+r /opt/tomcat9/conf/*
	7. sudo vi /etc/init/tomcat.conf, paste as:



description "Tomcat Server"

  start on runlevel [2345]
  stop on runlevel [!2345]
  respawn
  respawn limit 10 5

  setuid tomcat
  setgid tomcat

script
  export JAVA_HOME=/usr/lib/jvm/java-8-oracle
  export JRE_HOME=/usr/lib/jvm/java-8-oracle/jre
  export CATALINA_HOME=/opt/tomcat9

  # Modify these options as needed
  #env JAVA_OPTS="-Djava.awt.headless=true -Djava.security.egd=file:/dev/./urandom"
  #env CATALINA_OPTS="-Xms256M -Xmx512M -server -XX:+UseParallelGC"
  export JAVA_OPTS="-Djava.awt.headless=true -Djava.security.egd=file:/dev/./urandom"
  export CATALINA_OPTS="-Xms256M -Xmx512M -server -XX:+UseParallelGC"
  #export TOMCAT_OPTS="-Dlog4j.configuration=/opt/tomcat9/webapps/webcrawling/WEB-INF/classes/log4j2.xml"

  exec $CATALINA_HOME/bin/catalina.sh run
  #exec $CATALINA_HOME/bin/catalina.sh start
  #exec $CATALINA_HOME/bin/startup.sh
end script

  # cleanup temp directory after stop
  post-stop script
    rm -rf $CATALINA_HOME/temp/*
  end script



 	8. sudo initctl reload-configuration #no initctl at Ubuntu 16
 	9. sudo initctl start tomcat #no initctl at Ubuntu 16
	10. sudo sh /opt/tomcat9/bin/startup.sh #start tomcat manually
	11. sudo sh /opt/tomcat9/bin/shutdown.sh #shutdown tomcat manually

	12. check if start success:
 		sudo vi /var/log/upstart/tomcat.log 

Debug use:
	1. sudo vi /var/log/upstart/tomcat.log 
	2. sudo vi /opt/tomcat9/logs/catalina.out
	3. sudo -s -tomcat find . -type d -name 'webapps'
	4. sudo -s -u tomcat find . -uid 0

======create client user
sudo usermod -a -G tomcat hkjc


====after import war into tomcat,
stop the webcraling app, login
cd to /opt/tomcat9

edit /opt/tomcat9/webapps/webcrawling0.9/WEB-INF/web.xml,
	(the servlet to enable)

	/opt/tomcat9/webapps/webcrawling0.9/WEB-INF/classes/WCDIOconfig.json
	(the file sharing path)

	log4j2_productionTomcat.xml overwrite to log4j2.xml


for file sharing part, the root folder set owned by tomcat mannually first, just make sure tomcat has the write access to the folder, example:
in WCDIOconfig.json, the root is /a/b/c, then /a/b/c is owned by tomcat


***Final Check: is system time read by java is using HKT? from webcrawling main log read it






