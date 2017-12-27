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

### Issue: A3 XSS - Password Change

### Issue: A8 CSRF - Password Change

### Issue: A7 Missing FLAC - Withdraw Enrollment

### Issue: A5 security configuration - H2 Console Access

## How to Fix the Flaws

### Fix: A2 Broken Authentication - Password Management

### Fix: A3 XSS - Password Change

### Fix: A8 CSRF - Password Change

### Fix: A7 Missing FLAC - Withdraw Enrollment

### Fix: A5 security configuration - H2 Console Access

## outline
* Registration Page for students
  * A2 overwrite security credentials
* Login page
* change password page
  * A3
  * A8
* Homepage
* Course Selection page (signup & drop out)
* Course participants page (remove participant)
  * A7 remove without permission
* h2 console available
  * security misconfiguration
  * A5

## Issue Template
Issue: SQL Injection
Steps to reproduce:
1. Open Injection Flaws
2. Select Numeric SQL Injection
3. Open Developer Console
4. Inspect the Weather Station Element
5. In the Developer Console, find the select element that
   lists the weather stations.
6. Edit one of the option elements within the select element and
   change the option value to "101 OR station < 9999999".
7. Select the altered option from the dropdown list on the page
8. Press Go!
9. You can now see all weather the weather data.


