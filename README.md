# jenkins-homework2

## Second homework for Jenkins

* ~~Setup system messgae~~
* ~~setup global admin email address~~
* setup smtp server
* setup slack
* setup github
* Create three folders `/folder1`, `/folder1/folder2` and `folder3`
* for `folder1` configure your shared library
* create credentials `USERNAME` and `PASSWORD`
* create group and role `poweruser` and assing it to `folder1`
* inside folder3 create test-job with build permissions for `poweruser`

### My steps

* created a startup shell script for my Jenkins VM that installs Jenkins and creates a hook script in `init.groovy.d` folder. First task done; the script changes system message

![img1](img/CleanShot%202021-10-12%20at%2013.48.15@2x.png)

* added changing admin email address to script
* Now i will stop writing in groovy and try to migrate my code to CASC plugin (yaml)
