import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

public class CriptografiaAES {
    public static byte[] gerarSalt() {
        byte[] salt = new byte[8];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    public static SecretKeySpec gerarChave(String senha, byte[] salt) throws Exception {
        KeySpec spec = new PBEKeySpec(senha.toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] chave = factory.generateSecret(spec).getEncoded();
        return new SecretKeySpec(chave, "AES");
    }

    public static String criptografar(String texto, SecretKeySpec chave) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, chave);
        byte[] criptografado = cipher.doFinal(texto.getBytes());
        return Base64.getEncoder().encodeToString(criptografado);
    }

    public static String descriptografar(String textoCriptografado, SecretKeySpec chave) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, chave);
        byte[] decodificado = Base64.getDecoder().decode(textoCriptografado);
        byte[] descriptografado = cipher.doFinal(decodificado);
        return new String(descriptografado);
    }
}