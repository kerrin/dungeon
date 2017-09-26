package org.kerrin.dungeon.utils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

public class ShaPasswordEncoder implements PasswordEncoder {
	private static final Logger logger = LoggerFactory.getLogger(ShaPasswordEncoder.class);
	
	// The higher the number of iterations the more 
    // expensive computing the hash is for us and
    // also for an attacker.
    private static final int iterations = 20*1000;
    private static final int saltLen = 32;
    private static final int desiredKeyLen = 256;

    // using PBKDF2 from Sun, an alternative is https://github.com/wg/scrypt
    // cf. http://www.unlimitednovelty.com/2012/03/dont-use-bcrypt.html
    public static String hash(String password, byte[] salt) throws Exception {
        if (password == null || password.length() == 0)
            throw new IllegalArgumentException("Empty passwords are not supported.");
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        SecretKey key = f.generateSecret(new PBEKeySpec(
            password.toCharArray(), salt, iterations, desiredKeyLen)
        );
        return DatatypeConverter.printBase64Binary(key.getEncoded());
    }

    /** Computes a salted PBKDF2 hash of given plaintext password
    	suitable for storing in a database. 
    	Empty passwords are not supported. */
	@Override
	public String encode(CharSequence rawPassword) {
		byte[] salt;
		try {
			salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(saltLen);
		} catch (NoSuchAlgorithmException e) {
			logger.error("Can't find SHA1PRNG encryption!");
			e.printStackTrace();
			return null;
		}
        // store the salt with the password
        try {
			return DatatypeConverter.printBase64Binary(salt) + "$" + hash(rawPassword.toString(), salt);
		} catch (Exception e) {
			logger.error("Error creating password");
			e.printStackTrace();
			return null;
		}
	}

	/** Checks whether given plaintext password corresponds 
    	to a stored salted hash of the password. */
	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		String[] saltAndPass = encodedPassword.split("\\$");
        if (saltAndPass.length != 2) {
        	logger.error("Could not split hash password and salt for: "+encodedPassword);
            throw new IllegalStateException(
                "The stored password have the form 'salt$hash'");
        }
        String hashOfInput;
		try {
			hashOfInput = hash(rawPassword.toString(), DatatypeConverter.parseBase64Binary(saltAndPass[0]));
		} catch (Exception e) {
			logger.error("Error checking password");
			e.printStackTrace();
			return false;
		}
        logger.trace("Password: "+rawPassword+"=>"+hashOfInput+"=="+saltAndPass[1]);
        return hashOfInput.equals(saltAndPass[1]);
	}
}
