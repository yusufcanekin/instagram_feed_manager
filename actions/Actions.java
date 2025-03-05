package actions;

import datastructures.HashTable;
import models.Post;
import models.User;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Provides static methods for performing user and post-related actions.
 */
public class Actions {

    /**
     * Creates a new user and adds it to the users hash table.
     *
     * @param users   the hash table of users
     * @param userID  the unique identifier for the new user
     * @param writer  the writer used for logging output
     * @throws IOException if an I/O error occurs
     */
    public static void createUser(HashTable<String, User> users, String userID, BufferedWriter writer) throws IOException {
        User user = new User(userID);

        // Add the user to the hash table
        if (!users.put(userID, user)) {
            // Error if the user already exists
            writer.write("Some error occurred in create_user.\n");
        } else {
            writer.write(String.format("Created user with Id %s.\n", userID));
        }
    }

    /**
     * Makes one user follow another user.
     *
     * @param users   the hash table of users
     * @param userID1 the ID of the user who will follow
     * @param userID2 the ID of the user to be followed
     * @param writer  the writer used for logging output
     * @throws IOException if an I/O error occurs
     */
    public static void followUser(HashTable<String, User> users, String userID1, String userID2, BufferedWriter writer) throws IOException {
        // Check if a user is trying to follow himself
        if (userID1.equals(userID2)) {
            writer.write("Some error occurred in follow_user.\n");
            return;
        }

        User followerUser = users.get(userID1);
        if (followerUser == null) {
            // Error if the user doesn't exist
            writer.write("Some error occurred in follow_user.\n");
            return;
        }

        User followedUser = users.get(userID2);
        if (followedUser == null) {
            writer.write("Some error occurred in follow_user.\n");
            return;
        }

        // Attempt to follow the user
        if (!followerUser.follow(followedUser)) {
            // Error if the user is already followed
            writer.write("Some error occurred in follow_user.\n");
        } else {
            writer.write(String.format("%s followed %s.\n", userID1, userID2));
        }
    }

    /**
     * Makes one user unfollow another user.
     *
     * @param users   the hash table of users
     * @param userID1 the ID of the user who will unfollow
     * @param userID2 the ID of the user to be unfollowed
     * @param writer  the writer used for logging output
     * @throws IOException if an I/O error occurs
     */
    public static void unfollowUser(HashTable<String, User> users, String userID1, String userID2, BufferedWriter writer) throws IOException {
        // Check if a user is trying to unfollow himself
        if (userID1.equals(userID2)) {
            writer.write("Some error occurred in unfollow_user.\n");
            return;
        }

        User followingUser = users.get(userID1);
        if (followingUser == null) {
            writer.write("Some error occurred in unfollow_user.\n");
            return;
        }

        User followedUser = users.get(userID2);
        if (followedUser == null) {
            writer.write("Some error occurred in unfollow_user.\n");
            return;
        }

        if (!followingUser.unfollow(followedUser)) {
            // Error if the user is not already followed
            writer.write("Some error occurred in unfollow_user.\n");
        } else {
            writer.write(String.format("%s unfollowed %s.\n", userID1, userID2));
        }
    }

    /**
     * Creates a new post and associates it with the user.
     *
     * @param users       the hash table of users
     * @param posts       the hash table of posts
     * @param userID      the ID of the user creating the post
     * @param postID      the unique identifier for the post
     * @param postContent the content of the post
     * @param writer      the writer used for logging output
     * @throws IOException if an I/O error occurs
     */
    public static void createPost(HashTable<String, User> users, HashTable<String, Post> posts, String userID, String postID, String postContent, BufferedWriter writer) throws IOException {
        User user = users.get(userID);

        Post post = new Post(userID, postID, postContent);

        // Add the post to the hash table and associate it with the user
        if (user != null && posts.put(postID, post)) {
            user.createPost(post, writer);
        } else {
            // Error if the user doesn't exist or the post ID already exists
            writer.write("Some error occurred in create_post.\n");
        }
    }

