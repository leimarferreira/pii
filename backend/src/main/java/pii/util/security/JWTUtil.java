package pii.util.security;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

@Component
public class JWTUtil {
	
	@Value("${security.jwt.subject: User Details}")
	private String subject;
	
	@Value("${security.jwt.issuer}")
	private String issuer;

	@Value("${security.jwt.secret}")
	private String secret;
	
	@Value("${security.jwt.claim: email}")
	private String claim;
	
	@Value("${security.jwt.expires-after: 1}")
	private Long expiresAfter;
	
	@Value("${security.jwt.expires-after-unit: DAYS}")
	private String unit;
	
	@PostConstruct
	private void initializeFields() {
		if (secret == null) {
			secret = UUID.randomUUID().toString().replace("-", "");
		}
	}
	
	public String generateToken(String email) {
		var issuedAt = new Date();
		var expiresAt = new Date(issuedAt.getTime() + Duration.of(expiresAfter, ChronoUnit.valueOf(unit)).toMillis());
		
		return JWT.create()
				.withSubject(subject)
				.withClaim(claim, email)
				.withIssuedAt(issuedAt)
				.withIssuer(issuer)
				.withExpiresAt(expiresAt)
				.sign(Algorithm.HMAC256(secret));
	}
	
	public String validateTokenAndRetrieveSubject(String token) {
		var verifier = JWT.require(Algorithm.HMAC256(secret))
				.withSubject(subject)
				.withIssuer(issuer)
				.build();
		
		var jwt = verifier.verify(token);
		return jwt.getClaim(claim).asString();
	}
}
