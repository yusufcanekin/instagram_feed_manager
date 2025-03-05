package models;

import datastructures.HashTable;
import datastructures.MaxHeap;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Represents a user in the system who can create posts, follow other users,
 * interact with posts, and manage his feed.
 */
public class User {
    private String userId; // Unique identifier for the user
    ArrayList<User> followedUsersList; // List of users this user follows
    ArrayList<Post> posts; // List of posts created by this user
    HashTable<String, Post> postsTable; // Hash table for quick access to posts by ID
    HashTable<String, User> followedUsers; // Hash table of followed users
    HashTable<String, Post> seenPosts; // Hash table of posts seen by this user
    HashTable<String, Post> likedPosts; // Hash table of posts liked by this user

    /**
     * Constructs a new User with the specified user ID.
     *
     * @param userID the unique identifier of the user
     */
    public User(String userID) {
        this.userId = userID;
        this.posts = new ArrayList<>();
        this.postsTable = new HashTable<>();
        this.followedUsersList = new ArrayList<>();
        this.followedUsers = new HashTable<>();
        this.seenPosts = new HashTable<>();
        this.likedPosts = new HashTable<>();
    }

    /**
     * Follows another user.
     *
     * @param user the user to follow
     * @return {@code true} if the user was successfully followed, {@code false} otherwise
     */
    public boolean follow(User user) {
        // Add the user to the followed hash table and list if not already followed
        if (followedUsers.put(user.getUserId(), user)) {
            followedUsersList.add(user);
            return true; // Successfully followed
        }
        return false; // Already following
    }

    /**
     * Unfollows a user.
     *
     * @param user the user to unfollow
     * @return {@code true} if the user was successfully unfollowed, {@code false} otherwise
     */
    public boolean unfollow(User user) {
        // Remove the user from the followed hash table and list if present
        if (followedUsers.remove(user.getUserId())) {
            followedUsersList.remove(user);
            return true; // Successfully unfollowed
        }
        return false; // User was not followed
    }

    /**
     * Creates a new post and writes the expected output to a file.
     *
     * @param post   the post to create
     * @param writer the writer used for logging output
     * @throws IOException if an I/O error occurs
     */
    public void createPost(Post post, BufferedWriter writer) throws IOException {
        // Check if the post ID already exists in the hash table via trying to insert the post to the hash table
        boolean isAlreadyExisting = !postsTable.put(post.getPostID(), post);
        if (isAlreadyExisting) {
            writer.write("Some error occurred in create_post.\n"); // Log error if post already exists
        } else {
            posts.add(post);
            writer.write(String.format("%s created a post with Id %s.\n", this.userId, post.getPostID()));
        }
    }

    /**
     * Marks a post as seen by the user.
     *
     * @param post the post to mark as seen
     */
    public void seePost(Post post) {
        seenPosts.put(post.getPostID(), post);
    }

    /**
     * Likes or unlikes a post and writes the action to a file.
     *
     * @param post   the post to like or unlike
     * @param writer the writer used for logging output
     * @throws IOException if an I/O error occurs
     */
    public void likePost(Post post, BufferedWriter writer) throws IOException {
        // Check if the post is already liked
        if (this.likedPosts.put(post.getPostID(), post)) {
            post.like();
            this.seePost(post); // Mark the post as seen
            writer.write(String.format("%s liked %s.\n", this.userId, post.getPostID()));
        } else {
            post.unlike();
            this.likedPosts.remove(post.getPostID());
            writer.write(String.format("%s unliked %s.\n", this.userId, post.getPostID()));
        }
    }

    /**
     * Marks all posts of another user's as seen by the user.
     *
     * @param posts the list of posts to mark as seen
     */
    public void seeAllPosts(ArrayList<Post> posts) {
        // Add each post to the seen posts hash table
        for (Post post : posts) {
            seenPosts.put(post.getPostID(), post);
        }
    }

