import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import javax.crypto.SecretKey;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.IvParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Initialize gradebook with specified name and generate a key.
 */
public class setup {

  /* test whether the file exists */

  // make sure static is allowed
  private static boolean file_test(String filename) {
    // TODO complete

    File file = new File(filename);
    return file.exists();
  }

  public static void main(String[] args) {

    String gbName;
    String key = "";
    SecretKey keyAES = null;
    IvParameterSpec ivspec = null;

    try {
      // generate IV
      SecureRandom secureRandom = new SecureRandom();
      byte[] iv = new byte[16];
      secureRandom.nextBytes(iv);
      ivspec = new IvParameterSpec(iv);

      // Create KeyGenerator for AES
      KeyGenerator keyGen = KeyGenerator.getInstance("AES");

      // Initialize KeyGenerator with a key size of 256 bits
      keyGen.init(256);

      // Generate a random AES key
      keyAES = keyGen.generateKey();

      // Convert the key to a hexadecimal String
      StringBuilder result = new StringBuilder();
      for (byte b : keyAES.getEncoded()) {
        result.append(String.format("%02X", b));
      }
      key = result.toString();

      result = null;

      // Print the generated key
    } catch (Exception e) {
      System.exit(255);
    }

    if (args.length < 2) {
      System.out.println("Usage: setup <logfile pathname>");
      System.exit(1);
    } else if (args.length > 2) {
      System.out.println("invalid");
      System.exit(255);
    }

    /* add your code here */
    if (!(args[0].equals("-N"))) {
      System.out.println("invalid");
      System.exit(255);
    } else {
      gbName = args[1];
      if (!(file_test(gbName))) {
        try {
          FileOutputStream fos = new FileOutputStream(gbName);
          Gradebook gb = new Gradebook();

          byte[] encGB = encryptObject("AES", gb, keyAES, ivspec);

          fos.write(encGB);
          fos.close();

        } catch (Exception ex) {
          // System.out.println(ex);
          System.out.println("invalid");
          System.exit(255);
        }
      } else {
        System.out.println("invalid");
        System.exit(255);
      }
    }
    System.out.println("Key is: " + key);
    return;
  }

  public static byte[] encryptObject(String algorithm, Serializable object,
      SecretKey key, IvParameterSpec iv) throws NoSuchPaddingException,
      NoSuchAlgorithmException, InvalidAlgorithmParameterException,
      InvalidKeyException, IOException, IllegalBlockSizeException {

    Cipher cipher = Cipher.getInstance(algorithm);
    cipher.init(Cipher.ENCRYPT_MODE, key, iv);

    SealedObject sealedObject = new SealedObject(object, cipher);

    // Serialize IV and SealedObject
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

    // Write IV
    objectOutputStream.writeObject(iv);

    // Write SealedObject
    objectOutputStream.writeObject(sealedObject);

    objectOutputStream.close();

    // Get the combined byte array
    return outputStream.toByteArray();
  }
}

