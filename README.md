# Cybersecuritybase-Project

This project is developed for the [Cyber Security Base](https://cybersecuritybase.github.io/) 
course. The idea of this project is to be a course signup page with 
user registration and the possibility to enroll into or dis-enroll from a course.
It implements 5 security issues from the 
[OWASP 2013 Top 10](https://www.owasp.org/index.php/Top_10_2013-Top_10)
list. The source code is available at github:
[https://github.com/alpako/cybersecuritybase-project](https://github.com/alpako/cybersecuritybase-project).

The [cybersecuritybase-project template](https://github.com/cybersecuritybase/cybersecuritybase-project) 
is used. Hence it can easily be compiled and Run by an IDE or Maven.

instructions to run project from command line:
```
git clone https://github.com/alpako/cybersecuritybase-project
cd cybersecuritybase-project
mvn spring-boot:run
```

Some reproduction steps require 
[OWASP ZAP](https://www.owasp.org/index.php/OWASP_Zed_Attack_Proxy_Project).

This project uses an in memory h2 database which is destroyed when the program is stopped.
The database is on start populated with a few users and courses.
* For _user0_ to _user9_ the initial password is _1234_.
* The _admin_ user has the initial password _super-secret-password_

To make guessing of database ids easier for attackers I purposefully included a
`Hacking Information` section on the homepage of the application.

## Issues

### Issue: A2 Broken Authentication - Password Management

#### Reproduction Steps

1. Start ZAP and set it up as your browser proxy
2. In your web browser go to: `http://localhost:8080`
3. Click _Sign in_ and login as
    * user: user1
    * password: 1234
4. Click _Update Account_
5. Enter new credentials e.g.:
    * password: 0000
    * full name: The First User
    * Address: happy hacking
6. click _Submit_
7. look at the ZAP history and find the last POST request to `http://localhost:8080/user/update`
8. Right click on the request and select _Resend..._ 
9. Change the post parameters to
    * username=admin&password=admin&fullname=Hacked+Admin&status=happy+hacking
10. Click _Send_
11. in your browser click _Sign Out_ and then enter the admin credentials you just set
    * user: admin
    * password: admin
12. click login
13. You successfully signed in as admin

#### Fix 

This issue shows that it is a bad idea to do user information updates
using a posted parameter to identify the user that should be changed.

POST request mapping for `/user/update` can be found in class
`sec.project.UserController.update`. Instead of using a `@RequestParam String
username` to retrieve the username it should use an `Authentication` object
to retrieve the username. 

With an `Authentication` object the attack surface is
reduced from all users to the currently logged in user. It does not protect
from other attacks like CSRF or XSS.



### Issue: A3 XSS - Password Change

#### Reproduction Steps

1. go to `http://localhost:8080/`
2. _Sign in_ as _user6_ with password _1234_
3. Click _Update Account_
4. Enter information:
   * Password: 1234
   * Full Name: My Real Name
   * Status: `A harmless message<script>alert("Hello there. You got hacked.");</script>`
5. click _Submit_
6. click _Sign Out_
7. _Sign in_ as _user7_ with password _1234_
8. click _View all users_
9. an alert message pops up reading "Hello there. You got hacked."

#### Fix

The way the status is shown in the `users.html` page allows the user
to execute any html.
```
<span th:utext="${user.getStatus()}">status</span>
```
Instead of _Thymeleaf_ attribute `th:utext` only use attribute `th:text`.
While the earlier just uses the original text, 
the latter escapes potentially dangerous characters to prevent execution
of unwanted scripts.


### Issue: A8 CSRF - Password Change

#### Reproduction Steps

1. go to `http://localhost:8080/`
2. _Sign in_ as _user2_ with password _1234_
3. open another tab 
4. open `examples/CSRF.html` from the project root in the new tab
5. click _Sign Out_ in the first tab
6. you are no longer able to _Sign in_ with the admin credentials
    * user: admin
    * password: super-secret-password
7. but you can sign in with
    * user: admin
    * password: HelloWorld!

#### Fix

The fix is quite simple. Just remove `http.csrf().disable();`
from the `sec.project.config.SecurityConfiguration.configure` method.
Then rebuild and restart the project. You will then get an error
and the admin password won't change following the reproduction steps.

This fix is only so easy because the used templating framework _Thymeleaf_
automatically adds a CSRF token to every form automatically. Without this feature
it would be quite a tedious and faulty process to add a CSRF token to
every form.



### Issue: A7 Missing FLAC - Withdraw Enrollment

1. go to `http://localhost:8080/`
2. _Sign in_ as _user5_ with password _1234_
3. Look up in the `Hacking Information` section of the page
   a _course id_ (blue) and a corresponding _user id_ (red).
4. then go to URL
`http://localhost:8080/course/disenrollUser/<course id>/<user id>`
5. notice `Hacking Information` section how the user got dis-enrolled (disappeared)
   from the course

### Fix

The request mapping for `/course/disenrollUser/<course id>/<user id>`
is only used on page `http://localhost:8080/admin/course/enrollments`,
which is only accessible by the _admin_ user.
But the request mapping itself still remains accessible to every registered
as it matches the security config for `/course/**`.
This issue can be fixed moving the request mapping to a path 
starting with `/admin/` which is only accessible by the _admin_ user.



### Issue: A5 security configuration - H2 Console Access

#### Reproduction Steps

1. go to `http://localhost:8080/h2-console`
2. login with credentials:
	* JDBC URL: jdbc:h2:mem:test
	* User Name: sa
	* empty password

#### Fix

In `sec.project.config.SecurityConfiguration` remove the _h2-console_
references used to enable the console for development:
* the ant matcher _"/h2-console/\*\*"_ to remove access permissions
* the two lines which made the console accessible in the browser:
```
http.csrf().disable();
http.headers().frameOptions().disable();
```
* remove the following lines from the application properties file:
```
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```
