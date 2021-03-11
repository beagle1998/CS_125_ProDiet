# CS_125_Project

## ProDiet

### Project Info
- Project Name: ProDiet
- Group Number: 28
- Group Name: ProDiet

### Team Members
- Tyler Mun: locate & process online data from USDA
- Jing Yang: build up communication between front-end / back-end & ranking system
- Sally Huynh: build up matching system
- Hoijong Kim: design App UI & testing

### Repository Introduction
- ProDiet: source code of Android App
- data_processing/USDA_API: source code to process data from USDA
- README.md: team & project introduction
- rules.json: rules to index online database
- sample.json: small sample of online database

### Android App Introduction
ProDiet is an Android app that gives users food recommendation based on their personal information (height, weight, age, gender, vegan, food preference) and contextual information (activity level, current time), to help users reach ideal weight. This app requires internet connection to communicate with online database. The data source is from USDA website.  
User can login with their username and password. Username is unique for each user, and new users can sign up new accounts by clicking sign up. If users provide any invalid information, for example, a password with space, there will be message shows up and login / sign up will not success.  
Users will enter main page after users sucessfully login. Users can click `ACTIVITY` button and choose their activity level to get food recommendation. The recommendation is given based on their personal information (height, weight, age, gender, vegan, activity level, food preference) and contextual information (current time, food information from USDA).  
Users make choice by clicking a food item. After clicking, a message will pop up and tell users that the food choice has been recorded. History of food choices are treated as users food preference which is an important factor of giving recommendation.  
User can change page by clicking menu on top left.  
Profile page allows users to update some of their personal information, especially changing factor like weight. Information with empty entry box will remain the same.  
Assessment page gives users an assessment of their current weight status.