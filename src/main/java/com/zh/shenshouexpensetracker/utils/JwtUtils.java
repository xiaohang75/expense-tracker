package com.zh.shenshouexpensetracker.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${jwt.secret}")
    private String jwtSecret;          // 从配置文件读取，例如：mySecretKey12345678901234567890

    @Value("${jwt.expirationMs}")
    private int jwtExpirationMs;       // 毫秒，例如 604800000（7天）

    /**
     * 生成 JWT Token
     * @param userId   用户ID
     * @param username 用户名
     * @return JWT字符串
     */
    public String generateToken(Long userId, String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(String.valueOf(userId))           // 将 userId 存入 sub
                .claim("username", username)                  // 自定义字段
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    /**
     * 从 Token 中获取 userId
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.getSubject());
    }

    /**
     * 从 Token 中获取用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return (String) claims.get("username");
    }

    /**
     * 获取 Token 的过期时间
     */
    public Date getExpirationDateFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return claims.getExpiration();
    }

    /**
     * 验证 Token 是否有效（签名正确 + 未过期）
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    /**
     * 判断 Token 是否即将过期（例如剩余时间小于5分钟，用于前端主动刷新）
     * @param token         原始Token
     * @param thresholdMs   阈值（毫秒），如 300000 表示5分钟
     * @return true 表示即将过期
     */
    public boolean isTokenExpiringSoon(String token, long thresholdMs) {
        Date expiration = getExpirationDateFromToken(token);
        long remainMs = expiration.getTime() - System.currentTimeMillis();
        return remainMs > 0 && remainMs <= thresholdMs;
    }

    /**
     * 刷新 Token（前提是原 Token 未过期，且需重新生成）
     * 注意：实际业务中最好调用 validateToken 校验通过后再刷新
     */
    public String refreshToken(String token) {
        if (!validateToken(token)) {
            throw new IllegalArgumentException("Invalid or expired token");
        }
        Long userId = getUserIdFromToken(token);
        String username = getUsernameFromToken(token);
        return generateToken(userId, username);
    }
}