package com.ztj.jwtdemo.common.jwt;

import com.ztj.jwtdemo.common.util.StringUtils;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * JWT 公共方法类
 */
@Component
public class JWTUtils {

    @Autowired
    private JWTProperties jwtProperties;

    /**
     * 生成token
     *
     * @param info
     * @return
     */
    public String generateToken(JWTInfo info) {
        final Date createdDate = new Date();
        final Date expirationDate = new Date(createdDate.getTime() + jwtProperties.getExpire() * 1000);

        return Jwts.builder()
                .setSubject(info.getUserAccount())
                .claim(JWTConstant.USER_NAME, info.getUserName())
                .claim(JWTConstant.USER_DEPT, info.getUserDept())
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, jwtProperties.getSecret())
                .compact();
    }

    /**
     * 从token中获取JWTInfo
     *
     * @param token
     * @return
     */
    public JWTInfo getInfoFromToken(String token) {
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(jwtProperties.getSecret()).parseClaimsJws(token);

        Claims body = claimsJws.getBody();
        return new JWTInfo(body.getSubject(), StringUtils.returnObjectValue(body.get(JWTConstant.USER_ACCOUNT)),
                StringUtils.returnObjectValue(body.get(JWTConstant.USER_NAME)));
    }

    /**
     * 判断token是否过期
     *
     * @param token
     * @return
     */
    public Boolean isTokenExpired(String token) {
        try {
            final Date expiration = getClaimFromToken(token).getExpiration();
            return expiration.before(new Date());
        } catch (ExpiredJwtException expiredJwtException) {
            return true;
        }
    }

    /**
     * 获取Claims
     */
    private Claims getClaimFromToken(String token) {
        return Jwts.parser().setSigningKey(jwtProperties.getSecret()).parseClaimsJws(token).getBody();
    }

}
