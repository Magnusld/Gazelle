package gazelle.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
            assert(t.getDatabase() == Database.this);
            long tId = t.getId();
            if(!TtoP.containsKey(tId))
                TtoP.put(tId, new ArrayList<>());
            return TtoP.get(tId);
        }

        public List<T> getFromP(P p) {
            assert(p.getDatabase() == Database.this);
            long pId = p.getId();
            if(!PtoT.containsKey(pId))
                PtoT.put(pId, new ArrayList<>());
            return PtoT.get(pId);
        }
    }

    private class Table<T extends DatabaseRow> {
        private HashMap<Long, T> rows;

        public void add(T t) {
            assert(t.getDatabase() == Database.this);
            rows.put(t.getId(), t);
        }

        public boolean remove(long id) {
            return rows.remove(id) != null;
        }

        public T get(long id) {
            return rows.get(id);
        }
    }

    private Table<Course> courses;
    private Table<User> users;

    private NtoN<Course, User> followers;
    private NtoN<Course, User> owners;

    private long nextId;

    public Database() {
        courses = new Table<>();
        users = new Table<>();
        followers = new NtoN<>();
        owners = new NtoN<>();
        nextId = 1;
    }

    public Course newCourse(String name) {
        Course course = new Course(getNextId(), name);
        courses.add(course);
        return course;
    }

    public void addOwnerToCourse(User user, Course course) {
        owners.add(course, user);
    }

    public List<Course> getCoursesOwned(User owner) {
        return owners.getFromP(owner);
    }

    private Id getNextId() {
        return new Id(nextId++);
    }
}
