package pii.util.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.exceptions.JWTVerificationException;

import pii.service.UserCredentialsService;


// TODO: tratar todas as excessões lançadas por essa classe
@Component
public class JWTFilter extends OncePerRequestFilter {
	
	@Autowired
	private UserCredentialsService userCredentialsService;
	
	@Autowired
	private JWTUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		var authHeader = request.getHeader("Authorization");
		
		if (authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")) {
			var jwt = authHeader.substring(7);
			
			if (jwt == null || jwt.isBlank()) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Token inválido.");
			} else {
				try {
					var email = jwtUtil.validateTokenAndRetrieveSubject(jwt);
					var userCredentials = userCredentialsService.findByUserEmail(email).get();
					var authToken = new UsernamePasswordAuthenticationToken(email, userCredentials.password(),
							getAuthorities(userCredentials.role().name()));
					if (SecurityContextHolder.getContext().getAuthentication() == null) {
						SecurityContextHolder.getContext().setAuthentication(authToken);
					}
				} catch (JWTVerificationException exception) {
					response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Token inválido.");
				}
			}
		}
		
		filterChain.doFilter(request, response);
	}
	
	private Collection<? extends GrantedAuthority> getAuthorities(String role) {
		return Arrays.asList(new SimpleGrantedAuthority("ROLE_" + role));
	}
}
