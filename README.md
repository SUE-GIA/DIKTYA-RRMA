# ***ReadMe - Request-Reply Messaging App***

---

*STEFANOS PANAGIOTIS GIANNAKOS 3568*

---

Server Initialization
*The Server class serves as the entry point. It initializes a server socket and listens for incoming connections on the specified port. Upon client connection, it creates a new MessagingClient instance to handle communication with the client.*

MessagingClient Class
*The MessagingClient class extends the Thread class, enabling concurrent handling of multiple clients. Each instance of MessagingClient manages the communication with a single client.*

Client Request Handling
*Client requests are processed within the run() method of MessagingClient. The client sends requests in the form of strings with specific formats, and these requests are split and interpreted. Depending on the function identifier (FN_ID) in the request, different functions are called to handle specific actions.*

Multithreading
*Multithreading is employed to handle multiple clients concurrently. Each client connection is managed by a separate thread, allowing for efficient handling of simultaneous client interactions while avoiding conflicts through the use of locks.*

Key Functions
handleFunction1: 
*Handles user registration. It checks if a username is valid, not already taken, and then creates a new user account.*

handleFunction2:
*Retrieves a list of usernames of all registered users when provided with a valid authentication token.*

handleFunction3:
*Facilitates message sending between users. It ensures that both sender and recipient exist and adds the message to the recipient's inbox.*

handleFunction4:
*Retrieves a user's messages when provided with a valid authentication token.*

handleFunction5:
*Retrieves and marks a specific message as read when provided with a valid authentication token and message ID.*

handleFunction6:
*Deletes a specific message when provided with a valid authentication token and message ID.*


Thread Safety
*Thread safety is ensured by using a ReentrantLock to protect shared data structures such as the list of user accounts. This prevents data corruption when multiple threads attempt to access or modify these structures concurrently.*
***
Client:
*The client accepts command-line arguments for specifying the hostname, port, function identifier (FN_ID), and function-specific arguments. It connects to the server, sends requests, and receives responses.*
Key Functionality:
*Command-Line Parameters: The client expects the user to provide the hostname, port number, FN_ID (function identifier), and function-specific arguments as command-line parameters.*

*Socket Communication: It establishes a socket connection to the specified server and communicates with it through input and output streams.*

*Request Handling: The code constructs requests based on the provided FN_ID and arguments. It sends the request to the server in a specific format (using "~" as a delimiter) and reads the server's response.*

*Error Handling: The client handles exceptions related to socket operations, such as UnknownHostException, EOFException, and IOException.*

---
Account:
*The Account class represents user accounts in the messaging system. It stores the username, authentication token, and the user's message box.*

*Constructor: Creates an account with a username and authentication token.*

*getUsername(): Returns the username.*

*getAuthToken(): Returns the authentication token.*

*getMessageBox(): Returns a copy of the user's message box.*

*addMessage(sender, receiver, body, id): Adds a new message to the user's message box with sender, receiver, body, and ID.*

*deleteMessage(messageID): Deletes a message from the message box based on its ID.*

*This class is essential for managing user data and their messages within the messaging system.*

---

Messages:
*The Message class represents individual messages within the messaging system:*

*Constructor: Creates a message with sender, receiver, body, and an ID, initially marked as unread.*

*isRead(): Checks if the message has been read.*

*setRead(boolean read): Sets the message as read or unread.*

*getId(): Retrieves the message's unique ID.*

*getSender(): Returns the sender's name.*

*getBody(): Retrieves the message's content.*

*This class encapsulates message properties and is used for message handling and tracking within the messaging system.*

---
---
 ***HOW TO RUN:***
***

***Server:***

```java Server < port number >```

***
***Client:***

```java Client < ip > < port number > < fn_id > < args >```

***
***Functionality 1: User Registration:***

Register a new user:
```java Client <hostname> <port> 1 <username>```
***

***Functionality 2: Get Usernames:***

Retrieve a list of all registered usernames:
```java Client <hostname> <port> 2 <authToken>```

***
***Functionality 3: Send Message:***

Send a message to a user:
```java Client <hostname> <port> 3 <authToken> <recipient> "<message>"```

***
***Functionality 4: Get User Messages:***

Retrieve messages for a user:
```java Client <hostname> <port> 4 <authToken>```

***
***Functionality 5: Read Message:***

Read a specific message:
```java Client <hostname> <port> 5 <authToken> <messageID>```

***
***Functionality 6: Delete Message:***

Delete a specific message:
```java Client <hostname> <port> 6 <authToken> <messageID>```
***
Replace 
```<hostname>, <port>, <authToken>, <username>, <recipient>, <message>, and <messageID>```
with the appropriate values for your use case.
