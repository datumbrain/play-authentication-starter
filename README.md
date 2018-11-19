
## RUN INSTRUCTIONS
### Prerequisites
1. SQLite Database
    First. we need to create a database
	```
		$ cd project-directory/
		$ touch users.sqlite
	```

### How to run?
1. Move into project directory
	```
    $ cd project-directory
	```
2. Run through `sbt`
	```
	$ sbt -Dhttp.port=2020 compile run
	```
3. Go to this [link](http://localhost:2020/)  to test.
	>http://localhost:2020/

### Extra Notes
1. By default, the project is using `mock` mail service with output to console, if you want to test with proper email do the following.
    1. Open `play_skeleton/conf/application.conf`
	2. In section

      ```
	     play.mailer {
			  host = smtp.gmail.com
			  port = 465
			  ssl = true
			  user = "" //enter email like "xyz@gmail.com"
			  password = "" //enter password
			  from = "" //enter email like "xyz@gmail.com"
			  mock = true
		}
    ```
	Change blank `user`, `from` and `password` values to actual `user`, `from` and `password` values.
	>If you are using gmail, you will have to change a security setting to `Allow Unsecure Apps to Access` to be able to use it.
