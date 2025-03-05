# Social Media Simulation

## Project Overview
This project is a simple social media simulation implemented in Java. It allows users to create accounts, follow/unfollow other users, create posts, like/unlike posts, and generate feeds. The system is built using a custom `HashTable` for storing users and posts, and a `MaxHeap` for sorting posts by likes.

## Features
- **User Management:**
  - Create users.
  - Follow/unfollow other users.
- **Post Management:**
  - Create posts.
  - View posts.
  - Like/unlike posts.
  - View all posts of a followed user.
- **Feed Generation:**
  - Generate a feed from followed users' posts.
  - Scroll through the feed.
  - Sort user’s own posts by like count.

## Project Structure
```
📂 src/
 ├── actions/              # Contains all actions that users can perform.
 │   └── Actions.java      # Provides static methods for handling user and post interactions.
 ├── datastructures/       # Custom data structures used in the project.
 │   ├── HashTable.java    # A simple hash table with separate chaining.
 │   └── MaxHeap.java      # A max-heap implementation for sorting posts.
 ├── models/               # Defines core models.
 │   ├── User.java         # Represents a user in the system.
 │   ├── Post.java         # Represents a post.
 ├── Main.java             # Entry point of the application.
```

## Usage
### Compilation
To compile the project, navigate to the `src` directory and run:
```sh
javac -d out *.java actions/*.java datastructures/*.java models/*.java
```

### Running the Program
To run the program, provide an input file with commands:
```sh
java -cp out Main input.txt output.txt
```
- `input.txt`: The file containing commands.
- `output.txt`: The file where results are written.

### Supported Commands
The program reads a text file with commands in the following format:
```
create_user userID
follow_user userID1 userID2
unfollow_user userID1 userID2
create_post userID postID "post content"
see_post userID postID
see_all_posts_from_user viewerID viewedID
toggle_like userID postID
generate_feed userID feedSize
scroll_through_feed userID feedSize command_sequence
sort_posts userID
```

## Example Input
```
create_user Alice
create_user Bob
follow_user Alice Bob
create_post Bob P1 "Hello world!"
see_post Alice P1
toggle_like Alice P1
generate_feed Alice 10
sort_posts Bob
```

## Example Output
```
Created user with Id Alice.
Created user with Id Bob.
Alice followed Bob.
Bob created a post with Id P1.
Alice saw P1.
Alice liked P1.
Feed for Alice:
Post ID: P1, Author: Bob, Likes: 1
Sorting Bob's posts:
P1, Likes: 1
```

## Dependencies
This project does not use external libraries and is implemented using pure Java.

## Authors
- **Developer:** Yusuf Can Ekin



