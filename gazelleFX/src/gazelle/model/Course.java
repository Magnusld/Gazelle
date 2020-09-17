package gazelle.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Course {

    private final String id;
    private String name;
    private List<User> owners = new ArrayList<>();
    private List<User> followers = new ArrayList<>();
    private List<Post> posts = new ArrayList<>();

    public Course(String name, User... owners) {
        this.id = "a";
        this.name = name;
        for (User owner : owners)
            addOwner(owner);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String getId() {
        return this.id;
    }

    public List<User> getOwners() {
        return Collections.unmodifiableList(this.owners);
    }

    public void addOwner(User owner) {
        if (this.owners.contains(owner))
            return;
        this.owners.add(owner);
        owner.addOwnedCourse(this);
    }

    public void removeOwner(User owner) {
        if (this.owners.remove(owner))
            owner.removeOwnedCourse(this);
    }

    public List<User> getFollowers() {
        return Collections.unmodifiableList(this.followers);
    }

    public void addFollower(User follower) {
        if (this.followers.contains(follower))
            return;
        this.followers.add(follower);
        follower.addFollowedCourse(this);
    }

    public void removeFollower(User follower) {
        if (this.followers.remove(follower))
            follower.removeFollowedCourse(this);
    }

    public List<Post> getPosts() {
        return Collections.unmodifiableList(this.posts);
    }

    public void addPost(Post post) {
        if (this.posts.contains(post))
            return;
        this.posts.add(post);
        post.setCourse(this);
    }

    public void removePost(Post post) {
        if (this.posts.remove(post))
            post.setCourse(null);
    }
}
