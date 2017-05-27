package project.tools;

import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.crypto.hash.Sha256Hash;

/**
 * Created by mariosk89 on 20-Mar-16.
 */
public class PasswordEncryptor {

    public static String encrypt(String originalPassword)
    {
        return new Sha256Hash(originalPassword).toHex();
    }

    public static String encryptWithSalt(String originalPassword, Object salt)
    {
        return new Sha256Hash(originalPassword, salt).toHex();
    }
}
