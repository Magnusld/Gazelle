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
        this.posts.add(post);
        post.setCourse(this);
    }

    public void removeOwner(User owner){
        if(owner.removeCourse(this))
            this.owners.remove(owner);
    }

    public void removeFollower(User follower){
        if(follower.removeCourse(this))
            this.followers.remove(follower);
    }

    public void removePost(Post post){
        this.posts.remove(post);
    }

    public String getId(){
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
