package cz.railway_system.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import cz.railway_system.pojo.AuthenticationRequest;

@WebMvcTest(StationsController.class)
public class LoginControllerTest {

	private AuthenticationRequest authenticationRequest;
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private AuthenticationManager authenticationManager;
	
	
	/**
	 * Inicializace parametrů
	 */
	@BeforeEach
	public void setUp() {
		
		authenticationRequest = new AuthenticationRequest();
	}
	
	
	/**
	 * Test metody pro ověření přihlašovacích údajů
	 * 
	 * @throws Exception
	 */
	@Test
	public void authenticate() throws Exception {
		
		// Nastavení testovacího uživatele
		authenticationRequest.setUsername("username");
		authenticationRequest.setPassword("password");
		
		Authentication authentication = new UsernamePasswordAuthenticationToken(
				authenticationRequest.getUsername(), authenticationRequest.getPassword()
		);
		
		when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);
		
		// Porovnání výstupních hodnot
		mockMvc.perform(post("/api/authenticate")
			.contentType(MediaType.APPLICATION_JSON)
			.content(new ObjectMapper().writeValueAsString(authenticationRequest)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.token").isString())
			.andExpect(jsonPath("$.expireTime").exists());
		
		verify(authenticationManager, times(1)).authenticate(any(Authentication.class));
	}
	
}
