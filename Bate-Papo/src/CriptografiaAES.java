import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class CriptografiaAES {
    private static final String CHAVE = "1234567890123456"; // 16 caracteres

    public static String criptografar(String texto) throws Exception {
        SecretKeySpec chave = new SecretKeySpec(CHAVE.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, chave);
        byte[] criptografado = cipher.doFinal(texto.getBytes());
        return Base64.getEncoder().encodeToString(criptografado);
    }

    public static String descriptografar(String textoCriptografado) throws Exception {
        SecretKeySpec chave = new SecretKeySpec(CHAVE.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, chave);
        byte[] decodificado = Base64.getDecoder().decode(textoCriptografado);
        byte[] descriptografado = cipher.doFinal(decodificado);
        return new String(descriptografado);
    }
}