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
- install MySQL
- create MySQL database. See pom.xml for details
- install your favorite JDK v7 or higher (Oracle or OpenJDK)
- install Maven
- install your favorite J2EE IDE and import the Maven project
- optional: install the JRebel plugin for your J2EE IDE to speed up JVM based development
- install your favorite Java Servlet 3.0 Spec compatible web container and integrate it into your J2EE IDE
- sign up with BugSnag
- sign up with SparkPost
- sign up with Cloudflare
- setup environment variables


## run tests
```shell
mvn clean test
```

## run app
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

### barebones
```shell
mvn clean package -DskipTests spring-boot:run
```

### with JRebel
```shell
mvn clean package -DskipTests spring-boot:run "-Drun.jvmArguments='-agentpath:/~/Library/Application Support/IdeaIC2017.2/jr-ide-idea/lib/jrebel6/lib/libjrebel64.dylib'"
```

### with JRebel and IntelliJ

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

todo: describe CircleCI to AWS deployment process

