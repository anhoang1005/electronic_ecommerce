package com.example.ecommerce.utils;

import com.example.ecommerce.entities.enums.ErrorMessage;
import com.example.ecommerce.exceptions.BaseException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenUtils {
	
	@Value("${jwt.secretKeyString}")
	private String secretKeyString;
	
	public String generateAccessTokens(String username,Long userId, List<String> roles) {
		Date expirationDate = new Date(System.currentTimeMillis() + 1 * 24 * 3600 * 1000);
		SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKeyString));
		String jws = Jwts.builder()
				.subject(username)
				.expiration(expirationDate)
				.claim("roles", roles)
				.claim("userId",userId)
				.signWith(key)
				.compact();
		return jws;
	}

	public String generateRefreshTokens(String username, List<String> roles) {
		Date expirationDate = new Date(System.currentTimeMillis() + 3 * 24 * 3600 * 1000);
		SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKeyString));
		String jws = Jwts.builder()
				.subject(username)
				.expiration(expirationDate)
				.claim("roles", roles)
				.signWith(key)
				.compact();
		return jws;
	}

	
	public boolean verifyToken(String token) {
		boolean isVerify = false;
		try {
			SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKeyString));
			Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
			isVerify = true;
		} catch (Exception e) {
			isVerify = false;
		}
		System.out.println("Verify: " + isVerify);
		return isVerify;
	}
	public String getToken(){
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String authHeader = request.getHeader("Authorization");
		if(authHeader == null || !authHeader.startsWith("Bearer ")){
			throw new BaseException(ErrorMessage.UNAUTHORIZATION_ERROR.name());
		}

		return authHeader.substring(7);
	}
	
	@SuppressWarnings("unchecked")
	public List<String> extractClaims(String token) {
		SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKeyString));
		@SuppressWarnings("deprecation")
		Claims claims = Jwts.parser()
				.verifyWith(key).build().parseSignedClaims(token).getBody();
		return claims.get("roles", List.class);
    }
	
	public String extractUsername(String token) {
		SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKeyString));
		
		@SuppressWarnings("deprecation")
		String username = Jwts.parser()
							.verifyWith(key)
							.build()
							.parseSignedClaims(token).getBody().getSubject();
		return username;
	}
	public Long extractUserId(String token){
		SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKeyString));
		Claims claims = Jwts.parser()
				.verifyWith(key).build().parseSignedClaims(token).getBody();

		return claims.get("userId",Long.class);
	}
}
