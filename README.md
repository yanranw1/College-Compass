# College-Compass
A college recommendation Web/Android application, supporting over 20 features, including account creation, college browsing, search functionality, and personalized college recommendation based on user's school interests and academic performance.

- Collected 1000+ college data by web scraping and stored data in a self-designed MySQL database.
- Designed and implemented the user interface through HTML and JavaScript.
- Utilized RESTful API design principles to create a robust and scalable backend architecture
- Deployed the application on AWS and GCP for efficient access. 
- Improved the App performance with Apache load balancer, JDBC Connection Pooling, and MySQL Master-Slave Replication, allowing multiple users to login and complete each action within 200ms.

![](https://github.com/yanranw1/College-Compass/assets/83220283/2b552865-d30f-43e6-97fa-087fee23d1ac)

# Usage
- Login
Users are able to create a new account with email address or login in to their account with username and password at the login page. reCAPTCHA is deployed to protect websites from spam and abuse
- Search/Browse
User are able to search for schools with school name, location name, school genre. Auto complete and search recommendation is also avaible. 
User can also choose to browse schools with different location, school genre, and school initial. 

Wishlist(Shopping Cart) and Recommendation(Checkout)
Similar to a shopping cart in the Fablix domain, our college recommendation website CollegeConnector(a placeholder name) uses a wishlist to allows the users to choose a college of their own liking. Inside the wishlist, the user has access to the colleges' info such as name, location, and state. The user can add and delete college inside the wishlist. From the wishlist, the user may proceed to a Calculate Match page, where the user enters personal statistics such as SAT scores, range of tuition cost, and school genre, from which the reader can click on "calculate match", which ultimately directs the reader to a page showing a best-matching college from the user's wishlist.

# Contact
- Yanran Wang - yanranw1@uci.edu
