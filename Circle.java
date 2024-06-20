import java.util.Scanner;

public class Circle {
    private double radius;
    private double area;
    private double circumference;

    public Circle(double radius) {
        this.radius = radius;
        calculateArea();
        calculateCircumference();
    }

    private void calculateArea() {
        area = 3.14 * radius * radius;
    }

    private void calculateCircumference() {
        circumference = 2 * 3.14 * radius;
    }

    public double getArea() {
        return area;
    }

    public double getCircumference() {
        return circumference;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the first radius: ");
        double firstRadius = scanner.nextDouble();
        Circle firstCircle = new Circle(firstRadius);

        System.out.print("Enter the second radius: ");
        double secondRadius = scanner.nextDouble();
        Circle secondCircle = new Circle(secondRadius);

        System.out.println("First circle: The area is " + firstCircle.getArea() +
                " and the circumference is " + firstCircle.getCircumference() + ".");
        System.out.println("Second circle: The area is " + secondCircle.getArea() +
                " and the circumference is " + secondCircle.getCircumference() + ".");

        double ratioOfAreas = firstCircle.getArea() / secondCircle.getArea();
        System.out.println("The ratio of the areas is " + ratioOfAreas + ".");
    }
}
