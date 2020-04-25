package com.telecominfraproject.wlan.portal.controller.webtoken;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.codec.digest.Crypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.telecominfraproject.wlan.core.model.webtoken.IntrospectWebTokenResult;
import com.telecominfraproject.wlan.core.model.webtoken.RefreshWebTokenRequest;
import com.telecominfraproject.wlan.core.model.webtoken.WebTokenRequest;
import com.telecominfraproject.wlan.core.model.webtoken.WebTokenResult;

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
    
    private static final long customerIdForWebToken = Long.getLong("tip.wlan.webtokenCustomerId", 2);
    /** For the format of the Salt @see Crypt.crypt()
    * SHA-512 salts start with {@code $6$} and are up to 16 chars long. 
    * The chars in the salt string are drawn from the set {@code [a-zA-Z0-9./]}.
    */
    private static final String saltForTheSignature = System.getProperty("tip.wlan.saltForTheSignature", "$6$V9DcGMV/");


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

    @RequestMapping(value = "/token", method = RequestMethod.POST)
    public WebTokenResult token(@RequestBody WebTokenRequest body){

        if(!"support@example.com".equals(body.getUserId()) || !"support".equals(body.getPassword())){
            throw new BadCredentialsException("Unknown user or password");
        }

        WebTokenResult ret = new WebTokenResult();
        ret.setToken_type("Bearer");
        ret.setExpires_in((int) (accessTokenExpiryMs/1000));
        ret.setIdle_timeout((int) (refreshTokenExpiryMs/1000));

        String accessToken = createAccessToken();
        ret.setAccess_token(accessToken);
        String refreshToken = createRefreshToken();
        ret.setRefresh_token(refreshToken);

        LOG.debug("token {}", ret);

        return ret;
    }


    public static void main(String[] args) {
        String uuid = UUID.randomUUID().toString();
        System.out.println("UUID="+uuid);
        String token = createAccessToken();

        System.out.println(token);
        System.out.println(decodeAndVerify(token));

        String decodedToken = decodeAndVerify(token);
        System.out.println(extractExpiryTime(decodedToken));
        
        String externalToken = "eyJpc3MiOiJ0aXAiLCJqdGkiOiI4M2ZkYWJjZS04MjhiLTQxM2UtYTEwMi1mZjlkMGNhM2U2NTciLCJleHBpcnlUaW1lIjoxNTg3NTgxNjAzNjg5LCJjdXN0b21lcklkIjoyfQ==.IPUCQtXrzL4UuAI9bgEfFvINNh0EMHVzt6B4OPmxCA1yQYTGObLpgLVbJK1/kdqgtZn3UdsAYWXoawvp6cthx.";
        System.out.println(decodeAndVerify(externalToken));
        
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

        //create new accessToken and refreshToken

        WebTokenResult ret = new WebTokenResult();
        ret.setToken_type("Bearer");
        ret.setExpires_in((int) (accessTokenExpiryMs/1000));
        ret.setIdle_timeout((int) (refreshTokenExpiryMs/1000));

        String accessToken = createAccessToken();
        ret.setAccess_token(accessToken);
        String refreshToken = createRefreshToken();
        ret.setRefresh_token(refreshToken);

        LOG.debug("refreshToken {}", ret);

        return ret;
    }
    
    private static String createAccessToken() {
        String accessToken = encodeAndSign("{\"iss\":\"tip\",\"jti\":\""+UUID.randomUUID()+"\",\"expiryTime\":"+(System.currentTimeMillis() + accessTokenExpiryMs)+",\"customerId\":"+customerIdForWebToken+"}" );
        return accessToken;
    }

    private static String createRefreshToken() {
        String refreshToken = encodeAndSign("{\"iss\":\"tip\",\"jti\":\""+UUID.randomUUID()+"\",\"expiryTime\":"+(System.currentTimeMillis() + refreshTokenExpiryMs)+",\"refresh\":true}");
        return refreshToken;
    }

}
