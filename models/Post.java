package models;

/**
 * Represents a post in the system. A post has an author, content, a unique post ID,
 * and a like count. It supports liking, unliking, and comparison based on likes.
 */
public class Post implements Comparable<Post> {
    private String author; // The author of the post
    private String postID; // Unique identifier for the post
    private String content; // Content of the post
    private int likes; // Number of likes on the post

    /**
     * Constructs a new Post with the specified author, post ID, and content.
     *
     * @param author  the author of the post
     * @param postID  the unique identifier for the post
     * @param content the content of the post
     */
    public Post(String author, String postID, String content) {
        this.author = author;
        this.content = content;
        this.postID = postID;
    }

    /**
     * Increments the like count for the post.
     */
    public void like() {
        this.likes++;
    }

    /**
     * Decrements the like count for the post.
     */
    public void unlike() {
        this.likes--;
    }

    /**
     * Retrieves the unique post ID.
     *
     * @return the post ID
     */
    public String getPostID() {
        return postID;
    }

    /**
     * Retrieves the author of the post.
     *
     * @return the author's name
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Retrieves the number of likes on the post.
     *
     * @return the like count
     */
    public int getLikes() {
        return likes;
    }

    /**
     * Compares this post to another post. Posts are first compared by their like count
     * in ascending order, and if the like counts are equal, they are compared
     * lexicographically by their post IDs.
     *
     * @param o the other post to compare to
     * @return a positive integer if this post has more likes, a negative integer if
     * the other post has more likes, or the result of comparing the post IDs if the like counts are equal
     */
    @Override
    public int compareTo(Post o) {
        // Compare posts by the number of likes
        if (this.likes > o.likes) {
            return 1;
        } else if (this.likes < o.likes) {
            return -1;
        } else {
            // If likes are equal, compare by post IDs lexicographically
            return this.postID.compareTo(o.getPostID());
        }
    }
}
