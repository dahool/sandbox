package com.hsbc.hbar.teller.broker.security;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JWTTokenHelper {

	@Value("${jwt.token.leeway}")
	private Long tokenLeeway;

	@Value("${jwt.token.alg}")
	private String algMode;
	
	@Value("${jwt.token.secret}")
	private String secret;
	
	@Value("${jwt.token.publickey}")
	private String keyFileName;
	
	private Algorithm algorithm;
	
	private JWTVerifier verifier;
	
	@PostConstruct
	public void initialize() throws IllegalArgumentException, NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		if ("rsa".equalsIgnoreCase(algMode)) {
			algorithm = Algorithm.RSA512(PublicKeyReader.readFromFile(keyFileName), null);
			log.debug("Initialized JWT Algorithm using RSA key file {}", keyFileName);
		} else {
			log.debug("Initialized JWT Algorithm using SHA secret");
			algorithm = Algorithm.HMAC512(Base64.getDecoder().decode(secret.getBytes()));
		}
		verifier = JWT.require(algorithm)
					.acceptLeeway(tokenLeeway)
					.build();
	}
	
	public Optional<String> verify(String token) {
		log.debug("Verify received token {}", token);
		try {
			DecodedJWT decoded = verifier.verify(token);
			log.debug("Token verification successful");
			return Optional.of(ObjectUtils.firstNonNull(decoded.getIssuer(), decoded.getSubject(), "Validated"));
		} catch (JWTVerificationException e) {
			log.error("Error during verification of token {}", token, e);
		}
		return Optional.empty();
	}
	
	private static final class PublicKeyReader {
		
		public static RSAPublicKey readFromFile(String filename) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
			final Path filePath = Paths.get(filename);
			
			if (!Files.isReadable(filePath)) {
				log.error("PublicKey file {} doesn't exists, or has invalid permissions", filename);
				throw new IOException("Missing PublicKey file");
			}
			
			final String key = new String(Files.readAllBytes(filePath), Charset.defaultCharset());
			
		    final String publicKeyPEM = key
		    	      .replace("-----BEGIN PUBLIC KEY-----", "")
		    	      .replaceAll(System.lineSeparator(), "")
		    	      .replace("-----END PUBLIC KEY-----", "");
		    
		    byte[] decoded = Base64.getDecoder().decode(publicKeyPEM);
		    
		    final KeyFactory fk = KeyFactory.getInstance("RSA");
		    final X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
			
		    return (RSAPublicKey) fk.generatePublic(spec);
		}
		
	}
	
}
