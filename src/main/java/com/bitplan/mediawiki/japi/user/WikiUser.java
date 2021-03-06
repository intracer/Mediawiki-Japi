/**
 * Copyright (C) 2015 BITPlan GmbH
 *
 * Pater-Delp-Str. 1
 * D-47877 Willich-Schiefbahn
 *
 * http://www.bitplan.com
 * 
 * This source is part of
 * https://github.com/WolfgangFahl/Mediawiki-Japi
 * and the license for Mediawiki-Japi applies
 * 
 */
package com.bitplan.mediawiki.japi.user;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Properties;
import java.util.logging.Level;

/**
 * Wiki User information
 * 
 * @author wf
 *
 */
public class WikiUser {

	/**
	 * Logging
	 */
	protected static java.util.logging.Logger LOGGER = java.util.logging.Logger
			.getLogger("com.bitplan.mediawiki.japi.user");

	String username;
	String password;
	String email;

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *          the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * get input from standard in
	 * @param name
	 * @param br - the buffered reader to read from
	 * @return the input returned
	 * @throws IOException 
	 */
	public static String getInput(String name, BufferedReader br) throws IOException {
		// prompt the user to enter the given name
		System.out.print("Please Enter " + name + ": ");

		String value = br.readLine();
		return value;
	}

	/**
	 * get the property file for the given wiki
	 * @param wikiId
	 * @return the property File
	 */
	public static File getPropertyFile(String wikiId) {
		String user = System.getProperty("user.name");
		String userPropertiesFileName = System.getProperty("user.home") + "/.mediawiki-japi/"
				+ user + "_" + wikiId + ".ini";
		File propFile = new File(userPropertiesFileName);
		return propFile;
	}
	
	/**
	 * get the Wiki user for the given wikiid
	 * 
	 * @param wikiId - the id of the wiki
	 * @param siteurl - the siteurl
	 * @return a Wikiuser for this site
	 */
	public static WikiUser getUser(String wikiId,String siteurl) {
    File propFile=getPropertyFile(wikiId);
		Properties props = new Properties();
		WikiUser result =null;
		try {
			props.load(new FileReader(propFile));
			result = new WikiUser();
			result.setUsername(props.getProperty("user"));
			result.setEmail(props.getProperty("email"));
			Crypt pcf = new Crypt(props.getProperty("cypher"),
					props.getProperty("salt"));
			result.setPassword(pcf.decrypt(props.getProperty("secret")));
		} catch (FileNotFoundException e) {
			String msg=help(wikiId,siteurl);
			LOGGER.log(Level.SEVERE, msg);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		} catch (GeneralSecurityException e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		}
		return result;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *          the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *          the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * create a credentials ini file from the command line
	 */
	public static void createIniFile(String wikiid) {
		try {
		  // open up standard input
	    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			if (wikiid==null)
				wikiid=getInput("wiki id",br);
			File propFile=getPropertyFile(wikiid);
			String username = getInput("username",br);
			String password = getInput("password",br);
			String email=getInput("email",br);
			String remember= getInput("shall i store "+username+"'s credentials encrypted in "+propFile.getName()+" y/n?",br);
			if (remember.trim().toLowerCase().startsWith("y")) {
				Crypt lCrypt=Crypt.getRandomCrypt();
				Properties props = new Properties();
				props.setProperty("cypher", lCrypt.getCypher());
				props.setProperty("salt", lCrypt.getSalt());
				props.setProperty("user", username);
				props.setProperty("email",email);
				props.setProperty("secret", lCrypt.encrypt(password));
				if (!propFile.getParentFile().exists()) {
					propFile.getParentFile().mkdirs();
				}
				FileOutputStream propsStream=new FileOutputStream(propFile);
				props.store(propsStream, "Mediawiki JAPI credentials for "+wikiid);
				propsStream.close();
			}
		} catch (IOException e1) {
			LOGGER.log(Level.SEVERE,e1.getMessage());
		} catch (GeneralSecurityException e1) {
			LOGGER.log(Level.SEVERE,e1.getMessage());
		}
	}

	/**
	 * help text
	 * @param wikiId
	 * @param siteurl
	 * @return - the help text
	 */
	public static String help(String wikiId, String siteurl) {
		File propFile=getPropertyFile(wikiId);
		String help="Need to be able to read Credentials for \n\t"+siteurl+"\nfrom "
				+ propFile.getPath()+"\n";
		help+="Please run \n";
		help+="\tjava -cp target/test-classes com.bitplan.mediawiki.japi.user.WikiUser "+wikiId+"\n";
		help+="to create it. Then restart your tests.";
		return help;
	}

	
	/**
	 * main program
	 * @param args
	 */
	public static void main(String args[]) {
		if (args.length==0)
			createIniFile(null);
		else
		  createIniFile(args[0]);
	}


}
