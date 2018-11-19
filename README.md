# Description
This project is the simple most possible implementation of `Authentication` in `Play Framework 2.6` using `Deadbolt2` and `SQLite`.

You can easily change database source to any of your choice by modifying `conf/application.conf` file.

This project has the following features implemented:
1. Login
2. Sign Up
3. Forget Password / Reset Password
4. Logging (Minimal, just to get you started.)
5. Email Service (Email Verification)
6. Restricted Action / View

## RUN INSTRUCTIONS

### Prerequisites

#### SQLite Database

First, we need to create a database

	$ cd project-directory/
	$ touch users.sqlite


### How to run?
1. Move into project directory
	```
	$ cd play-authentication-starter
	```
2. Run through script
	```
	$ ./scripts/run
	```
3. Go to [http://localhost:2020/](http://localhost:2020/)

### Extra Notes
1. By default, the project is using `mock` mail service with output to console, if you want to test with proper email do the following.
    1. Open `play-authentication-starter/conf/application.conf`
	2. In section

		```
		play.mailer {
		  host = smtp.gmail.com
		  port = 465
		  ssl = true
		  user = "" # enter email like "xyz@gmail.com"
		  password = "" # enter password
		  from = "" # enter email like "xyz@gmail.com"
		  mock = true
		}
		```

	Change blank `user`, `from` and `password` values to actual `user`, `from` and `password` values.
	> If you are using gmail, you will have to change a security setting to `Allow Unsecure Apps to Access` to be able to use it.

#### Credits
- [Fahad Siddiqui](https://www.linkedin.com/in/fsdqui/), [Saad Ali](https://www.linkedin.com/in/saad-ali-33814b124/) at [Datum Brain](https://www.datumbrain.com/)
