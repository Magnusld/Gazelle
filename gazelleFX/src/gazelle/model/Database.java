package gazelle.model;

import java.util.*;

public class Database {
    public final class Id {
        private long id;
        private Id(long id) {
            this.id = id;
        }

        public long getId() {
            return id;
        }

        public Database getDatabase() {
            return Database.this;
        }
    }

    private class NtoN<T extends DatabaseRow, P extends  DatabaseRow> {
        private HashMap<Long, List<P>> TtoP = new HashMap<>();
        private HashMap<Long, List<T>> PtoT = new HashMap<>();

        public void add(T t, P p) {
            Objects.requireNonNull(t);
            Objects.requireNonNull(p);
            assert(t.getDatabase() == Database.this);
            assert(p.getDatabase() == Database.this);
            long tId = t.getId();
            long pId = p.getId();

            if(!TtoP.containsKey(tId))
                TtoP.put(tId, new ArrayList<>());
            if(!PtoT.containsKey(pId))
                PtoT.put(pId, new ArrayList<>());

            if(TtoP.get(tId).contains(p)) {
                assert(PtoT.get(pId).contains(t));
                return;
            }

            TtoP.get(tId).add(p);
            PtoT.get(pId).add(t);
        }

        public boolean remove(T t, P p) {
            Objects.requireNonNull(t);
            Objects.requireNonNull(p);
            assert(t.getDatabase() == Database.this);
            assert(p.getDatabase() == Database.this);
            long tId = t.getId();
            long pId = p.getId();

            if(!TtoP.containsKey(tId))
                return false;
            if(!PtoT.containsKey(pId))
                return false;

            boolean rmd1 = TtoP.get(tId).remove(p);
            boolean rmd2 = PtoT.get(pId).remove(t);
            assert(rmd1 == rmd2);
            return rmd1;
        }

        public boolean isRelated(T t, P p) {
            Objects.requireNonNull(t);
            Objects.requireNonNull(p);
            assert(t.getDatabase() == Database.this);
            assert(p.getDatabase() == Database.this);
            long tId = t.getId();
            long pId = p.getId();
            if(!TtoP.containsKey(tId))
                return false;
            if(!PtoT.containsKey(pId))
                return false;
            boolean rel1 = TtoP.get(tId).contains(p);
            boolean rel2 = PtoT.get(pId).contains(t);
            assert(rel1 == rel2);
            return rel1;
        }

        public List<P> getFromT(T t) {
            Objects.requireNonNull(t);
            assert(t.getDatabase() == Database.this);
            long tId = t.getId();
            if(!TtoP.containsKey(tId))
                TtoP.put(tId, new ArrayList<>());
            return Collections.unmodifiableList(TtoP.get(tId));
        }

        public List<T> getFromP(P p) {
            Objects.requireNonNull(p);
            assert(p.getDatabase() == Database.this);
            long pId = p.getId();
            if(!PtoT.containsKey(pId))
                PtoT.put(pId, new ArrayList<>());
            return Collections.unmodifiableList(PtoT.get(pId));
        }
    }

    private class Table<T extends DatabaseRow> {
        private HashMap<Long, T> rows = new HashMap<>();

        public void add(T t) {
            assert(t.getDatabase() == Database.this);
            rows.put(t.getId(), t);
        }

        public boolean remove(long id) {
            //TODO: tell relationships about removed item
            return rows.remove(id) != null;
        }

        public T get(long id) {
            return rows.get(id);
        }
    }

    private Table<Course> courses = new Table<>();
    private Table<User> users = new Table<>();

    private NtoN<Course, User> followers = new NtoN<>();
    private NtoN<Course, User> owners = new NtoN<>();

    private long nextId = 1;

    public Database() {
    }

    public Course newCourse(String name) {
        Course course = new Course(getNextId(), name);
        courses.add(course);
        return course;
    }

    public User newUser() {
        User user = new User(getNextId());
        users.add(user);
        return user;
    }

    public void addOwnerToCourse(User user, Course course) {
        owners.add(course, user);
    }

    public boolean removeOwnerOfCourse(User user, Course course) {
        return owners.remove(course, user);
    }

    public List<Course> getCoursesOwned(User owner) {
        return owners.getFromP(owner);
    }

    public List<User> getCourseOwners(Course course) {
        return owners.getFromT(course);
    }

    private Id getNextId() {
        return new Id(nextId++);
    }
}
