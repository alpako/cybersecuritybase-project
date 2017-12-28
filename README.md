# Cybersecuritybase-Project

This project is developed for the [Cyber Security Base](https://cybersecuritybase.github.io/) 
course. The idea of this project is to be a course signup page with simple
user registration and role management. It implements 5 security issues from the 
[OWASP 2013 Top 10](https://www.owasp.org/index.php/Top_10_2013-Top_10)
list. The source code is available at gihub:
[https://github.com/alpako/cybersecuritybase-project](https://github.com/alpako/cybersecuritybase-project).

The [cybersceuritybase-project template](https://github.com/cybersecuritybase/cybersecuritybase-project) 
is used. Hence it can easily be compiled and Run by an IDE or Maven.

instructions to run project from commandline:
```
git clone https://github.com/alpako/cybersecuritybase-project
cd cybersecuritybase-project
mvn spring-boot:run
```

Some steps require 
[OWASP ZAP](https://www.owasp.org/index.php/OWASP_Zed_Attack_Proxy_Project).

## Issues

### Issue: A2 Broken Authentication - Password Management

#### Reproduction Steps

1. Start ZAP and set it up as your browser proxy
2. In your web browser go to: `http://localhost:8080`
3. Click _Sign in_ and login as
    * user: user1
    * password: 1234
4. Click _Update Account_
5. Enter new credentials eg:
    * password: 0000
    * full name: The First User
    * Address: happy hacking
6. click _Submit_
7. look at the ZAP hisory and find the last POST request to `http://localhost:8080/user/update`
8. Right click on the request and select _Resend..._ 
9. Change the post parameters to
    * username=admin&password=admin&fullname=Hacked+Admin&status=happy+hacking
10. Click _Send_
11. in your browser click _Sign Out_ and then enter the admin credentials you just set
    * user: admin
    * password: admin
12. click login
13. You sucessfully signed in as admin

#### Fix 

POST request mapping for `/user/update` can be found in class
`sec.project.UserController.update`. Instead of using a `@RequestParam String
username` to retrieve the username it should use an `Authentication` object
to retrieve the username.



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
Instead of thymeleaf attribute `th:utext` only use attribute `th:text`.
While the earlier just uses the original text, 
the latter escapes potenitally dangerous charaters to prevent XSS attacks.


### Issue: A8 CSRF - Password Change

#### Reproduction Steps

TODO

#### Fix

TODO

### Issue: A7 Missing FLAC - Withdraw Enrollment

1. go to `http://localhost:8080/`
2. _Sign in_ as _user5_ with password _1234_
3. Look up in the `Hacking Information` section of the page
   a _course id_ (blue) and a corresponding _user id_ (red).
4. then go to url 
`http://localhost:8080/course/disenrollUser/<course id>/<user id>`
5. notice `Hacking Information` section how the user got disenrolled (disappeared)
   from the course

### Fix

TODO



### Issue: A5 security configuration - H2 Console Access

The request mapping for `/course/disenrollUser/<course id>/<user id>`
is only used on page `http://localhost:8080/admin/course/enrollments`,
which is only accessible by the _admin_ user.
But the request mapping itself still remains accessible to every regisered
as it matches the security config for `/course/**`.
This issue can be fixed moving the requestmapping to the path 
starting with `/admin/` which is only accessible by the _admin_ user.


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