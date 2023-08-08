# College-Compass
College Compass is a college recommendation Web/Android application for high school students to find their dream college program. College Compass supports over 20 features, including account creation, college browsing, search functionality, and personalized college recommendation based on the user's school interests and academic performance.

- Collected 1000+ college data by web scraping and stored data in a self-designed MySQL database.
- Designed and implemented the user interface through HTML and JavaScript.
- Utilized RESTful API design principles to create a robust and scalable backend architecture
- Deployed the application on AWS and GCP for efficient access. 
- Improved the App performance with Apache load balancer, JDBC Connection Pooling, and MySQL Master-Slave Replication, allowing multiple users to login and complete each action within 200ms.

![Work Flow](https://github.com/yanranw1/College-Compass/assets/83220283/bd83864f-5ff9-484c-888f-df621889b7b6)

 
### Build With
- Java
- JavaScript
- HTML
- MySQL
- Android Studio
- Jakarta Servlet
- REST API
- Maven
- AWS/GCP
 

# Usage
### Login
Users are able to create a new account with an email address or login into their account with username and password at the login page. reCAPTCHA is deployed to protect websites from spam and abuse. All passwords are encrypted first and then stored into our database. 
### Search/Browse
Users are able to search for schools with school name, location name, school genre. Autocomplete and search recommendation is also available.
Users can also choose to browse schools with filters of locations, school genres, and school initials.
### Best Match
At the main page, the user may proceed to a calculate match page, where the user enters personal statistics such as SAT scores, range of tuition cost,preferred major, preferred college location, or preferred college genre, from which the reader can click on "calculate match". The web would ultimately direct the user to a school list with best-matching colleges.
### Dream School List
Each user has a dream school list. Users can add/delete any school from their dream school list.
### Update Information (Employee Only)
Information update page is available for employees of college compass to add new schools in the future without direct access to the database. To access the information update page, users need to pass an employee check with an employee username and password. 
# Contact
- Yanran Wang - yanranw1@uci.edu
