import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

/**
 * Allows the user to add a new student or assignment to a gradebook,
 * or add a grade for an existing student and existing assignment
 */
public class gradebookadd {

  private static Serializable decryptObjectWithIV(String algorithm, byte[] combinedData,
            SecretKey key) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException,
            InvalidKeyException, IOException, ClassNotFoundException,
            IllegalBlockSizeException, BadPaddingException {

        ByteArrayInputStream inputStream = new ByteArrayInputStream(combinedData);
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

        try {            
            // Read IV as byte array
            byte[] ivBytes = new byte[16];
            objectInputStream.readFully(ivBytes);
            IvParameterSpec iv = new IvParameterSpec(ivBytes);

            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, key, iv);

            // Read SealedObject
            SealedObject sealedObject = (SealedObject) objectInputStream.readObject();

            // Perform decryption
            Object decryptedObject = sealedObject.getObject(cipher);

            // Check and cast to Serializable
            if (decryptedObject instanceof Serializable) {
                return (Serializable) decryptedObject;
            } else {
                throw new ClassNotFoundException("Decrypted object is not Serializable.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            objectInputStream.close();
        }
        return null;
    }

      public static byte[] encryptObject(String algorithm, Serializable object,
            SecretKey key, IvParameterSpec iv) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException,
            InvalidKeyException, IOException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);

        SealedObject sealedObject = new SealedObject(object, cipher);

        byte[] ivBytes = iv.getIV();

        // Serialize IV and SealedObject
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

        // Write IV
        objectOutputStream.write(ivBytes);
        
        // Write SealedObject
        objectOutputStream.writeObject(sealedObject);

        objectOutputStream.close();

