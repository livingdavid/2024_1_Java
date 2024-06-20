public class Person {
    private String name;
    private int age;

    public Person() {
        this.name = "No name yet";
        this.age = 0;
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return this.name;
    }

    public int getAge() {
        return this.age;
    }

    public void setName(String first, String last) {
        this.name = first + " " + last;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public static Person createAdult() {
        return new Person("An adult", 21);
    }

    public static Person createToddler() {
        return new Person("A toddler", 2);
    }

    public static Person createPreschooler() {
        return new Person("A preschooler", 5);
    }

    public static Person createAdolescent() {
        return new Person("An adolescent", 9);
    }

    public static Person createTeenager() {
        return new Person("A teenager", 15);
    }

    public void setPersonInfo(String name, int age) {
        setName(name);
        setAge(age);
    }
    
    public static void main(String[] args) {
        Person adult = Person.createAdult();
        Person toddler = Person.createToddler();
        Person preschooler = Person.createPreschooler();
        Person adolescent = Person.createAdolescent();
        Person teenager = Person.createTeenager();

        System.out.println("Adult: " + adult.getName() + ", Age: " + adult.getAge());
        System.out.println("Toddler: " + toddler.getName() + ", Age: " + toddler.getAge());
        System.out.println("Preschooler: " + preschooler.getName() + ", Age: " + preschooler.getAge());
        System.out.println("Adolescent: " + adolescent.getName() + ", Age: " + adolescent.getAge());
        System.out.println("Teenager: " + teenager.getName() + ", Age: " + teenager.getAge());
    }
}
