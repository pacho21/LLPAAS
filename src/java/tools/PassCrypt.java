/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author admin
 */
public class PassCrypt {
    
    /**
     * Method to create a personal sha1 encription.
     * Do not change after data is inside the server, otherwhise passwords will not match.
     * @param input
     * @return
     * @throws NoSuchAlgorithmException 
     */
    public String encrypt(String input) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA-1");
        String strong = "2pac" + input + "is" + input + "dead";
        byte[] result = mDigest.digest(strong.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
    
}
