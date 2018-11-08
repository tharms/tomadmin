Tool to administrate the career path of technology employees. 
Simple Spring Boot app that runs on Google AppEngine. It uses spring-security to secure URI's and redirects user to the google login page if a user is not logged in.
Depends on [spring-boot-legacy](https://github.com/scratches/spring-boot-legacy) (which you will need to build and install locally):

```
$ git clone https://github.com/tharms/tomadmin.git
$ cd tomadmin 
$ mvn gae:run # running it locally on port 8080
# update appengine-web.xml with application information from google
$ mvn gae:deploy # deploy to google app engine
```

