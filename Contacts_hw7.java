import java.util.*;

abstract class Person implements Comparable<Person> {
    protected String dept;
    protected String name;
    protected String id;
    protected String email;

    public Person(String dept, String name, String id, String email) {
        this.dept = (dept.isEmpty()) ? "Not Yet Defined" : dept;
        this.name = name;
        this.id = id;
        this.email = (email.isEmpty()) ? "Not Yet Defined" : email;
    }

    public String getName() {
        return name;
    }

    public String getDept() {
        return dept;
    }

    abstract public void writeOutput();

    @Override
    public int compareTo(Person other) {
        int nameComparison = this.name.compareTo(other.name);
        if (nameComparison != 0) {
            return nameComparison;
        }
        return this.dept.compareTo(other.dept);
    }
}

abstract class Student extends Person {
    public Student(String dept, String name, String id, String email) {
        super(dept, name, id, email);
    }
}

abstract class Employee extends Person {
    public Employee(String dept, String name, String id, String email) {
        super(dept, name, id, email);
    }
}

class Under extends Student {
    private static int count = 0;
    private int underNumber;

    public Under(String dept, String name, String id, String email) {
        super(dept, name, id, email);
        underNumber = ++count;
    }

    public void writeOutput() {
        System.out.println("Under " + underNumber + ", " + dept + ", Name: " + name + ", Under Student Number: " + id + " (" + email + ")");
    }
}

class Grad extends Student {
    private static int count = 0;
    private int gradNumber;

    public Grad(String dept, String name, String id, String email) {
        super(dept, name, id, email);
        gradNumber = ++count;
    }

    public void writeOutput() {
        System.out.println("Grad " + gradNumber + ", " + dept + ", Name: " + name + ", Grad Student Number: " + id + " (" + email + ")");
    }
}

class Faculty extends Employee {
    private static int count = 0;
    private int facultyNumber;

    public Faculty(String dept, String name, String id, String email) {
        super(dept, name, id, email);
        facultyNumber = ++count;
    }

    public void writeOutput() {
        System.out.println("Faculty " + facultyNumber + ", " + dept + ", Name: " + name + ", Faculty ID: " + id + " (" + email + ")");
    }
}

class Staff extends Employee {
    private static int count = 0;
    private int staffNumber;

    public Staff(String dept, String name, String id, String email) {
        super(dept, name, id, email);
        staffNumber = ++count;
    }

    public void writeOutput() {
        System.out.println("Staff " + staffNumber + ", " + dept + ", Name: " + name + ", Staff ID: " + id + " (" + email + ")");
    }
}

public class Contacts_hw7 {
    private static List<Person> persons = new ArrayList<>();

    // Comparators
    private static Comparator<Person> byDeptComparator = Comparator.comparing(Person::getDept);
    private static Comparator<Person> byNameComparator = Comparator.comparing(Person::getName).thenComparing(Person::getDept);
        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {
                String input = scanner.nextLine();
                if (input.equals("print")) {
                    for (Person p : persons) {
                        p.writeOutput();
                    }
                } else if (input.startsWith("Under") || input.startsWith("Grad") || input.startsWith("Faculty") || input.startsWith("Staff")) {
                    String[] parts = input.split(", ");
                    String type = parts[0];
                    String dept = (parts.length > 1) ? parts[1] : "";
                    String name = (parts.length > 2) ? parts[2] : "";
                    String id = (parts.length > 3) ? parts[3] : "";
                    String email = (parts.length > 4) ? parts[4] : "";
                    addPerson(type, dept, name, id, email);
                } else if (input.startsWith("sort name")) {
                    Collections.sort(persons, byNameComparator);
                } else if (input.startsWith("sort dept")) {
                    Collections.sort(persons, byDeptComparator);
                } else if (input.startsWith("quit")){
                    break;
                }
            }
            scanner.close();
    }

    private static void addPerson(String type, String dept, String name, String id, String email) {
        switch (type) {
            case "Under":
                persons.add(new Under(dept, name, id, email));
                break;
            case "Grad":
                persons.add(new Grad(dept, name, id, email));
                break;
            case "Faculty":
                persons.add(new Faculty(dept, name, id, email));
                break;
            case "Staff":
                persons.add(new Staff(dept, name, id, email));
                break;
        }
    }
}
