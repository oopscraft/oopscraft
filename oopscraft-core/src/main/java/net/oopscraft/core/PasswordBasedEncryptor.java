package net.oopscraft.core;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;

public class PasswordBasedEncryptor {

	private static final String ALGORITHM = "PBEWithMD5AndDES";
    private static final String ENCRYPT_IDENTIFIER = "(ENC\\()([^)]{1,})(\\))";
    private static final String DEFAULT_PASSWORD = new String(Base64.getDecoder().decode("MjZDNUNCRDYzNDNBNDVCQTgyNDIwQkM4NUYwMEMxMkQ="),StandardCharsets.UTF_8);
    
    private EnvironmentStringPBEConfig config = new EnvironmentStringPBEConfig();
    private StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
    
    /**
     * constructor with default password key
     */
    public PasswordBasedEncryptor() {
        config.setAlgorithm(ALGORITHM);
        encryptor.setConfig(config);
        encryptor.setPassword(DEFAULT_PASSWORD);
    }
    
    /**
     * constructor with custom password key
     * @param password
     * @throws Exception
     */
    public PasswordBasedEncryptor(String password) {
    	super();
        encryptor.setPassword(password);
    }
    
    /**
     * Encrypts string
     * @param decryptedString
     * @return
     * @throws Exception
     */
    public String encrypt(String decryptedString) throws Exception {
        return encryptor.encrypt(decryptedString);
    }
    
    /**
     * Decrypts string 
     * @param encryptedString
     * @return
     * @throws Exception
     */
    public String decrypt(String encryptedString) throws Exception {
        return encryptor.decrypt(encryptedString);
    }
    
    /**
     * encryptIdentifiedValue
     * @param decryptedString
     * @return
     * @throws Exception
     */
    public String encryptIdentifiedValue(String decryptedString) throws Exception {
        Pattern p = Pattern.compile(ENCRYPT_IDENTIFIER);
        Matcher m = p.matcher(decryptedString);
        StringBuffer sb = new StringBuffer();
        while(m.find()) {
            String decryptedValue = m.group(2);
            String encryptedValue = encrypt(decryptedValue);
            m.appendReplacement(sb, m.group(1) + Matcher.quoteReplacement(encryptedValue) + m.group(3));
        }
        m.appendTail(sb);
        return sb.toString();
    }
    
    /**
     * decryptIdentifiedValue
     * @param encryptedString
     * @return
     * @throws Exception
     */
    public String decryptIdentifiedValue(String encryptedString) throws Exception {
        Pattern p = Pattern.compile(ENCRYPT_IDENTIFIER);
        Matcher m = p.matcher(encryptedString);
        StringBuffer sb = new StringBuffer();
        while(m.find()) {
            String encryptedValue = m.group(2);
            String decryptedValue = decrypt(encryptedValue);
            m.appendReplacement(sb, Matcher.quoteReplacement(decryptedValue));
        }
        m.appendTail(sb);
        return sb.toString();
    }
    
    /**
     * main
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
    	try {
	        System.out.print("Please enter 1.Encryption or 2.Decryption:");	// NOPMD
	        @SuppressWarnings("resource")
	        Scanner scanner = new Scanner(System.in);
	        String type = scanner.nextLine();
	        PasswordBasedEncryptor passwordBasedEncryptor = new PasswordBasedEncryptor();
	        if("1".equals(type)) {
	            System.out.print("enter original value:");	// NOPMD
	            String value = scanner.nextLine();
	            String encrypted = passwordBasedEncryptor.encryptIdentifiedValue(value);
	            System.out.println(" encrypted:" + encrypted);	// NOPMD
	            String decrypted = passwordBasedEncryptor.decryptIdentifiedValue(encrypted);
	            System.out.println(" decrypted:" +  decrypted);	// NOPMD
	        }
	        else if("2".equals(type)) {
	            System.out.print("enter encrypted value:");	// NOPMD
	            String value = scanner.nextLine();
	            String decrypted = passwordBasedEncryptor.decryptIdentifiedValue(value);
	            System.out.println(" decrypted:" + decrypted);	// NOPMD
	        }
	        System.exit(0);
    	}catch(Exception e) {
    		e.printStackTrace(System.err);
    		System.exit(-1);
    	}
    }

}
