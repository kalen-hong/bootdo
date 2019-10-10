package com.openapi.system.utils;

import java.util.Date;

import javax.xml.bind.DatatypeConverter;

import org.springframework.util.StringUtils;

import com.openapi.system.constant.ApiConstants;
import com.openapi.system.vo.JwtAccount;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

/* *
 * @Author tomsun28
 * @Description 
 * @Date 16:29 2018/3/8
 */
public class JsonWebTokenUtil {

	public static final String SECRET_KEY = "?::4343fdf4fdf6cvf):";

	/*
	 * *
	 * 
	 * @Description json web token 签发
	 * 
	 * @param id 令牌ID
	 * 
	 * @param subject 用户ID
	 * 
	 * @param issuer 签发人
	 * 
	 * @param period 有效时间(毫秒)
	 * 
	 * @param roles 访问主张-角色
	 * 
	 * @param permissions 访问主张-权限
	 * 
	 * @param algorithm 加密算法
	 * 
	 * @Return java.lang.String
	 */
	public static String issueJWT(String id, String subject, String issuer, Long period, String permissions,
			SignatureAlgorithm algorithm) {
		// 当前时间戳
		Long currentTimeMilis = System.currentTimeMillis();
		// 秘钥
		byte[] secreKeyBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
		JwtBuilder jwtBuilder = Jwts.builder();
		if (!StringUtils.isEmpty(id)) {
			jwtBuilder.setId(id);
		}
		if (!StringUtils.isEmpty(subject)) {
			jwtBuilder.setSubject(subject);
		}
		if (!StringUtils.isEmpty(issuer)) {
			jwtBuilder.setIssuer(issuer);
		}
		// 设置签发时间
		jwtBuilder.setIssuedAt(new Date(currentTimeMilis));
		// 设置到期时间
		if (null != period) {
			jwtBuilder.setExpiration(new Date(currentTimeMilis + period * 1000));
		}
		if (!StringUtils.isEmpty(permissions)) {
			jwtBuilder.claim("perms", permissions);
		}
		// 压缩，可选GZIP
		jwtBuilder.compressWith(CompressionCodecs.DEFLATE);
		// 加密设置
		jwtBuilder.signWith(algorithm, secreKeyBytes);

		return jwtBuilder.compact();
	}

	/**
	 * 验签JWT
	 *
	 * @param jwt json web token
	 */
	public static JwtAccount parseJwt(String jwt, String appKey) throws ExpiredJwtException, UnsupportedJwtException,
			MalformedJwtException, SignatureException, IllegalArgumentException {
		Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(appKey)).parseClaimsJws(jwt)
				.getBody();
		JwtAccount jwtAccount = new JwtAccount();
		jwtAccount.setTokenId(claims.getId());// 令牌ID
		jwtAccount.setAppId(claims.getSubject());// 客户标识
		jwtAccount.setIssuer(claims.getIssuer());// 签发者
		jwtAccount.setIssuedAt(claims.getIssuedAt());// 签发时间
		jwtAccount.setExpiration(claims.getExpiration());
		jwtAccount.setAudience(claims.getAudience());// 接收方
		jwtAccount.setRoles(claims.get("roles", String.class));// 访问主张-角色
		jwtAccount.setPerms(claims.get("perms", String.class));// 访问主张-权限
		return jwtAccount;
	}

	public static void main(String[] args) {
		String token=issueJWT(String.valueOf("1"), "test", ApiConstants.TOKEN_ISSUER, ApiConstants.TOKEN_EXPIRE_TIME, null, SignatureAlgorithm.HS256);
		System.out.println(token);
	}

}
