# Introduction

The purpose of the TwitterCLI application is to use the Command Line Interface (CLI) to run create, read and delete (CRUD, without the U) operations on Twitter objects using the Twitter REST API. By doing so, the user is able to post, show and delete Twitter posts using their Twitter account. During the implementation of this app, I learned about REST APIs, the Data Access Object (DAO) and the Model View Controller (MVC) design patterns, and how to use Spring to manage my project's dependencies.

# Quick Start

Apache Maven is used to build the TwitterCLI app project. Therefore, Maven can be used to package all the class files into a single JAR file. In order to accomplish this, first change to the directory where the POM.xml file exists. Then open the terminal window and run the following command `mvn package`. This command will make a JAR file containing all the project's classes and will be placed in the `target` directory. 
When running the TwitterCLI app there are three usages. Each usage will allow the user to either post, show or delete Twitter posts. The general usage of the application is shown below.

`TwitterCLI post|show|delete [options]`

Details for each usage will be outlined in this section.

1) The first feature described for the TwitterCLI app is `post`, where users can post tweets with the following usage,

`TwitterCLI "post" "tweet_text" "latitude:longitude"`

The "tweet_text" field is the text that the user would like to post, and it must not exceed 140 characters. The "latitude:longitude" field are the geographic coordinates for the location of the post and must be within range. An example of the usage is shown below.

`TwitterCLI "post" "test post" "0:0"`

2) The second feature is `show` and allows the user to look up tweets by their tweet ID and view the details of the tweet in JSON format. Additionally, the user can use the [options] feature to display specific fields from the Tweet object. If no options are selected, then all Tweet object fields are displayed. The usage is as follows,

`TwitterCLI show tweet_id [field1,fields2]`

And an example usage is shown below.

`TwitterCLI show 1097607853932564480 "id,text,retweet_count"`

Finally, the options and their details are listed next.

- `created_at`: The date and time the Tweet object was created
- ` id`: A unique identification number assigned to the Tweet object
- `id_str`: The identification number as a string instance
- `text`: The text/message found in the Tweet object when posted
- `entities`: contains a collection of all the 'hashtags' and 'user_mentions' in the tweet
- `retweet_count`: The number of times the tweet was retweeted
- `favorite_count`: The number of times the tweet was favorited
- `favorited`: Returns true if the tweet has been favorited and false otherwise
- `retweeted`: Returns true if the tweet has been retweeted and false otherwise
 
3) The third feature is `delete` and it allows the user to delete a list of tweets by their ID. The deleted Tweet objects are returned to the user in JSON format. The usage for this feature is,

`TwitterCLI delete [id1,id2,..]`

And an example of the usage is shown below,

`TwitterCLI delete 1200145224103841792`

# Design

The UML diagram describing the TwitterCLI application is shown below.

<p align="center">
<img src="https://github.com/jarviscanada/jarvis_data_eng_JudeFurtal/blob/feature/twitterREADME/core_java/twitter/UML.png">
</p>

The application contains four main components and these components are displayed in the UML diagram. This section will describe each component in detail.

- `app/main`: This component is shown in the UML diagram as the 'TwitterCLIApp' class and it is the highest layer and provides a starting point for the program. It is responsible for directly interacting with the client/end user. The component receives arguments from the user, then parses the arguments and calls the next layer in the application. Furthermore, it declares and instantiates all the other components.
- `controller`: This layer of the application is responsible for consuming the user input arguments and then using the arguments to make the call to the service layer. The controller layer is not responsible for handling any business logic or interacting with the Twitter REST API. 
- `service`: This layer is responsible for handling the business logic of the application. It receives the user arguments from the controller layer and then validates the input. For this application it is checked that the user's text length does not exceed 140 characters, the geographic coordinates are within range and if the provided Tweet object ID is in the correct format.
- `DAO`: The Data Access Object (DAO) layer is responsible for interacting with external storage/services, for this application it is the Twitter REST API. It operates independent of business logic and works directly with the Data Transfer Objects (DTOs)/models. This component provides a layer of abstraction between the application and the external Twitter REST API.

# Spring

Initially all the dependencies were managed using a traditional dependency management system. This was accomplished by using the app/main component to manage all the dependencies via constructors. This method proved to be very tedious, so Spring was used to manage the dependencies. The Spring framework allows for simpler management of dependencies by defining dependencies between classes instead of specifying them through constructors in a main class. Two approaches of Spring were undertaken. The first approach is called the `@Beans` approach. A Bean is responsible for establishing the dependency relationship between classes. Then an Inversion of Control (IoC) is used to instantiate Beans based on the relationships specified. The second approach uses `@ComponentScan`, which takes advantage of the fact that dependencies are specified when interfaces are implemented. This eliminates the manual work done in the `@Beans` method. Once the dependencies have been specified, they are managed by the IoC container.      

# Models

The DAO layer of the application was responsible for interacting with the Data Transfer Objects (DTOs)/models. For the TwitterCLI application there was a total of 5 models used and details for each model is shown below.

- `Coordinates`: This model contains the geographic coordinates (latitude and longitude) of the Tweeter post. The type of coordinates in also specified (ex. point).
- `Entities`: This model contains a list of all the hashtags and user mentions found in the Tweeter post.
- `Hashtag`: This model contains information of the hashtag used such as the text of the hashtag.
- `UserMention`: This model contains information of other Twitter users if they are mentioned in the post. Information includes screen name, tweet ID, and the user's name.
- `Tweet`: This model is the largest and contains all the information that defines a Tweet object.

# Improvements

1) The current Tweet object used in the application contains limited information compared to the original Tweet object defined in the Twitter REST API. An improvement would be to extend our Tweet object to contain all the information that an original Tweet object contains.
2) The second improvement would be to add a Graphical User Interface (GUI). This will allow the application to look like other REST API tools such as Postman.
3) The third improvement would be to add more functionality to the application. Instead of just posting, showing, and deleting posts. Users should have options to archive, filter and scroll through multiple posts.