        // Get the combined byte array
        return outputStream.toByteArray();
    }

    private static void writeToFile(String filename, byte[] data) throws IOException {
      try (FileOutputStream fileOutputStream = new FileOutputStream(filename)) {
          fileOutputStream.write(data);
      }
  }

  private static byte[] readFromFile(String filename) throws IOException {
    try (FileInputStream fileInputStream = new FileInputStream(filename)) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024];
        while ((nRead = fileInputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        return buffer.toByteArray();
    }
  }

  /* parses the cmdline to keep main method simplified */
  private static int parse_cmdline(String[] args) {


    //System.out.println(args.length);

 
    if(args.length==1)
      System.out.println("\nNo Extra Command Line Argument Passed Other Than Program Name");
    if(args.length>=2) {
      System.out.println("\nNumber Of Arguments Passed: " + args.length);
      System.out.println("----Following Are The Command Line Arguments Passed----");
      for(int counter=0; counter < args.length; counter++)
        System.out.println("args[" + counter + "]: " + args[counter]);
        // Decide what is the setting we are in
    }
    //TODO ...

    // my code

    // read file

    // create object from file


    if(!(args[0].equals("-N"))) {
      System.out.println("invalid");
      System.exit(255);
    } else if (!(args[2].equals("-K"))){
      System.out.println("invalid");
      System.exit(255);
    }

    //nFlag = args[0];
    String gbName = args[1];
    //kFlag = args[2];
    String key = args[3];
    String action = args[4];
    Gradebook gb = new Gradebook(gbName);

    // need to check key

    /*          ADD ASSIGNMENT          */
    if(action.equals("-AA")) {
      System.out.println("add assignment");
      String assignment = "";
      int points = -1;
      double weight = -2;
      for(int i = 5; i < args.length; i++) {
        if(args[i].equals("-AN")) { // check flag -AN
          assignment = args[i+1];
          i++;
        } else if(args[i].equals("-P")) { // check flag -P
          try { // check if points is really an int
            points = Integer.parseInt(args[i+1]);
          } catch (NumberFormatException ex) { // if not int set points to -1
            points = -1;
            //System.out.println("invalid number: points");
            //System.exit(255);
          }
          i++;
        } else if (args[i].equals("-W")) { // check flag -W
          try { // check if weight is really a double
            weight = Double.parseDouble(args[i+1]);
          } catch (NumberFormatException ex) { // if not double set weight to -2
            weight = -2;
            //System.out.println("invalid number: weight");
            //System.exit(255);
          }
          i++;
        }
      }

      // if assignment name is blank OR if assignment exists OR points < 0, return error
      if(assignment.equals("") || gb.containsAssignment(assignment) == true || points < 0) {
        System.out.println("invalid");
        System.exit(255);
      }
            System.out.println(gb.getTotalWeights() + weight);

      // if weight > 1 OR weight < 0, return error
      if(gb.getTotalWeights() + weight > 1 || gb.getTotalWeights() + weight < 0) {
        System.out.println("total weight is above 1 or below 0");
        System.exit(255);
      }
      
      gb.addAssignment(assignment, points, weight);


    /*          DELETE ASSIGNMENT          */
    } else if(action.equals("-DA")) {
      System.out.println("delete assignment");
      String assignment = "";
      for(int i = 5; i < args.length; i++) {
        if(args[i].equals("-AN")) { // check flag -AN
          assignment = args[i+1];
          i++;
        } 
      }

      // assignment is black OR if assignment doesn't exist, return error
      if(assignment.equals("") || !(gb.containsAssignment(assignment))) {
        System.out.println("assignment does not exist");
        System.exit(255);
      }

      gb.delAssignment(assignment);

      
    /*          ADD STUDENT          */
    } else if(action.equals("-AS")) {
      System.out.println("add student");
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
        System.out.println("invalid");
        System.exit(255);
      }
      // if student exists, return error
      if(gb.containsStudent(fn, ln)) {
        System.out.println("invalid");
        System.exit(255);
      }

      gb.addStudent(fn, ln);
      
    /*          DELETE STUDENT          */
    } else if(action.equals("-DS")) {
      System.out.println("delete student");
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
        System.out.println("invalid");
        System.exit(255);
      }
      // if student doesn't exist, return error
      if(!(gb.containsStudent(fn, ln))) {
        System.out.println("student does not exist");
        System.exit(255);
      }

      gb.delStudent(fn, ln);
      
    /*          ADD GRADE          */
    } else if(action.equals("-AG")) {
      System.out.println("add grade");
      String fn = "";
      String ln = "";
      String assignment = "";
      int grade = -1;
      for(int i = 5; i < args.length; i++) {
        if(args[i].equals("-FN")) { // check flag -FN
          fn = args[i+1];
          i++;
        } else if(args[i].equals("-LN")) { // check flag -LN
          ln = args[i+1];
          i++;
        } else if(args[i].equals("-AN")){ // check flag -AN
          assignment = args[i+1];
          i++;
        } else if(args[i].equals("-G")) { // check flag -G
          try {
            grade = Integer.parseInt(args[i+1]);
          } catch (NumberFormatException ex) {
            grade = 0;
          }
          i++;
        } 
      }
      // if first OR last name is blank OR assignment is black OR grade < 0, return error
      if(fn.equals("") || ln.equals("")
          || assignment.equals("") || grade < 0) {
        System.out.println("invalid");
        System.exit(255);
      }
      // if student doesn't exist, return error
      if(!(gb.containsStudent(fn, ln))) { 
        System.out.println("student does not exist");
        System.exit(255);
      }
      // if assignment doesn't exist, return error
      if(!(gb.containsAssignment(assignment))) {
        System.out.println("assignment does not exist");
        System.exit(255);
      }

      gb.addGrade(fn, ln, assignment, grade);
      
    } else { // if action flag is not {-AA, - DA, -AS, -DS, -AG}
      System.out.println("invalid");
      System.exit(255);
    }

    gb.save(gbName);

    return 0;
    //return something;
  }

  public static void main(String[] args) {
    //gradebookadd gb = new gradebookadd();
    int result = parse_cmdline(args);

    

    return;
  }
}
