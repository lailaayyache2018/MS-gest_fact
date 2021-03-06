package org.sid.secservice.sec.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.jws.soap.SOAPBinding;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class JwtAuthenticationFiltre extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public JwtAuthenticationFiltre(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
   // premier authantication categorie 1
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String userName= request.getParameter("username");
        String password= request.getParameter("password");
        UsernamePasswordAuthenticationToken authenticationToken= new UsernamePasswordAuthenticationToken(userName,password);

        return authenticationManager.authenticate(authenticationToken);

    }
    // 2 EME authantication categorie 1

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User user=(User) authResult.getPrincipal();
        Algorithm algo1=Algorithm.HMAC256("mySecret1234");
        String jwtAccesToken= JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+1*60*1000))
               .withIssuer(request.getRequestURL().toString())
                .withClaim("roles",user.getAuthorities().stream().map(ga ->ga.getAuthority()).collect(Collectors.toList() ))
                .sign(algo1);
        String jwtRefrechToken= JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+20*60*1000))
                .withIssuer(request.getRequestURL().toString())
                .sign(algo1);

        Map<String,String> idToken=new HashMap<>();
        idToken.put("access-token",jwtAccesToken);
        idToken.put("refrech-token",jwtRefrechToken);
        new ObjectMapper().writeValue(response.getOutputStream(),idToken);

        response.setContentType("application/json");
    }
}