    /**
     * Marks a post as seen by the user.
     *
     * @param users   the hash table of users
     * @param posts   the hash table of posts
     * @param userID  the ID of the user viewing the post
     * @param postID  the ID of the post being viewed
     * @param writer  the writer used for logging output
     * @throws IOException if an I/O error occurs
     */
    public static void seePost(HashTable<String, User> users, HashTable<String, Post> posts, String userID, String postID, BufferedWriter writer) throws IOException {
        User user = users.get(userID);

        Post post = posts.get(postID);

        // Mark the post as seen if both the user and post exist
        if (user != null && post != null) {
            user.seePost(post);
            writer.write(String.format("%s saw %s.\n", userID, post.getPostID()));
        } else {
            // Error if the user or post doesn't exist
            writer.write("Some error occurred in see_post.\n");
        }
    }

    /**
     * Marks all posts of one user as seen by another user.
     *
     * @param users     the hash table of users
     * @param viewerID  the ID of the user viewing the posts
     * @param viewedID  the ID of the user whose posts are being viewed
     * @param writer    the writer used for logging output
     * @throws IOException if an I/O error occurs
     */
    public static void seeAllPosts(HashTable<String, User> users, String viewerID, String viewedID, BufferedWriter writer) throws IOException {
        User viewerUser = users.get(viewerID);

        if (viewerUser == null) {
            writer.write("Some error occurred in see_all_posts_from_user.\n");
            return;
        }

        User viewedUser = users.get(viewedID);
        if (viewedUser == null) {
            writer.write("Some error occurred in see_all_posts_from_user.\n");
            return;
        }

        // Retrieve the posts of the viewed user and mark them as seen by the viewer
        ArrayList<Post> posts = viewedUser.getPosts();
        viewerUser.seeAllPosts(posts);

        writer.write(String.format("%s saw all posts of %s.\n", viewerID, viewedID));
    }

    /**
     * Toggles the like status of a post for a user.
     *
     * @param users   the hash table of users
     * @param posts   the hash table of posts
     * @param userID  the ID of the user liking or unliking the post
     * @param postID  the ID of the post being liked or unliked
     * @param writer  the writer used for logging output
     * @throws IOException if an I/O error occurs
     */
    public static void toggleLike(HashTable<String, User> users, HashTable<String, Post> posts, String userID, String postID, BufferedWriter writer) throws IOException {
        User user = users.get(userID);

        Post post = posts.get(postID);

        // Toggle the like status if both the user and post exist
        if (user != null && post != null) {
            user.likePost(post, writer);
        } else {
            // Error if the user or post doesn't exist
            writer.write("Some error occurred in toggle_like.\n");
        }
    }

    /**
     * Generates a feed of posts for a user.
     *
     * @param users    the hash table of users
     * @param userID   the ID of the user for whom the feed is generated
     * @param feedSize the maximum number of posts to include in the feed
     * @param writer   the writer used for logging output
     * @throws IOException if an I/O error occurs
     */
    public static void generateFeed(HashTable<String, User> users, String userID, int feedSize, BufferedWriter writer) throws IOException {
        User user = users.get(userID);

        if (user == null) {
            // Error if the user doesn't exist
            writer.write("Some error occurred in generate_feed.\n");
            return;
        }

        user.generateFeed(feedSize, writer);
    }

    /**
     * Simulates scrolling through a feed for a user.
     *
     * @param users         the hash table of users
     * @param userID        the ID of the user scrolling through the feed
     * @param postCommands  an array of commands representing user actions on posts
     * @param writer        the writer used for logging output
     * @throws IOException if an I/O error occurs
     */
    public static void scrollThroughFeed(HashTable<String, User> users, String userID, String[] postCommands, BufferedWriter writer) throws IOException {
        User user = users.get(userID);

        if (user == null) {
            // Error if the user doesn't exist
            writer.write("Some error occurred in scroll_through_feed.\n");
            return;
        }

        user.scrollThroughFeed(postCommands, writer);
    }

    /**
     * Sorts the posts of a user by likes in descending order.
     *
     * @param users   the hash table of users
     * @param userID  the ID of the user whose posts are being sorted
     * @param writer  the writer used for logging output
     * @throws IOException if an I/O error occurs
     */
    public static void sortPosts(HashTable<String, User> users, String userID, BufferedWriter writer) throws IOException {
        User user = users.get(userID);

        if (user == null) {
            // Error if the user doesn't exist
            writer.write("Some error occurred in sort_posts.\n");
            return;
        }

        user.sortPosts(writer);
    }
}
