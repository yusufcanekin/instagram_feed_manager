import datastructures.HashTable;
import models.Post;
import models.User;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.util.Scanner;

import static actions.Actions.*;

/**
 * Entry point for the application. Handles reading input commands from a file,
 * performing user and post actions, and writing results to an output file.
 */
public class Main {
    /**
     * Main method for processing user commands from an input file and writing results to an output file.
     *
     * @param args command-line arguments; expects two arguments:
     *             1. The path to the input file containing commands.
     *             2. The path to the output file where results should be written.
     * @throws IOException if there is an issue reading from or writing to files.
     */
    public static void main(String[] args) throws IOException {
        // Open the output file for writing results
        BufferedWriter writer = new BufferedWriter(new FileWriter(args[1]));

        // Open the input file containing user commands
        File actionsFile = new File(args[0]);

        Scanner inputFile = new Scanner(actionsFile);

        // Initialize hash tables for users and posts
        HashTable<String, User> users = new HashTable<>();
        HashTable<String, Post> posts = new HashTable<>();

        // Process each command in the input file
        while (inputFile.hasNextLine()) {
            String line = inputFile.nextLine();
            String[] lineParts = line.split(" ");
            String method = lineParts[0];

            if (method.equals("create_user")) {
                String userID = lineParts[1];
                createUser(users, userID, writer);
            } else if (method.equals("follow_user")) {
                String userID1 = lineParts[1];
                String userID2 = lineParts[2];
                followUser(users, userID1, userID2, writer);
            } else if (method.equals("unfollow_user")) {
                String userID1 = lineParts[1];
                String userID2 = lineParts[2];
                unfollowUser(users, userID1, userID2, writer);
            } else if (method.equals("create_post")) {
                String userID = lineParts[1];
                String postID = lineParts[2];
                String postContent = lineParts[3];
                createPost(users, posts, userID, postID, postContent, writer);
            } else if (method.equals("see_post")) {
                String userID = lineParts[1];
                String postID = lineParts[2];
                seePost(users, posts, userID, postID, writer);
            } else if (method.equals("see_all_posts_from_user")) {
                String viewerId = lineParts[1];
                String viewedId = lineParts[2];
                seeAllPosts(users, viewerId, viewedId, writer);
            } else if (method.equals("toggle_like")) {
                String userID = lineParts[1];
                String postID = lineParts[2];
                toggleLike(users, posts, userID, postID, writer);
            } else if (method.equals("generate_feed")) {
                String userID = lineParts[1];
                int feedSize = Integer.parseInt(lineParts[2]);
                generateFeed(users, userID, feedSize, writer);
            } else if (method.equals("scroll_through_feed")) {
                String userID = lineParts[1];
                scrollThroughFeed(users, userID, lineParts, writer);
            } else if (method.equals("sort_posts")) {
                String userID = lineParts[1];
                sortPosts(users, userID, writer);
            }
        }

        // Close resources after processing
        writer.close();
        inputFile.close();
    }
}
