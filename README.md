# Bitbucket crawler
Run code on each of your bitbucket repo and collect results as parametrized tests


## How to run
* ```# mvn and git are prerequisites```
* ```$ git clone https://github.com/infobip/bitbucket-crawler.git```
* ```# SCRIPT_TO_RUN=MadgadgetAnalysis```
* ```# CREDENTIALS=username:pass```
* ```$ mvn clean test -Dscript=$SCRIPT_TO_RUN -Dcredentials=$CREDENTIALS ```