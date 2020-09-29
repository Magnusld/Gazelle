package gazelle.model;

import javafx.util.Pair;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

public class Database {
    public final class Id {
        private final long id;
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

    private class Table<T extends DatabaseRow> {
        private final HashMap<Long, T> rows = new HashMap<>();

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

        public Collection<T> getAll() {
            return Collections.unmodifiableCollection(rows.values());
        }

        private static final int DATA_VERSION = 1;
        public void dump(ObjectOutputStream oos) throws IOException {
            oos.writeUTF(getCheckString());
            oos.writeLong(rows.size());
            for(Map.Entry<Long, T> entry : rows.entrySet()) {
                oos.writeLong(entry.getKey());
                oos.writeObject(entry.getValue());
            }
        }

        public void load(ObjectInputStream ois) throws IOException {
            assert(rows.isEmpty());

            String check = ois.readUTF();
            if (!check.equals(getCheckString()))
                throw new IOException("File not correct version or format");
            long size = ois.readLong();
            try {
                for (long i = 0; i < size; i++) {
                    long id = ois.readLong();
                    @SuppressWarnings("unchecked")
                    T obj = (T) ois.readObject();
                    obj.respecifyId(new Id(id));
                    rows.put(id, obj);
                }
            }
            catch(ClassNotFoundException e) {
                throw new IOException(e);
            }
        }

        private String getCheckString() {
            return String.format("Table v.%d", DATA_VERSION);
        }
    }

    private class NtoN<T extends DatabaseRow, P extends  DatabaseRow> {
        private final HashMap<Long, List<P>> TtoP = new HashMap<>();
        private final HashMap<Long, List<T>> PtoT = new HashMap<>();

        private final Table<T> tTable;
        private final Table<P> pTable;

        public NtoN(Table<T> tTable, Table<P> pTable) {
            this.tTable = tTable;
            this.pTable = pTable;
        }

        public void add(T t, P p) {
            Objects.requireNonNull(t);
            Objects.requireNonNull(p);
            if(t.getDatabase() != Database.this || p.getDatabase() != Database.this)
                throw new IllegalArgumentException("Trying to form relationships between rows of different databases");

            long tId = t.getId();
            long pId = p.getId();
            assert(tId != pId);

            if (!TtoP.containsKey(tId))
                TtoP.put(tId, new ArrayList<>());
            if (!PtoT.containsKey(pId))
                PtoT.put(pId, new ArrayList<>());

            if (TtoP.get(tId).contains(p)) {
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

            if (!TtoP.containsKey(tId))
                return false;
            if (!PtoT.containsKey(pId))
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
            if (!TtoP.containsKey(tId))
                return false;
            if (!PtoT.containsKey(pId))
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
            if (!TtoP.containsKey(tId))
                TtoP.put(tId, new ArrayList<>());
            return Collections.unmodifiableList(TtoP.get(tId));
        }

        public List<T> getFromP(P p) {
            Objects.requireNonNull(p);
            assert(p.getDatabase() == Database.this);
            long pId = p.getId();
            if (!PtoT.containsKey(pId))
                PtoT.put(pId, new ArrayList<>());
            return Collections.unmodifiableList(PtoT.get(pId));
        }

        private static final int DATA_VERSION = 1;
        public void dump(ObjectOutputStream oos) throws IOException {
            ArrayList<Pair<Long, Long>> pairs = new ArrayList<>();
            TtoP.forEach((tId, pList)->
                pList.forEach(p -> {
                    long pId = p.getId();
                    pairs.add(new Pair<>(tId, pId));
                }));

            oos.writeUTF(getCheckString());
            oos.writeLong(pairs.size());
            for (Pair<Long, Long> pair : pairs) {
                oos.writeLong(pair.getKey());
                oos.writeLong(pair.getValue());
            }
        }

        public void load(ObjectInputStream ois) throws IOException {
            assert(TtoP.isEmpty() && PtoT.isEmpty());

            String check = ois.readUTF();
            if (!check.equals(getCheckString()))
                throw new IOException("File not correct version or format");
            long pairs = ois.readLong();
            for (long i = 0; i < pairs; i++) {
                long tId = ois.readLong();
                long pId = ois.readLong();
                add(tTable.get(tId), pTable.get(pId));
            }
        }

        private String getCheckString() {
            return String.format("NtoN v.%d", DATA_VERSION);
        }
    }

    private final Table<Course> courses = new Table<>();
    private final Table<User> users = new Table<>();

    private final NtoN<Course, User> followers = new NtoN<>(courses, users);
    private final NtoN<Course, User> owners = new NtoN<>(courses, users);

    private static final long START_ID = 1;
    private long nextId = START_ID;

    public Database() {
    }

    public User newUser() {
        User user = new User(getNextId());
        users.add(user);
        return user;
    }

    /** Gets the User with a specific id, or null if no user has that id.
     * @param id the user id
     * @return the user or null
     */
    public User getUser(long id) {
        return users.get(id);
    }

    /** Gets all Users, in no particular order
     * @return A Collection of all users, unmodifiable
     */
    public Collection<User> getUsers() {
        return users.getAll();
    }

    public Course newCourse(String name) {
        Course course = new Course(getNextId(), name);
        courses.add(course);
        return course;
    }

    /** Gets the Course with a specific id, or null if no course has that id
     * @param id the course id
     * @return the course or null
     */
    public Course getCourse(long id) {
        return courses.get(id);
    }

    /** Gets all Courses, in no particular order
     * @return A Collection of all courses, unmodifiable
     */
    public Collection<Course> getCourses() {
        return courses.getAll();
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

    public void dump(ObjectOutputStream oos) throws IOException {
        users.dump(oos);
        courses.dump(oos);
        followers.dump(oos);
        owners.dump(oos);
        oos.writeLong(nextId);
    }

    public void load(ObjectInputStream ois) throws IOException {
        if(nextId != START_ID)
            throw new IllegalStateException("Can't load into non-empty database");
        users.load(ois);
        courses.load(ois);
        followers.load(ois);
        owners.load(ois);
        nextId = ois.readLong();
    }

    private Id getNextId() {
        return new Id(nextId++);
    }
}
