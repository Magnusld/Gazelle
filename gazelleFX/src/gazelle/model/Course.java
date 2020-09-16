package gazelle.model;

import java.util.ArrayList;
import java.util.List;

public class Course {

    private final String id;
    private String name;
    private List<User> owners = new ArrayList<>();
    private List<User> followers = new ArrayList<>();
    private List<Post> posts = new ArrayList<>();

    public Course(String name, User owner) {
        this.id = "a";
        this.name = name;
        this.owners.add(owner);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addOwner(User owner) {
        if(this.owners.contains(owner))
            return;
        this.owners.add(owner);
        owner.addCourse(this);
    }

    public void addFollower(User follower) {
        if(this.followers.contains(follower))
            return;
        this.followers.add(follower);
        follower.addCourse(this);
    }

    public void addPost(Post post) {
        if(this.posts.contains(post))
            return;
        this.posts.add(post);
        post.setCourse(this);
    }

    public void removeOwner(User owner) {
        if(!this.followers.contains(owner))
            return;
        this.followers.remove(owner);
        owner.removeCourse(this);
    }

    public void removeFollower(User follower) {
        if(!this.followers.contains(follower))
            return;
        this.followers.remove(follower);
        follower.removeCourse(this);
    }

    public void removePost(Post post) {
        if(this.posts.remove(post))
            post.setCourse(null);
    }

    public String getId() {
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public List<User> getOwners(){
        return this.owners;
    }

    public List<User> getFollowers(){
        return this.followers;
    }

    public List<Post> getPosts(){
        return this.posts;
    }
}
