//import ...

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Prints out a gradebook in a few ways
 * Some skeleton functions are included
 */
public class gradebookdisplay {
  static boolean verbose = false;

  // private void print_Gradebook(...) {

  //   for(int i = 0; i < num_assigment; i++) {
  //     dump_assignment();
  //     System.out.println("----------------\n");
  //   }

  //   return;
  // }

  private static Comparator<Gradebook.Student> getCompByLastName() {
    Comparator<Gradebook.Student> comp = new Comparator<Gradebook.Student>() {
      @Override
      public int compare(Gradebook.Student s1, Gradebook.Student s2) {
        return s1.getLastName().compareTo(s2.getLastName());
      }
    };
    return comp;
  }

  private static Comparator<Gradebook.Student> getCompByGrade(String assignmentName) {
    System.out.println(assignmentName);
    Comparator<Gradebook.Student> comp = new Comparator<Gradebook.Student>() {
      @Override
      public int compare(Gradebook.Student s1, Gradebook.Student s2) {
        Integer score1 = s1.getAssignmentByName(assignmentName).getScore();
        Integer score2 = s2.getAssignmentByName(assignmentName).getScore();
        return score2.compareTo(score1);
      }
    };
    return comp;
  }

  private static Comparator<Gradebook.Student> getCompByFinalGrade() {
    Comparator<Gradebook.Student> comp = new Comparator<Gradebook.Student>() {
      @Override
      public int compare(Gradebook.Student s1, Gradebook.Student s2) {
        Double score1 = s1.getFinalGrade();
        Double score2 = s2.getFinalGrade();
        return score2.compareTo(score1);
      }
    };
    return comp;
  }

  private static void print_Assignment(Gradebook gb, String assignmentName, String flag) {
    if(gb != null) {
      if(gb.containsAssignment(assignmentName)) {
        ArrayList<Gradebook.Student> studentList = gb.getStudentList();
        if(flag.equals("a")) {
          Collections.sort(studentList, getCompByLastName());
        } else if (flag.equals("g")) {
          Collections.sort(studentList, getCompByGrade(assignmentName));
        }else {
            System.out.println("invalid flag");
            System.exit(255);
        }

        for(int i = 0; i<gb.getStudentList().size(); i++) {
          Gradebook.Student student = gb.getStudentList().get(i);
          Gradebook.Assignment studentAssignment = student.getAssignmentByName(assignmentName);
           System.out.println("("+ student.getLastName() + ", " + student.getFirstName()+ ", "
                + studentAssignment.getScore()+")");
        }

      }
    }
    return;
  }

  private static void print_Student( Gradebook gb, String firstName, String lastName) {
    //System.out.println("in print_Student");
    if (gb!=null){
      if (gb.containsStudent(firstName, lastName)){
        Gradebook.Student theStudent = gb.getStudent(firstName, lastName);

        for(int i =0; i < theStudent.getAssignmentList().size(); i++) {
          Gradebook.Assignment assignment = theStudent.getAssignmentList().get(i);
          System.out.println("(" + assignment.getName() +", "
          + assignment.getScore() + ")");
        }
      }
    }
    return;
  }

  private static void print_Final(Gradebook gb, String flag){
    if (gb!=null){
      for (Gradebook.Student student : gb.studentList) {
      double sum = 0;
      ArrayList<Gradebook.Assignment> listOfGrades = student.getAssignmentList(); 

        for (Gradebook.Assignment assignment : listOfGrades) { 
          sum += ((double)assignment.getWeight() * ((double)assignment.getScore()/(double)assignment.getNumberOfPoints()));
        }
        student.setFinalGrade(sum);
      }
      ArrayList<Gradebook.Student> studList = gb.studentList;
      if(flag.equals("a")) {
          Collections.sort(studList, getCompByLastName());
      } else if (flag.equals("g")) {
          Collections.sort(studList, getCompByFinalGrade());
      }else {
          System.out.println("invalid flag");
          System.exit(255);
      }

      for(Gradebook.Student student : studList) {
        System.out.println("(" + student.getLastName() + ", " + student.getFirstName()
            + ", " + student.getFinalGrade() + ")");
      }
    }
    return;
  }


  public static void main(String[] args) {
    int   opt,len;
    char  logpath;

    //TODO Code this
    if(args.length==1)
        System.out.println("\nNo Extra Command Line Argument Passed Other Than Program Name");
    if(args.length>=2)
    {
        System.out.println("\nNumber Of Arguments Passed: " + args.length);
        System.out.println("----Following Are The Command Line Arguments Passed----");
        for(int counter = 0; counter < args.length; counter++)
          System.out.println("args[" + counter + "]: " + args[counter]);
          // Decide what is the setting we are in
    }

    System.out.println("test");

    if(!(args[0].equals("-N"))) {
      System.out.println("invalid, no -N flag");
      System.exit(255);
    } else if (!(args[2].equals("-K"))){
      System.out.println("invalid, no -K flag");
      System.exit(255);
    }

    //nFlag = args[0];
    String gbName = args[1];
    //kFlag = args[2];
    String key = args[3];
    String action = args[4];
    Gradebook gb = new Gradebook(gbName);
    System.out.println(gb.toString());

    /*          PRINT ASSIGNMENT          */
    if(action.equals("-PA")) {
      System.out.println("print assignment");
      String assignment = "";
      int counter = 0;
      String flag = "";
      for(int i = 5; i < args.length; i++) {
        if(args[i].equals("-AN")) { // check flag -AN
          assignment = args[i+1];
          i++;
        } else if(args[i].equals("-A")) { // check flag -A
          flag = "a";
          counter+=1;
        } else if(args[i].equals("-G")) { // check flag -G
          flag = "g";
          counter+=1;
        }
      }
      // check if multiple of flag -A and -G
      if(counter > 1) {
        System.out.println("invalid, multiple flags");
        System.exit(255);
      }
      // check if 
      if(assignment.equals("") || gb.containsAssignment(assignment) != true) {
        System.out.println("invalid, assignment blank OR assignment not exists");
        System.exit(255);
      }

      print_Assignment(gb, assignment, flag);

      /*          PRINT STUDENT          */
    } else if(action.equals("-PS")) {
      System.out.println("print student");
      String fn = "";
      String ln = "";
      for(int i = 5; i < args.length; i++) {
        if(args[i].equals("-FN")) { // check flag -FN
          fn = args[i+1];
          i++;
        } else if(args[i].equals("-LN")) { // check flag -LN
          ln = args[i+1];
          i++;
        }
      }
      //if first and last name is black, return error
      if(fn.equals("") || ln.equals("")) {
        System.out.println("invalid, student field is blank");
        System.exit(255);
      }
      // if student doesn't exist, return error
      if(!(gb.containsStudent(fn, ln))) {
        System.out.println("invalid, student doesn't exists");
        System.exit(255);
      }

      print_Student(gb, fn, ln);

      /*          PRINT FINAL          */
    } else if(action.equals("-PF")) {
      System.out.println("print final");
      int counter = 0;
      String flag = "";
      if(args[5].equals("-A")) { // check flag -A
        flag = "a";
        counter+=1;
      } else if(args[5].equals("-G")) { // check flag -G
        flag = "g";
        counter+=1;
      } else {
        System.out.println("invalid, no flags");
        System.exit(255);
      }
      // check if multiple of flag -A and -G
      if(counter > 1) {
        System.out.println("invalid, multuple flags");
        System.exit(255);
      }

      print_Final(gb, flag);

    } else {
      System.out.println("invalid, wrong action flag");
      System.exit(255);
    }

  }
}
