package com.springleaf.auth;

import com.google.gson.internal.LinkedTreeMap;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.springleaf.common.$;
import com.springleaf.common.DefaultValues;
import com.springleaf.common.ulti.CryptoUlti;
import com.springleaf.exception.TokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import com.springleaf.object.entity.Role;
import com.springleaf.object.entity.User;

import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Slf4j
public class JWTSToken implements TokenFactory{
    static JWSAlgorithm alg = JWSAlgorithm.parse("RS256");

    @Override
    public String generate(Map<String, Object> payload, long expire){
        Map<String, Object> map = new HashMap<>();
        User user = (User) payload.get("user");
        user.setLogin(null);
        user.getRoles().stream()
                        .forEach(
                                role -> {
                                    role.setUsers(null);
                                }
                        );
        map.put(USER, user);
        long t = System.currentTimeMillis();
        log.debug("Token begin at " + System.currentTimeMillis());
        map.put(BEGIN, t);
        map.put(EXPIRE, expire);
        String unsignedToken = $.toString(map);
        return sign(unsignedToken);
    }

    @Override
    public Map<String, Object> validate(String token) {
        try {
            final JWSObject jwsObject = JWSObject.parse(token);
            RSASSAVerifier verifier = new RSASSAVerifier((RSAPublicKey) CryptoUlti.getPublicKey());
            if (jwsObject.verify(verifier)) {
                Map<String, Object> result = jwsObject.getPayload().toJSONObject();
                long t = System.currentTimeMillis();
                long begin = (long) result.get(BEGIN);
                long expire = DefaultValues.SESSION_EXPIRE;
                if ((t - begin) > expire) {
                    throw new TokenExpiredException();
                }
                return result;
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return null;
    }

    private String sign(String data) {
        try {
            JWSHeader header = new JWSHeader.Builder(alg).build();
            Payload payload = new Payload(data);
            JWSObject jwsObject = new JWSObject(header, payload);
            RSASSASigner rsassaSigner = new RSASSASigner((PrivateKey) CryptoUlti.getPrivateKey());
            jwsObject.sign(rsassaSigner);
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.debug("Cannot sign jwt");
            return null;
        }
    }
}
