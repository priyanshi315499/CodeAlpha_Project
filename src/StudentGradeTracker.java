import java.util.ArrayList;
import java.util.Scanner;

  class Student{
    String name;
    int grade;

    Student(String name, int grade){
        this.name = name;
        this.grade = grade;
    }
}
public class StudentGradeTracker {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        ArrayList<Student> students = new ArrayList<>();

        while(true){
            System.out.println("\n-- Student Grade Tracker ---");
            System.out.println("1. Add Students");
            System.out.println("2. View All Students");
            System.out.println("3. Show Statistics (Average, Highest, Lowest)");
            System.out.println("4. Exit");
            System.out.println("Choose an option: ");

            int choice = sc.nextInt();
            sc.nextInt();

            switch (choice){
                case 1:
                    String name = sc.nextLine();
                    System.out.println("Enter grade: ");
                    int grade = sc.nextInt();
                    students.add(new Student(name, grade));
                    System.out.println("Student added!");
                    break;

                case 2:
                    System.out.println("\n---Student List---");
                    for (Student s : students) {
                        System.out.println(s.name + " - " + s.grade);
                    }
                    break;

                case 3:
                    if (students.isEmpty()) {
                        System.out.println("No student data available.");
                        break;
                    }
                    int sum = 0, highest = Integer.MIN_VALUE, lowest = Integer.MAX_VALUE;
                    for (Student s : students) {
                        sum += s.grade;
                        if (s.grade > highest)
                            highest = s.grade;
                        if (s.grade < lowest) ;
                        lowest = s.grade;
                    }
                    double avg = (double) sum/ students.size();
                    System.out.println("\n---Statistics---");
                    System.out.println("Average Score: " + avg);
                    System.out.println("Highest Score: " + highest);
                    System.out.println("Lowest Score :" + lowest);
                    break;

                case 4:
                    System.out.println("Exciting program...");
                    sc.close();
                    return;

                default: System.out.println("Invalid option, try again.");
            }
        }
    }
}


