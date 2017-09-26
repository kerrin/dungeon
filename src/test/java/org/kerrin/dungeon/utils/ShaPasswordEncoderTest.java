package org.kerrin.dungeon.utils;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

public class ShaPasswordEncoderTest {

	@Test
	public void test_getSaltedHash() {
		Date now = new Date();
		
		String testPassword = now.toString();
		String saltedHash = "";
		String saltedHash2 = "";
		ShaPasswordEncoder passwordEncoder = new ShaPasswordEncoder();
		
		try {
			saltedHash = passwordEncoder.encode(testPassword);
		} catch (Exception e) {
			fail(e.getMessage());
		}
		try {
			saltedHash2 = passwordEncoder.encode(testPassword+"a");
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertTrue(!saltedHash.equals(saltedHash2));
	}

	@Test
	public void test_check() {
		Date now = new Date();
		String testPassword = now.toString();
		ShaPasswordEncoder passwordEncoder = new ShaPasswordEncoder();
		
		String hashedPassword = "";
		try {
			hashedPassword = passwordEncoder.encode(testPassword);
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
		try {
			assertTrue(passwordEncoder.matches(testPassword, hashedPassword));
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void test_hash() {
		Date now = new Date();
		String testPassword = now.toString();
		String testPassword2 = now.toString()+"A";

		String hashPasswordAndSalt = "";
		String hashPasswordAndSalt2 = "";
		String hashPasswordAndSalt3 = "";
		String hashPasswordAndSalt4 = "";
		try {
			hashPasswordAndSalt = ShaPasswordEncoder.hash(testPassword, testPassword.getBytes());
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
		try {
			hashPasswordAndSalt2 = ShaPasswordEncoder.hash(testPassword2, testPassword.getBytes());
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
		try {
			hashPasswordAndSalt3 = ShaPasswordEncoder.hash(testPassword, testPassword2.getBytes());
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertTrue(!hashPasswordAndSalt.equals(hashPasswordAndSalt2));
		assertTrue(!hashPasswordAndSalt2.equals(hashPasswordAndSalt3));
		assertTrue(!hashPasswordAndSalt.equals(hashPasswordAndSalt3));
		
		try {
			hashPasswordAndSalt4 = ShaPasswordEncoder.hash(testPassword, testPassword.getBytes());
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertTrue(hashPasswordAndSalt.equals(hashPasswordAndSalt4));
	}
}
