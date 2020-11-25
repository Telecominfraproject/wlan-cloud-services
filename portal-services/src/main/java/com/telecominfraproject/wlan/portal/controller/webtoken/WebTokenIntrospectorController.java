package com.telecominfraproject.wlan.portal.controller.webtoken;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.codec.digest.Crypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.telecominfraproject.wlan.core.model.role.PortalUserRole;
import com.telecominfraproject.wlan.core.model.webtoken.IntrospectWebTokenResult;
import com.telecominfraproject.wlan.core.model.webtoken.RefreshWebTokenRequest;
import com.telecominfraproject.wlan.core.model.webtoken.WebTokenRequest;
import com.telecominfraproject.wlan.core.model.webtoken.WebTokenResult;
import com.telecominfraproject.wlan.portaluser.PortalUserServiceInterface;
import com.telecominfraproject.wlan.portaluser.models.PortalUser;

/**
 * @author dtoptygin
 * THIS CLASS EXISTS ONLY FOR INTEGRATION TESTING AND TRIALS
 * Recommended solution is to use a 3rd-party authentication service, like Auth0
 */
@RestController
@RequestMapping(value = "/management/v1/oauth2")
public class WebTokenIntrospectorController {

    private static final Logger LOG = LoggerFactory.getLogger(WebTokenIntrospectorController.class);

    private static final long accessTokenExpiryMs = Long.getLong("tip.wlan.AccessTokenExpiryMs", 3000000);
    private static final long refreshTokenExpiryMs = Long.getLong("tip.wlan.RefreshTokenExpiryMs", 3600000);
    
    private static final int customerIdForWebToken = Integer.getInteger("tip.wlan.webtokenCustomerId", 2);
    /** For the format of the Salt @see Crypt.crypt()
    * SHA-512 salts start with {@code $6$} and are up to 16 chars long. 
    * The chars in the salt string are drawn from the set {@code [a-zA-Z0-9./]}.
    */
    private static final String saltForTheSignature = System.getProperty("tip.wlan.saltForTheSignature", "$6$V9DcGMV/");
    
    private static final String superUserName = System.getProperty("tip.wlan.superUserName", "support@example.com");
    private static final String superUserPassword = System.getProperty("tip.wlan.superUserPassword", "support");
    /** For the format of the Salt @see Crypt.crypt()
    * SHA-512 salts start with {@code $6$} and are up to 16 chars long. 
    * The chars in the salt string are drawn from the set {@code [a-zA-Z0-9./]}.
    */
    private static final String saltForThePasswords = System.getProperty("tip.wlan.saltForThePasswords", "$6$V3ZcYJP/");
    

    @Autowired
    private PortalUserServiceInterface portalUserServiceInterface;

