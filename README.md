# jenkins-homework2

## Second homework for Jenkins

* ~~Setup system messgae~~
* ~~setup global admin email address~~
* ~~setup smtp server~~
* ~~setup slack~~
* ~~setup github~~
* ~~Create three folders `/folder1`, `/folder1/folder2` and `folder3`~~
* ~~for `folder1` configure your shared library~~
* ~~create credentials `USERNAME` and `PASSWORD`~~
* ~~create group and role `poweruser` and assing it to `folder1`~~
* ~~inside folder3 create test-job with build permissions for `poweruser`~~

### My steps

* created a startup shell script for my Jenkins VM that installs Jenkins and creates a hook script in `init.groovy.d` folder.

* Organized init scripts:
* 000 - installs pre-requisite scripts ( ownsership, role strategy, slack, github)
* 001 - sets message and SMTP (seen above)
* 002 - sets github and slack plugins
* 003 - sets simple credentials from env
* 004 - sets folders1, folder2 and folder3 and sets ownership
* 005 - adds admin and poweruser roles
* Also an environment variables file
