# CS_125_Project

## ProDiet

### Project Info
- Project Name: ProDiet
- Group Number: 28
- Group Name: ProDiet

### Team Members
- Tyler Mun: locate & process online data from USDA
- Hoijong Kim: design App UI & testing
- Sally Huynh: build up matching system
- Jing Yang: build up communication between front-end / back-end & ranking system

### Repository Introduction
- ProDiet: source code of Android App
- data_processing/USDA_API: source code to process data from USDA
- README.md: team & project introduction
- rules.json: rules to index online database
- sample.json: a small sample of online database

### Android App Introduction
ProDiet is an Android app that gives users food recommendation based on their personal information (height, weight, age, gender, vegan, activity level, food preference) and contextual information (current time, food information from online database), to help users reach ideal weight. This app requires internet connection to communicate with online database. The data source is from USDA website.  
User can login with their username and password. Username is unique for each user, and new users can sign up new accounts by clicking sign up. If users provide any invalid information, for example, a password with space, there will be message shows up and login / sign up will not success.  
Users will enter main page after users sucessfully login. Users can click `ACTIVITY` button and choose their activity level to get food recommendation. The recommendation is given based on their personal information (height, weight, age, gender, vegan, activity level, food preference) and contextual information (current time, food information from USDA). Users make choice by clicking a food item. After clicking, a message will pop up and tell users that the food choice has been recorded. Users can select food items by clicking them, messages will pop up and tell users that their food choices have been recorded. The number of selection times is treated as degree of preference, and will be stored in the online database for the next ranking process.  
User can change page by clicking menu on top left corner.  
Profile page allows users to view and update some of their personal information, especially changing factor like weight. Invalid information will not be updated.  
Assessment page gives users an assessment of their food preferences and BMI (body mass index) status. The pie chart shows current user's top 5 favorite food items. If the current user hasn't chosen any food items yet, pie chart will not show up, and instead a message will show up and tell the user that he or she does not have any food history yet. The horizontal bar chart can help users visualize their BMI status.  