# Configuration

Platform & Preparation:
====
1. Machinne: raspberry Pi 2B
2. OS: Ubuntu Mate 16.06
3. ISP: dynamic ip type, so need to set DDNS, use noip2
4. Storage: 32GB
5. Connection: Wireless

Hosting sftp site
====
let "hkjc" be the client account only allowed sftp but not ssh variable

user, group and home directory permission settings:

   1.users
   
		sudo groupadd hkjc
		sudo useradd -s /sbin/nologin -g hkjc -d /home/hkjc hkjc
		sudo passwd hkjc #not necessary, but just set password to hkjc
		
   2.home directory
	
		sudo mkdir -p /home/hkjc
		sudo chown root:hkjc /home/hkjc/ #must be owned by root and grouped by hkjcfor sshd to chroot
		sudo chmod 750 /home/hkjc/ #group cannot writeable
		
Reference:
		
    http://askubuntu.com/questions/134425/how-can-i-chroot-sftp-only-ssh-users-into-their-homes

SSH key Generation (flexible)

	0. sudo su #login root
	1. create .ssh/ (mod 700, owned by hkjc and grouped hkjc) under hkjc's home directory, and touch authorized_keys (mod 600, owned by hkjc and grouped by hkjc) under .ssh/
	2. append allowed host pub key to authorized_keys
	3. generate ssh private key (ssh-keygen -t rsa) under /home/hkjc/rsaKeys for distribution to users
	4. under hkjc's home, just use chown -R hkjc .ssh/ and chgrp -R hkjc .ssh/ to set the group and ownership
ssh key settings

	1. need to give private key to filezilla, but host need pub only
	
Reference:

    http://www.tecmint.com/restrict-sftp-user-home-directories-using-chroot/

SSH settings
====
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

Install mongodb
====
Install mongodb 2.x, check whether the mongo java driver version supports the mongodb installed, now using 3.2.x
	
	1. sudo apt-get install
	2. sudo apt-get install mongodb-server
	3. (optional) Install specified version of mongodb
		https://docs.mongodb.com/manual/tutorial/install-mongodb-on-ubuntu/

Install JKD 1.8
===============
Install by ppa

	1. sudo apt-get install software-properties-common python-software-properties
	2. sudo add-apt-repository ppa:webupd8team/java
	3. sudo apt-get update
	4. sudo apt-get install oracle-java8-installer
Configure

	1. sudo echo "export JRE_HOME="/usr/lib/jvm/java-8-oracle/jre"" >> /etc/environment
	2. logout
	3. echo $JRE_HOME (optional)

Install Tomcat 9.0
====
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

Ubuntu 14.04

	1. sudo groupadd tomcat
	2. sudo useradd -s /bin/false -g tomcat -d /opt/tomcat9 tomcat
	3. sudo usermod -a -G tkingless tomcat
	4. cd /opt/tomcat9; sudo chown -R tomcat ./
	5. sudo chmod g+rwx /opt/tomcat9/conf /opt/tomcat9/webapps #for tkingless convenience
	6. sudo chmod g+r /opt/tomcat9/conf/*
	7. copy ForConfigurationUse/tomcat.conf (in this repo) to /etc/init/tomcat.conf
 	8. sudo initctl reload-configuration
 	9. sudo initctl start tomcat
	10. sudo sh /opt/tomcat9/bin/startup.sh #start tomcat manually
	11. sudo sh /opt/tomcat9/bin/shutdown.sh #shutdown tomcat manually
	12. check if start success:
 		sudo vi /var/log/upstart/tomcat.log

 Ubuntu 16.04

	1. sudo groupadd tomcat
	2. sudo useradd -s /bin/false -g tomcat -d /opt/tomcat9 tomcat
	3. sudo usermod -a -G tkingless tomcat
	4. cd /opt/tomcat9; sudo chown -R tomcat ./
	5. sudo chmod g+rwx /opt/tomcat9/conf /opt/tomcat9/webapps #for tkingless convenience
	6. sudo chmod g+r /opt/tomcat9/conf/*
	7. copy ForConfigurationUse/tomcat.service (in this repo) to /etc/systemd/system/
 	8. sudo systemctl daemon-reload
 	9. sudo systemctl start tomcat

Reference:

	https://www.digitalocean.com/community/tutorials/how-to-install-apache-tomcat-8-on-ubuntu-16-04

Debugging
====
	1. sudo vi /var/log/upstart/tomcat.log 
	2. sudo vi /opt/tomcat9/logs/catalina.out
	3. sudo -s -tomcat find . -type d -name 'webapps'
	4. sudo -s -u tomcat find . -uid 0

Create client user
====
sudo usermod -a -G tomcat hkjc


Import war
==========

    1. Build the war from gradle task, AppServer-> Task -> built -> war
    2. Can use tomcat app manager to import the war
    3. cd to /opt/tomcat9
        edit /opt/tomcat9/webapps/webcrawling0.9/WEB-INF/web.xml,
	    (the main servlet to enable)
	4./opt/tomcat9/webapps/webcrawling0.9/WEB-INF/classes/WCDIOconfig.json
	(the file sharing path)
    5. log4j2_productionTomcat.xml overwrite to log4j2.xml
    6.for file sharing part, the root folder set owned by tomcat mannually first, just make sure tomcat has the write access to the folder, example:
      in WCDIOconfig.json, the root is /a/b/c, then /a/b/c is owned by tomcat


**Final Check**
====
Is system time read by java is using HKT?
In terminal enter "date", should show time read in HKT
From webcrawling main log read it to debug if no event log found






