[![Build Status](https://travis-ci.org/debueb/padel.koeln.svg?branch=master)](https://travis-ci.org/debueb/padel.koeln)

# pro-padel

This repository contains the source code for the padel club managment software behind [padel.koeln](https://padel.koeln), [walls.de](https://walls.de) and [pro-padel.de](http://pro-padel.de)


## technologies used
- [Maven](https://maven.apache.org/) - Java dependency managment and build process
- [webpack](https://webpack.js.org) - Javascript minification and bundling
- [Spring](https://spring.io/) - excellent Backend framework for 
- [Hibernate](http://hibernate.org/) - object relational mapper
- [MySQL](https://www.mysql.com/)  - relational database
- [sparkpost](http://sparkpost.com) - mail relay service
- [LessCSS](http://lesscss.org) - CSS pre processor
- [BugSnag](https://bugsnag.com) - bug tracking platform
- [Openshift Online](http://openshift.com) - PAAS hosting
- [Cloudflare](https://cloudflare.com) - CDN with excellent API
- [Node](http://nodejs.org) - JS Runtime used during build process
- [NPM](https://www.npmjs.com) - Node Package Manager
- [JRebel](https://zeroturnaround.com/software/jrebel/) - JVM Hot Code replacement


## running instructions
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
- sign up with Openshift Online 2.x
- setup environment variables
```shell
export BUGSNAG_API_KEY=[BUGSNAG API KEY]
export SPARKPOST_DEFAULT_SENDER=[SPARKPOST DEFAULT SENDER]
export SPARKPOST_API_KEY=[SPARKPOST API KEY]
export CLOUDFLARE_API_EMAIL=[CLOUDFLARE API EMAIL]
export CLOUDFLARE_API_KEY=[CLOUDFLARE API KEY]
export OPENSHIFT_USERNAME=[OPENSHIFT ONLINE USERNAME]
export OPENSHIFT_PASSWORD=[OPENSHIFT ONLINE PASSWORD]
```
- run or debug the application with your J2EE IDE
- alternatively, run `mvn clean package`, which will generate `target/ROOT.war`

### keeping js up to date during development
- install node
- install npm
- run `npm run watch` to have webpack create your bundle.js and bundle.map.js after editing any of the javascript source files

## deployment instructions
- install node
- install npm
- run the webpack build process that minifies and bundles our JS. This process is actually part of the maven build process. However, due to limitations of the Openshift Online build environment, which currently only supports Maven 3.0.4, this process is currently only active in the development profile. Once Openshift Online has upgraded Maven to a current version, this step can be skipped
```shell
npm run build
```
- alternatively, you can just run build the development profile of the maven project. This will download node and npm and run webpack (same as above) and additionally compile and package the Java app and run all of the tests. 
```shell
mvn clean package
```
- commit changes and push to the desired openshift git repository. The openshift build process will kick in and deploy the application
```shell
git add .
git commit -am "comment"
git push openshift-remote
```

