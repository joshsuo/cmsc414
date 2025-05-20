/**
 * A helper class for your gradebook
 * Some of these methods may be useful for your program
 * You can remove methods you do not need
 * If you do not wiish to use a Gradebook object, don't
 */

import java.util.ArrayList;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Gradebook implements java.io.Serializable{
    public static final long serialVersionUID = 1;
  ArrayList<Student> studentList;
  ArrayList<Assignment> assignmentList;

  /* Read a Gradebook from a file */
  public Gradebook(String filename) {
    //create a gradebook object
    try {
      FileInputStream fis = new FileInputStream(filename);
      ObjectInputStream ois = new ObjectInputStream(fis);
      Gradebook gb = (Gradebook) ois.readObject();
      this.studentList = gb.studentList;
      this.assignmentList = gb.assignmentList;
      ois.close();
    } catch (Exception ex) {
      System.out.println(ex);
      System.out.println("invalid");
      System.exit(255);
    } 
  }

  /* Create a new gradebook */
  public Gradebook() {
    studentList = new ArrayList<Student>();
    assignmentList = new ArrayList<Assignment>();
  }

  /* return the size of the gradebook */
  public int size() {
    return studentList.size();
  }

  /* Adds a student to the gradebook */
  public void addStudent(String firstName, String lastName) {
    Student newStud = new Student(firstName, lastName);
    studentList.add(newStud);
    for(Assignment assignment : assignmentList) {
      newStud.addAssignment(assignment);
    }
  }

  //delete student from gradebook
  public void delStudent(String firstName, String lastName) {
    for(int i=0; i < studentList.size(); i++) {
      if(studentList.get(i).firstName.equals(firstName) && studentList.get(i).lastName.equals(lastName)) {
        studentList.remove(i);
        return;
      }
    }
  }

  // checks for repeated students
  public boolean containsStudent(String firstName, String lastName) {
    for(int i=0; i < studentList.size(); i++) {
      if(studentList.get(i).firstName.equals(firstName) && studentList.get(i).lastName.equals(lastName)) {
        return true;
      }
    }
    return false;
  }

  public Student getStudent(String firstName, String lastName) {
    for (int i = 0; i < studentList.size(); i++) { 
      if(studentList.get(i).firstName.equals(firstName) 
        && studentList.get(i).lastName.equals(lastName)) {
          
        return studentList.get(i);
      }
    }
    return null;
  }

  public ArrayList<Student> getStudentList() {
    return studentList;
  }

  /* Adds an assinment to the gradebook */
  public void addAssignment(String assignName, int points, double weight) {
    assignmentList.add(new Assignment(assignName, points, weight));
    for (Student student : studentList) {
      student.assignmentList.add(new Assignment(assignName, points, weight));
    }
  }

  public void delAssignment(String name) {
    for(int i=0; i < assignmentList.size(); i++) {
      if(assignmentList.get(i).name.equals(name)) {
        assignmentList.remove(i);
        return;
      }
    }
  }

  // checks for repeated students
  public boolean containsAssignment(String name) {
    //if(assignmentList == null) return false;

    for(int i=0; i < assignmentList.size(); i++) {
      if(assignmentList.get(i).name.equals(name)) {
        return true;
      }
    }
    return false;
  }

  /* Adds a grade to the gradebook */
  public void addGrade(String studentFirstName, String studentLastName, String assignmentName, int studentScore) {
    if (studentScore < 0) {
      // throw error
    }
    for (Student student : studentList) {
      if (student.getName().equals(studentFirstName + " " + studentLastName)) {
        student.addGrade(assignmentName, studentScore);
        break;
      }
    }
  }

  public double getTotalWeights() {
    double total = 0;
    for(int i=0; i < assignmentList.size(); i++) {
      total += assignmentList.get(i).percentWeight;
    }
    return total;
  }

  public void save(String filename) {
    try {
      FileOutputStream fos = new FileOutputStream(filename);
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      oos.writeObject(this);
      oos.close();
      fos.close();
    }catch (Exception ex){
      System.out.println("error writing file");
      System.exit(255);
    }
  }

  public String toString() {
    String ret = "";
    for (Student student : studentList) {
      ret += student.toString() + "\n";
    }
    return ret;
  }


public class Student implements java.io.Serializable {
  public static final long serialVersionUID = 2;
  private String firstName;
  private String lastName;
  private ArrayList<Assignment> assignmentList;
  private double finalGrade = 0;

  public Student(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
    assignmentList = new ArrayList<Assignment>();
  }

  public ArrayList<Assignment> getAssignmentList() {
    return assignmentList;
  }

  public void addAssignment(Assignment assignment) {
    assignmentList.add(assignment);
  }

  // checks for repeated assignment names
  // public boolean containsAssignment(String assignName) {
  //   for(int i=0; i < assignmentList.size(); i++) {
  //     if(assignmentList.get(i).name.equals(assignName)) {
  //       return true;
  //     }
  //   }
  //   return false;
  // }

  public String getName() {
    return this.firstName + " " + this.lastName;
  }

  public String getFirstName() {
    return this.firstName;
  }
  public String getLastName() {
    return this.lastName;
  }


  public void addGrade(String assignmentName, int studentScore) {
    for (Assignment assignment : assignmentList) {
      if (assignment.getName().equals(assignmentName)) {
        assignment.setStudentScore(studentScore);
        break;
      }
    }
  }

  public Assignment getAssignmentByName(String name) {
    for (Assignment assignment : assignmentList) {
      if (assignment.getName().equals(name)) {
        return assignment;
      }
    }
    return null;
  }

  public void setFinalGrade(Double fg){
    finalGrade = fg;
  }

  public double getFinalGrade(){
      return finalGrade;
  }


  public String toString() {
    String ret = this.firstName + " " + this.lastName + ":";
    for (Assignment assignment : assignmentList) {
      ret += assignment.toString();
    }
    return ret;
  }



}

public class Assignment implements java.io.Serializable{
  public static final long serialVersionUID = 3;
  private String name;
  private int numberOfPoints;
  private double percentWeight;
  private int studentScore;

  public Assignment(String name, int points, double weight) {
    if (points >= 0 && weight >= 0 && weight <= 1) {
      this.name = name;
      numberOfPoints = points;
      percentWeight = weight;
    }
    else {
      // throw error
    }
  }

    public Assignment(String name, int points, double weight, int studentScore) {
    if (points >= 0 && studentScore >= 0 && weight >= 0 && weight <= 1) {
      this.name = name;
      numberOfPoints = points;
      percentWeight = weight;
      this.studentScore = studentScore;
    }
    else {
      // throw error
    }
  }

  // set studentScore to parameter value
  public void setStudentScore(int studentScore) {
    if (studentScore >= 0) {
      this.studentScore = studentScore;
    }
    else {
      // throw error
    }
  }

  public double getWeight() {
    return percentWeight;
  }

  public int getNumberOfPoints() {
    return numberOfPoints;
  }

  public int getScore() {
    return studentScore;
  }

  // return assignment name
  public String getName() {
    return this.name;
  }

  public String toString() {
    return "(" + this.name + "," + this.numberOfPoints + "," + this.percentWeight + "," 
      + this.studentScore + ")";
  }

}
}