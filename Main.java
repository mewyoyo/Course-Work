import java.util.*;
public class Main {
    static HashMap<String, HashSet<String>> prereqs = new HashMap<>();

    static HashMap<String, HashSet<String>> completed = new HashMap<>();

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Course Enrollment Planner");
        System.out.println("Type HELP to see commands.");

        while (true) {
            System.out.println("> ");
            String line = scanner.nextLine().trim();

            if (line.isEmpty()) continue;

            String[] parts = line.split("\\s+");
            String command = parts[0].toUpperCase();

            switch (command) {

                case "HELP":
                    printHelp();
                    break;

                case "ADD_COURSE":
                    addCourse(parts);
                    break;

                case "EXIT":
                    System.out.println("Goodbye!");
                    return;   //

                case "ADD_PREREQ":
                    addPrereq(parts);
                    break;

                case "COMPLETE":
                    completeCourse(parts);
                    break;

                case "CAN_TAKE":
                    canTake(parts);
                    break;

                case "PREREQS":
                    printPrereqs(parts);
                    break;

                case "DONE":
                    printCompleted(parts);
                    break;

                case "TEST_TIME":
                    testTime();
                    break;

                default:
                    System.out.println("Unknown command");
            }

        }

    }

    static void printHelp() {
        System.out.println("Commands:");
        System.out.println("ADD_COURSE <C>");
        System.out.println("ADD_PREREQ <C> <P>");
        System.out.println("PREREQS <C>");
        System.out.println("COMPLETE <student> <C>");
        System.out.println("DONE <student>");
        System.out.println("CAN_TAKE <student> <C>");
        System.out.println("EXIT");
    }

    static void addCourse(String[] parts) {

        if (parts.length != 2) {
            System.out.println("Usage: ADD_COURSE <C>");
            return;
        }

        String course = parts[1];

        prereqs.putIfAbsent(course, new HashSet<>());

        System.out.println("Course added (or already exists): " + course);
    }
    static void addPrereq(String[] parts) {

        if (parts.length != 3) {
            System.out.println("Usage: ADD_PREREQ <C> <P>");
            return;
        }

        String course = parts[1];
        if (course.trim().isEmpty()) {
            System.out.println("Invalid course name.");
            return;
        }
        String prereq = parts[2];
        if (course.trim().isEmpty() || prereq.trim().isEmpty()) {
            System.out.println("Invalid input.");
            return;
        }

        if (course.equals(prereq)) {
            System.out.println("A course cannot be its own prerequisite.");
            return;
        }

        if (prereqs.containsKey(prereq) &&
                prereqs.get(prereq).contains(course)) {

            System.out.println("Warning: Potential cycle detected!");
            return;
        }

        prereqs.putIfAbsent(course, new HashSet<>());

        prereqs.putIfAbsent(prereq, new HashSet<>());

        prereqs.get(course).add(prereq);

        System.out.println("Added prereq: " + prereq + " -> " + course);
    }

    static void completeCourse(String[] parts) {

        if (parts.length != 3) {
            System.out.println("Usage: COMPLETE <student> <course>");
            return;
        }

        String student = parts[1];
        String course = parts[2];


        completed.putIfAbsent(student, new HashSet<>());

        completed.get(student).add(course);

        System.out.println(student + " completed " + course);
    }

    static void canTake(String[] parts) {

        if (parts.length != 3) {
            System.out.println("Usage: CAN_TAKE <student> <course>");
            return;
        }

        String student = parts[1];
        String course = parts[2];


        if (!prereqs.containsKey(course) || prereqs.get(course).isEmpty()) {
            System.out.println("YES");
            return;
        }


        if (!completed.containsKey(student)) {
            System.out.println("NO");
            return;
        }

        HashSet<String> required = prereqs.get(course);
        HashSet<String> done = completed.get(student);

        for (String prereq : required) {
            if (!done.contains(prereq)) {
                System.out.println("NO");
                return;
            }
        }

        System.out.println("YES");
    }

    static void printPrereqs(String[] parts) {

        if (parts.length != 2) {
            System.out.println("Usage: PREREQS <C>");
            return;
        }

        String course = parts[1];

        if (!prereqs.containsKey(course)) {
            System.out.println("Course not found");
            return;
        }

        System.out.println("Prereqs for " + course + ": " + prereqs.get(course));
    }

    static void printCompleted(String[] parts) {

        if (parts.length != 2) {
            System.out.println("Usage: DONE <student>");
            return;
        }

        String student = parts[1];

        if (!completed.containsKey(student)) {
            System.out.println("No record");
            return;
        }

        System.out.println("Completed courses: " + completed.get(student));
    }

    static void testTime() {

        int[] sizes = {100, 1000, 10000};

        for (int k : sizes) {

            prereqs.clear();
            completed.clear();

            String student = "TestStudent";
            String course = "BigCourse";

            prereqs.put(course, new HashSet<>());
            completed.put(student, new HashSet<>());

            for (int i = 0; i < k; i++) {
                String prereq = "C" + i;
                prereqs.get(course).add(prereq);
                completed.get(student).add(prereq);
            }

            long start = System.nanoTime();

            HashSet<String> required = prereqs.get(course);
            HashSet<String> done = completed.get(student);

            for (String p : required) {
                if (!done.contains(p)) {
                    break;
                }
            }

            long end = System.nanoTime();

            System.out.println("k = " + k + " → " + (end - start) + " ns");
        }
    }
}
