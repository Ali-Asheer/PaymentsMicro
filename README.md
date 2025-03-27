## Rest CRUD med JPA

 I have build an API for a service that functions as a forum. It allows users to connect to a permanent chat channel where participants can post comments.
 
REST API endpoints:
-  [GET]  - /channels/ <=> Retrieves a list of advertised channels.
-  [POST] - /channels/ <=> creates a new advertised channel. It needs a body like this: 
```
{"Channel name": "Name of the channel"}
```
-  [DELETE] - /channels/{id} <=> Delete a advertised channel by a channel ID
-  [PUT] /channels/{id} <=> Creates a new message in advertised channel by a channel ID. It needs a body like this:
```
{"Message content": "Content of the message"}
```
-  [GET] - /channels/{id} <=> Retrieves a list of messages in the advertised channels by a channel ID.
-  [PUT] /messages/{id} <=> Updates a message by a message ID. It needs a body like this:
```
{"Message content": "New content of the message to update"}
```
-  [DELETE] - /messages/{id} => Delete a message by a message ID.

Note: I have filled in the two tables with some Data, so you can run the program directly without having to enter any Data.
