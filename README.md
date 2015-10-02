Simple Spring Boot app that runs on Google AppEngine. It uses spring-security to secure URI's and redirects user to the google login page if a user is not logged in.
Depends on [spring-boot-legacy](https://github.com/scratches/spring-boot-legacy) (which you will need to build and install locally):

```
$ git clone https://github.com/tharms/tomadmin.git
$ (cd tomadmin; mvn gae:deploy)
```

Also runs as a deployed WAR in WTP or regular Tomcat container. The `main()` app (normal Spring Boot launcher) should also work.
