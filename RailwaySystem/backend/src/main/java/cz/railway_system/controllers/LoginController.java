package cz.railway_system.controllers;

import static java.lang.System.currentTimeMillis;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cz.railway_system.pojo.AuthenticationRequest;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api")
public class LoginController {
	
	@Autowired
	private String secretKey;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	
	/**
	 * Ověření přihlašovacích údajů + vygenerování JWT
	 * 
	 * @param authenticationRequest - přihlašovací údaje
	 * @param response
	 * 
	 * @return - vrací JWT + čas vypršení
	 */
	@PostMapping("/authenticate")
	public ResponseEntity<Object> authenticate(@RequestBody AuthenticationRequest authenticationRequest,
											   HttpServletResponse response) {
		
		Authentication authentication = new UsernamePasswordAuthenticationToken(
				authenticationRequest.getUsername(), authenticationRequest.getPassword()
		);
		
		// Ověření přihlášení
		authentication = authenticationManager.authenticate(authentication);
		
		// Čas vypršení - 60 min 
		Date expireTime = new Date(currentTimeMillis() + 60 * 60 * 1000);
		
		// Vygenerování JWT
		String token = Jwts.builder()
			.setSubject(authentication.getName())
			.claim("authorities", authentication.getAuthorities())
			.setIssuedAt(new Date())
			.setExpiration(expireTime)
			.signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
			.compact();
		
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("token", token);
		body.put("expireTime", expireTime);
		
		return new ResponseEntity<>(body, HttpStatus.OK);
	}
	
}
