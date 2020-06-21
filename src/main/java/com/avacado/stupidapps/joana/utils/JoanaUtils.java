package com.avacado.stupidapps.joana.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import com.avacado.stupidapps.joana.exceptions.JoanaException;

public class JoanaUtils
{
  
  private static Encoder encoder = Base64.getUrlEncoder().withoutPadding();
  private static Logger logger = LoggerFactory.getLogger(JoanaUtils.class);

  public static String generateSecureUserToken(String email) {
    try
    {
      MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
      messageDigest.update(email.getBytes("UTF-8"));
      messageDigest.update(String.valueOf(new Date().getTime()).getBytes("UTF-8"));
      messageDigest.update(generateSecureRandomString(8).getBytes("UTF-8"));
      
      byte[] digest = messageDigest.digest();           
      StringBuffer hexString = new StringBuffer();
      for (int i = 0;i<digest.length;i++) {
         hexString.append(Integer.toHexString(0xFF & digest[i]));
      }
      
      return hexString.toString();
      
    }
    catch (NoSuchAlgorithmException | UnsupportedEncodingException e)
    {
      logger.error("Mesage digest token generation failed");
      throw new JoanaException("Failed to generate token", HttpStatus.INTERNAL_SERVER_ERROR, e.getStackTrace());
    }
  }
  
  public static String generateSecureRandomString(int byteSize) {
    SecureRandom random = new SecureRandom();
    byte bytes[] = new byte[byteSize];
    random.nextBytes(bytes);
    return encoder.encodeToString(bytes);
  }

}
