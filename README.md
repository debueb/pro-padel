[![Build Status](https://travis-ci.org/debueb/pro-padel.svg?branch=master)](https://travis-ci.org/debueb/pro-padel)

# pro-padel

This repository contains the source code for the padel club managment software behind [walls.de](https://walls.de) and [pro-padel.de](http://pro-padel.de)


## technologies used
- [Maven](https://maven.apache.org/) - Java dependency managment and build process
- [webpack](https://webpack.js.org) - Javascript minification and bundling
- [Spring Boot](https://spring.io/) - excellent backend framework
- [Hibernate](http://hibernate.org/) - object relational mapper
- [MySQL](https://www.mysql.com/)  - relational database
- [sparkpost](http://sparkpost.com) - mail relay service
- [LessCSS](http://lesscss.org) - CSS pre processor
- [BugSnag](https://bugsnag.com) - bug tracking platform
- [Cloudflare](https://cloudflare.com) - CDN with excellent API
- [Node](http://nodejs.org) - JS Runtime used during build process
- [NPM](https://www.npmjs.com) - Node Package Manager
- [JRebel](https://zeroturnaround.com/software/jrebel/) - JVM Hot Code replacement


## setup instructions
- clone this repo
- install MySQL v5.7 or higher
- create a new MySQL user and db
- install your favorite JDK v8 or higher (Oracle or OpenJDK)
- install Maven v3.5.0 or higher
- install your favorite Java IDE and import the Maven project
- optional: install the JRebel plugin for your J2EE IDE to speed up JVM based development
- sign up with BugSnag
- sign up with SparkPost
- sign up with Cloudflare

## setup environment variables
```shell
export BUGSNAG_API_KEY=[BUGSNAG API KEY]
export SPARKPOST_DEFAULT_SENDER=[SPARKPOST DEFAULT SENDER]
export SPARKPOST_API_KEY=[SPARKPOST API KEY]
export CLOUDFLARE_API_EMAIL=[CLOUDFLARE API EMAIL]
export CLOUDFLARE_API_KEY=[CLOUDFLARE API KEY]
export SPRING_DATASOURCE_PASSWORD=[DB PASS]
export SPRING_DATASOURCE_URL=[jdbc:mysql://host:port/dbName]
export SPRING_DATASOURCE_USERNAME=[DB USER]
```

### run tests
```shell
mvn clean test
```

### run on command line
```shell
mvn clean package -DskipTests spring-boot:run
```

### run on command line with JRebel
```shell
mvn clean package -DskipTests spring-boot:run "-Drun.jvmArguments='-agentpath:/~/Library/Application Support/IdeaIC2017.2/jr-ide-idea/lib/jrebel6/lib/libjrebel64.dylib'"
```

### run in IntelliJ with JRebel

as above, and [add IntelliJ macro and keymap](https://gist.github.com/debueb/50966c527ea443bb4cc7f455f5d833b6)

### keeping js up to date during development
- install node
- install npm
- run `npm run watch` to have webpack create your bundled resources and resource maps after editing any of the javascript source files

## deployment instructions
```shell
mvn clean package
```
this will generate two war files in the target/ directory
- ROOT.war - this is a runnable Spring Boot war file. Start it with java -jar ROOT.war
- ROOT.war.original - this is a regular war file that you can deploy to an existing servlet container

### AWS - manual deployment

- due to an issue [[1](https://stackoverflow.com/questions/42769918/deploy-spring-boot-with-jsp-to-elastic-beanstalk), [2](https://stackoverflow.com/questions/41786136/spring-boot-unable-to-find-jsp-files-on-elasticbeanstalk-aws)] with jsp files in runnable jar Spring Boot (ROOT.war) apps on Amazon Web Services, the app must be deployed in a regular Tomcat instance (ROOT.war.original)
- setting up AWS is fairly straighforward
  - create an AWS account. you will need a credit card and a phone number
  - on the [AWS console](console.aws.amazon.com) first change the region of the datacenter (top right)
  - Elastic Beanstalk
    - Create New Application -> Web Server Environment -> Platform: Tomcat -> Application code: Root.war.original -> Configure more options -> Database Modify -> Engine: mysql, Retention: Delete -> wait for AWS to provision machines
    - All Applications / Application / Application Environment -> Configuration -> RDS Endpoint -> Details tab -> Security Groups -> Actions -> Edit inbound rules -> Type: Custom TCP, Port 3306, Source: My IP - Connect to MySQL using your favorite MySQL client and import DB as necessary
    - All Applications / Application / Application Environment -> Configuration -> Software Configuration -> Setup Environment Properties as described above
    - All Applications / Application / Application Environment -> Upload and Deploy new war.original file

### AWS - automatic deployment

- automatic deployment to AWS is done via [travis-ci.org](https://docs.travis-ci.com/user/deployment/elasticbeanstalk/)
- SSL on the AWS instance is required because the application uses the request URL to build URLS
- if SSL is terminated on Cloudflare with Flexible SSL (essentially requesting the app via http://) the constructed http:// redirects will fail in the browser
- in order to support SSL on the AWS single instance we could use letsencrypt with [certbot-auto](https://blog.lucasferreira.org/howto/2017/07/21/set-up-let-s-encrypt-ssl-certificate-with-aws-elastic-beanstalk-single-instance.html)
- however, certbot-auto requires that a) it is accessible on port 80 in order to confirm domain ownership or b) using the `--webroot` that an existing Apache config can serve .well-known files
- both a) and b) are [not compatible](https://support.cloudflare.com/hc/en-us/articles/214820528-How-to-Validate-a-Let-s-Encrypt-Certificate-on-a-Site-Already-Active-on-Cloudflare) with http:// to https:// page rule redirects on Cloudflare
- therefore, we set Cloudflare to Full SSL (non strict), which allows self signed certificates
- the self signed certificate is installed from environment variables (https://github.com/debueb/pro-padel/blob/aws/src/main/webapp/.ebextensions/https-instance-securitygroup.config)