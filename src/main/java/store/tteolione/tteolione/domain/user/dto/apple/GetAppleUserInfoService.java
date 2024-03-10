package store.tteolione.tteolione.domain.user.dto.apple;

import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.security.Security;

import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class GetAppleUserInfoService {

    private final AppleAuthClient appleAuthClient;

    private final AppleProperties appleProperties;

    public AppleIdTokenPayload get(String authorizationCode) {

        String idToken = appleAuthClient.getIdToken(
                appleProperties.getClientId(),
                generateClientSecret(),
                appleProperties.getGrantType(),
                authorizationCode
        ).getIdToken();

        return TokenDecoder.decodePayload(idToken, AppleIdTokenPayload.class);
    }

    public String getAccessToken(String authorizationCode) {

        String accessToken = appleAuthClient.getIdToken(
                appleProperties.getClientId(),
                generateClientSecret(),
                appleProperties.getGrantType(),
                authorizationCode
        ).getAccessToken();

        return accessToken;
    }

    private String generateClientSecret() {

        LocalDateTime expiration = LocalDateTime.now().plusMinutes(5);

        return Jwts.builder()
                .setHeaderParam(JwsHeader.KEY_ID, appleProperties.getKeyId())
                .setIssuer(appleProperties.getTeamId())
                .setAudience(appleProperties.getAudience())
                .setSubject(appleProperties.getClientId())
                .setExpiration(Date.from(expiration.atZone(ZoneId.systemDefault()).toInstant()))
                .setIssuedAt(new Date())
                .signWith(getPrivateKey(), SignatureAlgorithm.ES256)
                .compact();
    }

    private PrivateKey getPrivateKey() {

        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");

        try {
            byte[] privateKeyBytes = Base64.getDecoder().decode(appleProperties.getPrivateKey());

            PrivateKeyInfo privateKeyInfo = PrivateKeyInfo.getInstance(privateKeyBytes);
            return converter.getPrivateKey(privateKeyInfo);
        } catch (Exception e) {
            throw new RuntimeException("Error converting private key from String", e);
        }
    }

    public void revoke(String accessToken) {
        try {
            AppleRevokeRequest appleRevokeRequest = AppleRevokeRequest.builder()
                    .client_id(appleProperties.getClientId())
                    .client_secret(this.generateClientSecret())
                    .token(accessToken)
                    .token_type_hint("access_token")
                    .build();

            appleAuthClient.revoke(appleRevokeRequest);
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Apple Revoke Error");
        }
    }
}