    /**
     * Generates a feed of posts from followed users in descending order comparing posts' likes and writes it to a file.
     *
     * @param feedSize the maximum number of posts to include in the feed
     * @param writer   the writer used for logging output
     * @throws IOException if an I/O error occurs
     */
    public void generateFeed(int feedSize, BufferedWriter writer) throws IOException {
        MaxHeap<Post> posts = new MaxHeap<>(10); // MaxHeap to sort posts by likes
        for (User user : this.followedUsersList) {
            for (Post post : user.getPosts()) {
                if (!seenPosts.containsKey(post.getPostID())) {
                    posts.insert(post); // Add unseen posts to the heap
                }
            }
        }
        writer.write(String.format("Feed for %s:\n", this.userId));
        while (feedSize > 0 && !posts.isEmpty()) {
            Post post = posts.deleteMax(); // Get the next post with the highest likes
            writer.write(String.format("Post ID: %s, Author: %s, Likes: %s\n", post.getPostID(), post.getAuthor(), post.getLikes()));
            feedSize--;
        }
        if (feedSize != 0) {
            writer.write(String.format("No more posts available for %s.\n", this.userId)); // Log if fewer posts are available
        }
    }

    /**
     * Simulates scrolling through a feed and processes user actions.
     *
     * @param postCommands an array of commands representing user actions on posts
     * @param writer       the writer used for logging output
     * @throws IOException if an I/O error occurs
     */
    public void scrollThroughFeed(String[] postCommands, BufferedWriter writer) throws IOException {
        MaxHeap<Post> posts = new MaxHeap<>(10); // MaxHeap for unseen posts
        for (User user : this.followedUsersList) {
            for (Post post : user.getPosts()) {
                if (!seenPosts.containsKey(post.getPostID())) {
                    posts.insert(post); // Add unseen posts to the heap
                }
            }
        }
        writer.write(String.format("%s is scrolling through feed:\n", this.userId));
        int scrolledPostCount = 0;
        for (int i = 3; i < postCommands.length && !posts.isEmpty(); i++) {
            Post post = posts.deleteMax(); // Get the next post with the highest likes
            scrolledPostCount++;
            if (postCommands[i].equals("0")) {
                writer.write(String.format("%s saw %s while scrolling.\n", this.userId, post.getPostID()));
                seenPosts.put(post.getPostID(), post); // Mark as seen
            } else {
                writer.write(String.format("%s saw %s while scrolling and clicked the like button.\n", this.userId, post.getPostID()));
                seenPosts.put(post.getPostID(), post);
                likedPosts.put(post.getPostID(), post);
                post.like();
            }
        }
        if (scrolledPostCount != Integer.parseInt(postCommands[2])) {
            writer.write("No more posts in feed.\n"); // Log if fewer posts are available
        }
    }

    /**
     * Sorts the user's posts by likes in descending order and writes the sorted posts to a file.
     *
     * @param writer the writer used for logging output
     * @throws IOException if an I/O error occurs
     */
    public void sortPosts(BufferedWriter writer) throws IOException {
        writer.write(String.format("Sorting %s's posts:\n", this.userId));
        MaxHeap<Post> postsHeap = new MaxHeap<>(10); // MaxHeap for sorting
        if (this.posts.isEmpty()) {
            writer.write(String.format("No posts from %s.\n", this.userId)); // Log if no posts exist
            return;
        }
        for (Post post : this.posts) {
            postsHeap.insert(post); // Add all posts to the heap
        }
        while (!postsHeap.isEmpty()) {
            Post post = postsHeap.deleteMax(); // Get the post with the highest likes
            writer.write(String.format("%s, Likes: %s\n", post.getPostID(), post.getLikes()));
        }
    }

    /**
     * Retrieves the list of posts created by the user.
     *
     * @return the list of posts created by the user
     */
    public ArrayList<Post> getPosts() {
        return this.posts;
    }

    /**
     * Retrieves the user's unique ID.
     *
     * @return the user's ID
     */
    public String getUserId() {
        return userId;
    }
}
