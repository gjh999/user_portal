/**
 *
 */
package egovframework.example.cmmn.web;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

/**
 * <b>공공 마이데이터 유통체계 구축 사업</b><br>
 * ConvertUtil
 *
 * @author			: LJY
 * @since			: 2020. 10. 26.
 * @modifier 		: GJH
 * @modified 		: 2024.  1. 22.
 * @modification
 * @
 * @ 수정일                    수정자       	수정내용
 * @ -------------	---------	---------------------
 * @ 2020. 10. 26.	LJY	               최초 생성
 * @ 2024.  1. 22.	GJH	        [자체개선] 개발가이드 주석정리 테스트
 */

@Component
public class JwtUtil {
	
	//private final SecretKey jwtSecretKey;
	
	// 테스트
	
	//private final SecretKey jwtSecretKey;

    /*private SecretKey getSecretKey() {
        return jwtSecretKey;
    }*/
	
	/*public static String genToken(Map<String, Object> claims, int seconds) {
        long now = new Date().getTime();
        Date accessTokenExpiresIn = new Date(now + 1000L * seconds);
        
        String secretKey = "T1wSKZ1bhr8WNv4xLYpSqSQWOy0pEk1f";

				// JWT build
        return Jwts.builder()
                .claim("body", ConvertUtil.convertMapToJsonString(claims))
                .setExpiration(accessTokenExpiresIn)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }*/
	
	String secretKey = "T1wSKZ1bhr8WNv4xLYpSqSQWOy0pEk1f";
	
    public String createToken(Long id, String nickname, String socialAccessToken, int expMinutes, String tokenType) {
        Date accessTokenExp = new Date(System.currentTimeMillis() + (60000 * expMinutes));

        String createdToken = JWT.create()
                .withSubject(String.valueOf(id))
                .withClaim("nickname", nickname)
                .withClaim("socialAccessToken", socialAccessToken)
                .withClaim("tokenType", tokenType)
                .withExpiresAt(accessTokenExp)
                .sign(Algorithm.HMAC512(secretKey));

        return createdToken;
    }
    
    /*int accessTokenExpMinutes = 60; // 60분
    int refreshTokenExpMinutes = 60; // 60분
    
    public Jwt createTokens(Long id, String nickname, String socialAccessToken) {
        String accessToken = createToken(id, nickname, socialAccessToken, accessTokenExpMinutes, "accessToken");
        String refreshToken = createToken(id, nickname, socialAccessToken, refreshTokenExpMinutes, "refreshToken");
        

        return Jwt.builder().
                accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExp(new Date(System.currentTimeMillis() + 60000 * accessTokenExpMinutes))
                .refreshTokenExp(new Date(System.currentTimeMillis() + 60000 * refreshTokenExpMinutes))
                .build();
    }*/

}