    @RequestMapping(value = "/introspecttoken", method = RequestMethod.POST, consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public IntrospectWebTokenResult introspectToken(@RequestParam Map<String, String> body){

        String requestUrl = body.get("requestUrl");
        String token = body.get("token");
        @SuppressWarnings("unused")
        String tokenTypeHint = body.get("tokenTypeHint");
        @SuppressWarnings("unused")
        String requestMethod = body.get("requestMethod");


        IntrospectWebTokenResult ret = new IntrospectWebTokenResult();

        try{
            String accessToken = decodeAndVerify(token);
            long incomingExpiryTimeMs = extractExpiryTime(accessToken);

            if(incomingExpiryTimeMs < System.currentTimeMillis()){
                //setting this to false would result in status code 403
                ret.setActive(false);
                ret.setErrorCode(403);
            } else {
                ret.setActive(true);
                ret.setErrorCode(200);
            }

        } catch (Exception e){
            LOG.error("Cannot validate access token {}", token, e);
            ret.setActive(false);
            ret.setErrorCode(401);
        }

        LOG.debug("introspectToken {} : {}", requestUrl, ret);

        return ret;
    }

    private static String decodeAndVerify(String encodedToken) {
        if(encodedToken==null || encodedToken.indexOf('.')<0) {
            throw new IllegalArgumentException("Invalid token format");
        }

        String tokenWithoutSignature = encodedToken.substring(0, encodedToken.indexOf('.'));
        String signature = saltForTheSignature + "$" +encodedToken.substring(encodedToken.indexOf('.') + 1);
        String ret = new String(Base64Utils.decodeFromString(tokenWithoutSignature));

        //verify the signature
        if( !( signature.equals(Crypt.crypt(ret, saltForTheSignature)) ) ) {
            throw new IllegalArgumentException("Invalid token signature");
        }

        return ret;
    }

    private static String encodeAndSign(String plainToken) {
        String ret = null;
        try {
            ret = Base64Utils.encodeToString(plainToken.getBytes("UTF-8")) + "." + Crypt.crypt(plainToken, saltForTheSignature).substring(saltForTheSignature.length()+1);
        } catch (UnsupportedEncodingException e) {
            LOG.error("Cannot encode token", e);
        }

        return ret;
    }

    private static long extractExpiryTime(String decodedToken){
        int startPos = decodedToken.indexOf("\"expiryTime\":") + "\"expiryTime\":".length();
        int endPos = decodedToken.indexOf(',',startPos+1);
        if(endPos<0){
            endPos = decodedToken.indexOf('}',startPos+1);
        }

        long expiryTimeMs = Long.parseLong(decodedToken.substring(startPos, endPos));
        return expiryTimeMs;

    }

    private static String extractUserName(String decodedToken){
        int startPos = decodedToken.indexOf("\"userName\":\"") + "\"userName\":\"".length();
        int endPos = decodedToken.indexOf('"',startPos+1);
        if(endPos<0){
            endPos = decodedToken.length();
        }

        return decodedToken.substring(startPos, endPos);
    }

    private static String extractUserRole(String decodedToken){
        int startPos = decodedToken.indexOf("\"userRole\":\"") + "\"userRole\":\"".length();
        int endPos = decodedToken.indexOf('"',startPos+1);
        if(endPos<0){
            endPos = decodedToken.length();
        }

        return decodedToken.substring(startPos, endPos);
    }

    
    private static long extractUserId(String decodedToken) {
        int startPos = decodedToken.indexOf("\"userId\":") + "\"userId\":".length();
        int endPos = decodedToken.indexOf(',',startPos+1);
        if(endPos<0){
            endPos = decodedToken.indexOf('}',startPos+1);
        }

        long userId = Long.parseLong(decodedToken.substring(startPos, endPos));
        return userId;
	}

    private static boolean verifyPassword(String incomingPassword, String storedPassword) {
        if(incomingPassword==null || incomingPassword.isEmpty()) {
        	return false;
        }

        //verify the password
        return (saltForThePasswords + '$' +  storedPassword).equals(Crypt.crypt(incomingPassword, saltForThePasswords)) ;
    }

    @RequestMapping(value = "/token", method = RequestMethod.POST)
    public WebTokenResult token(@RequestBody WebTokenRequest body){

    	PortalUser portalUser = null;
    	
        if(superUserName.equals(body.getUserId())){
        	 if(!superUserPassword.equals(body.getPassword())){
        		 throw new BadCredentialsException("Unknown user or password");
        	 }
        	 
        	 portalUser = new PortalUser();
        	 portalUser.setUsername(superUserName);
        	 portalUser.setRoles(Arrays.asList(PortalUserRole.SuperUser));
        } else {
        	//lookup the portal user with this username
        	portalUser = portalUserServiceInterface.getByUsernameOrNull(customerIdForWebToken, body.getUserId());
            if(portalUser == null) {
            	throw new BadCredentialsException("Unknown user or password");
            }
            
        	//verify the password for the user
            if(!verifyPassword(body.getPassword(), portalUser.getPassword())) {
            	throw new BadCredentialsException("Unknown user or password");
            }

        }
        
        	
        WebTokenResult ret = new WebTokenResult();
        ret.setToken_type("Bearer");
        ret.setExpires_in((int) (accessTokenExpiryMs/1000));
        ret.setIdle_timeout((int) (refreshTokenExpiryMs/1000));

        String accessToken = createAccessToken(portalUser);
        ret.setAccess_token(accessToken);
        String refreshToken = createRefreshToken(portalUser);
        ret.setRefresh_token(refreshToken);

        LOG.debug("token {}", ret);

        return ret;
    }


    public static void main(String[] args) {
        String uuid = UUID.randomUUID().toString();
        System.out.println("UUID="+uuid);
        PortalUser portalUser = new PortalUser();
	   	portalUser.setUsername(superUserName);
	   	portalUser.setRoles(Arrays.asList(PortalUserRole.SuperUser));
	   	portalUser.setId(42);

        String token = createAccessToken(portalUser);

        System.out.println(token);
        System.out.println(decodeAndVerify(token));

        String decodedToken = decodeAndVerify(token);
        System.out.println("Expiry time: " + extractExpiryTime(decodedToken));

        System.out.println("User name: " + extractUserName(decodedToken));
        System.out.println("User role: " + extractUserRole(decodedToken));
        System.out.println("User id: " + extractUserId(decodedToken));

        String externalToken = "eyJpc3MiOiJ0aXAiLCJqdGkiOiI0MTBjMDQ0Mi1kZTI3LTRhZTQtODNmZi1hNWFmNDZhNzY2OWEiLCJleHBpcnlUaW1lIjoxNTg5MjE0MjU2NjQwLCJjdXN0b21lcklkIjoyLCJ1c2VyTmFtZSI6InN1cHBvcnRAZXhhbXBsZS5jb20iLCJ1c2VySWQiOjAsInVzZXJSb2xlIjoiU3VwZXJVc2VyIn0=.EZpL05pY3U8rj2PvkYiRXl7MLY9LdF9BQGmUXD/b6iKrh.PangHsJBt6kAXJywW2BLw2yt4P34WuRaDcmUfnY0";
        System.out.println("External token: " + decodeAndVerify(externalToken));

        //Now check refresh token
        String refreshToken = createRefreshToken(portalUser);

        System.out.println(refreshToken);
        System.out.println(decodeAndVerify(refreshToken));

        String decodedRefreshToken = decodeAndVerify(refreshToken);
        System.out.println("Refresh Expiry time: " + extractExpiryTime(decodedRefreshToken));

        System.out.println("Refresh User name: " + extractUserName(decodedRefreshToken));
        System.out.println("Refresh User role: " + extractUserRole(decodedRefreshToken));
        System.out.println("Refresh User id: " + extractUserId(decodedRefreshToken));
        
        //pasword handling
        String incomingPassword = "mypassword";
        String saltedPassword = Crypt.crypt(incomingPassword, saltForThePasswords);
        String pwdWithoutSalt = saltedPassword.substring(saltForThePasswords.length()+1);
        
        System.out.println(saltedPassword);
        System.out.println(pwdWithoutSalt);       
        System.out.println("Password match: " + verifyPassword(incomingPassword, pwdWithoutSalt));
    }

    @RequestMapping(value = "/refreshToken", method = RequestMethod.POST)
    public WebTokenResult refreshToken(@RequestBody RefreshWebTokenRequest body){

        String incomingRefreshToken = decodeAndVerify(body.getRefreshToken());
        if(!incomingRefreshToken.contains("\"refresh\":true")){
            throw new BadCredentialsException("Invalid refresh token");
        }

        String decodedToken = incomingRefreshToken;
        long incomingExpiryTimeMs = extractExpiryTime(decodedToken);

        if(incomingExpiryTimeMs < System.currentTimeMillis()){
            throw new CredentialsExpiredException("Refresh token expired");
        }

        String userName = extractUserName(decodedToken);
        String userRole = extractUserRole(decodedToken);
        long userId = extractUserId(decodedToken);
        PortalUser portalUser = new PortalUser();
        portalUser.setUsername(userName);
        portalUser.setId(userId);
        portalUser.setRoles(Arrays.asList(PortalUserRole.valueOf(userRole)));
        
        //create new accessToken and refreshToken

        WebTokenResult ret = new WebTokenResult();
        ret.setToken_type("Bearer");
        ret.setExpires_in((int) (accessTokenExpiryMs/1000));
        ret.setIdle_timeout((int) (refreshTokenExpiryMs/1000));

        String accessToken = createAccessToken(portalUser);
        ret.setAccess_token(accessToken);
        String refreshToken = createRefreshToken(portalUser);
        ret.setRefresh_token(refreshToken);

        LOG.debug("refreshToken {}", ret);

        return ret;
    }

	private static String createAccessToken(PortalUser portalUser) {
        String accessToken = encodeAndSign("{\"iss\":\"tip\",\"jti\":\""+UUID.randomUUID()
        	+"\",\"expiryTime\":"+(System.currentTimeMillis() + accessTokenExpiryMs)
        	+",\"customerId\":"+customerIdForWebToken
        	+",\"userName\":\""+portalUser.getUsername()+"\""        	
        	+",\"userId\":"+portalUser.getId()        	
        	+",\"userRole\":\""+portalUser.getRoles()+"\""        	
        	+"}" );
        return accessToken;
    }

    private static String createRefreshToken(PortalUser portalUser) {
        String refreshToken = encodeAndSign("{\"iss\":\"tip\",\"jti\":\""+UUID.randomUUID()
        	+"\",\"expiryTime\":"+(System.currentTimeMillis() + refreshTokenExpiryMs)
        	+",\"customerId\":"+customerIdForWebToken
        	+",\"userName\":\""+portalUser.getUsername()+"\""        	
        	+",\"userId\":"+portalUser.getId()        	
        	+",\"userRole\":\""+portalUser.getRoles()+"\""        	
        	+",\"refresh\":true}");
        return refreshToken;
    }

}
