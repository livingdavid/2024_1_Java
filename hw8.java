import java.util.Scanner;

abstract class Shape {
    abstract String getName();
    abstract double getArea();
    abstract double getPerimeter();

    void printShape() {
        System.out.println("Shape: " + getName());
        System.out.println("Area: " + getArea());
        System.out.println("Perimeter: " + getPerimeter());
        System.out.println();
    }
}

class Rectangle extends Shape {
    private double width;
    private double height;

    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }

    @Override
    String getName() {
        return "Rectangle";
    }

    @Override
    double getArea() {
        return width * height;
    }

    @Override
    double getPerimeter() {
        return 2 * (width + height);
    }
}

class Triangle extends Shape {
    private double width;
    private double height;

    public Triangle(double width, double height) {
        this.width = width;
        this.height = height;
    }

    @Override
    String getName() {
        return "Right Triangle";
    }

    @Override
    double getArea() {
        return (width * height) / 2;
    }

    @Override
    double getPerimeter() {
        double hypotenuse = Math.sqrt(width * width + height * height);
        return width + height + hypotenuse;
    }
}

class Circle extends Shape {
    private double radius;

    public Circle(double radius) {
        this.radius = radius;
    }

    @Override
    String getName() {
        return "Circle";
    }

    @Override
    double getArea() {
        return Math.PI * radius * radius;
    }

    @Override
    double getPerimeter() {
        return 2 * Math.PI * radius;
    }
}

public class hw8 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Shape[] shapes = new Shape[5];

        for (int i = 0; i < shapes.length; i++) {
            System.out.println("Enter shape type (rectangle, triangle, circle):");
            String shapeType = scanner.next().toLowerCase();

            switch (shapeType) {
                case "rectangle":
                    System.out.println("Enter width and height:");
                    double width = scanner.nextDouble();
                    double height = scanner.nextDouble();
                    shapes[i] = new Rectangle(width, height);
                    break;
                case "triangle":
                    System.out.println("Enter width and height:");
                    width = scanner.nextDouble();
                    height = scanner.nextDouble();
                    shapes[i] = new Triangle(width, height);
                    break;
                case "circle":
                    System.out.println("Enter radius:");
                    double radius = scanner.nextDouble();
                    shapes[i] = new Circle(radius);
                    break;
                default:
                    System.out.println("Invalid shape type. Using default circle with radius 1.");
                    shapes[i] = new Circle(1);
                    break;
            }
        }

        System.out.println("\nShape details:");
        for (Shape shape : shapes) {
            shape.printShape();
        }

        scanner.close();
    }
}
