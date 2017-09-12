# Lviv

[Online book shops](https://github.com/sekta-java/lviv/blob/master/bookshop_list.md) which should be indexed in the project.


## Project description

Here should be some project description...



## Technical info

### High level diagram

![High level diagram](https://raw.githubusercontent.com/sekta-java/lviv/master/Lvil_highlevel_diagram.png)

#### Application Core

We will use **ElasticSearch** in order to keep indexed data about books in our application. It will be main storage for our data.

**ElasticSearch** and **Main Storage Middleware** are represent core of application.

**Main Storage Middleware** encapsulates details about underlying data storage, I mean **ElasticSearch**. So all services will work with **Main Storage Middleware** by *REST* and will be not aware about actual DB.

It allows us to replace **ElasticSearch** with any another DB(if it be required) without affecting other parts of application.

#### Crawlers
For each book shop which will be indexed by our system we will create own **Crawler**.

Each **Crawler** is responsible for:
- Search for all book page links and store them into local DB - **Spider**;
- Take some link from local DB and parse book data to **Book Model** (it will be described later) - **Parser**;
- Take care about sending this **Book Model** to **Main Storage Middleware**.

We should be able to run few instances of the same **Crawler** simultaneously in order to increase its performance. It means that we take care about some locking on DB level or smth like that.

#### Web

**Frontend** & **Edge Backend** provide access to our system from Internet. There is **MySQL** server that holds user data (User Accounts, Likes/Dislikes, Bought Books, Read Books, Add to Wish List, etc)

PS. Feel free to contribute to this document :)
