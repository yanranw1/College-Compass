# College-Compass
A college recommendation Web/Android application, supporting over 20 features, including account creation, college browsing, search functionality, and personalized college recommendation based on user's school interests and academic performance.

- Collected 1000+ college data by web scraping and stored data in a self-designed MySQL database.
- Designed and implemented the user interface through HTML and JavaScript.
- Utilized RESTful API design principles to create a robust and scalable backend architecture
- Deployed the application on AWS and GCP for efficient access. 
- Improved the App performance with Apache load balancer, JDBC Connection Pooling, and MySQL Master-Slave Replication, allowing multiple users to login and complete each action within 200ms.

![](https://github.com/yanranw1/College-Compass/assets/83220283/2b552865-d30f-43e6-97fa-087fee23d1ac)

 

 

# Usage
### Login
Users are able to create a new account with email address or login in to their account with username and password at the login page. reCAPTCHA is deployed to protect websites from spam and abuse. All password are entrypted first and then stored in our database. 
### Search/Browse
- User are able to search for schools with school name, location name, school genre. Auto complete and search recommendation is also avaible.
- User can also choose to browse schools with different location, school genre, and school initial.
### Best Match
At the main page, the user may proceed to a Calculate Match page, where the user enters personal statistics such as SAT scores, range of tuition cost,preferred major, preferred college location or preferred college genre, from which the reader can click on "calculate match", which ultimately directs the user to a school lists with best-matching colleges.
### Dream School List
Each user has a dream school list. Users can add/delete any school from dream school list.
### Update Information (Employee Only)
Information update page is avaible for employees of college compass to add new schools in the futrue without direct access to the database. To access to the Information update page, user need to pass employee check with an employee user name and password. 

# Contact
- Yanran Wang - yanranw1@uci.edu
