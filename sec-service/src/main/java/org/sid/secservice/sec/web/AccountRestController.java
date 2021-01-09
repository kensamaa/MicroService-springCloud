package org.sid.secservice.sec.web;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.sid.secservice.sec.JWTUtil;
import org.sid.secservice.sec.entities.AppRole;
import org.sid.secservice.sec.entities.AppUser;
import org.sid.secservice.sec.service.AccountService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class AccountRestController {
    private AccountService accountService;
    public AccountRestController(AccountService accountService) {
        this.accountService = accountService;
    }
    @GetMapping(path = "/users")
    @PreAuthorize("hasAuthority('USER')")
    public List<AppUser> appUsers() {
        return accountService.listUsers();
    }
    @PostMapping(path = "/users")
    @PreAuthorize("hasAuthority('ADMIN')")
    public AppUser saveUser(@RequestBody AppUser appUser) {
        return accountService.addNewUser(appUser);
    }

    @PostMapping(path = "/roles")
    @PreAuthorize("hasAuthority('ADMIN')")
    public AppRole saveRole(@RequestBody AppRole appRole) {
        return accountService.addNewRole(appRole);
    }
    @PostMapping(path = "/addRoleToUser")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void addRoleToUser(@RequestBody RoleUserForm roleUserForm) {
        accountService.addRoleToUser(roleUserForm.getUsername(), roleUserForm.getRoleName());
    }
    @GetMapping(path = "/refreshToken")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            try{
                String jwt=token.substring(7);
                Algorithm algo1=Algorithm.HMAC256(JWTUtil.SECRET);
                JWTVerifier jwtVerifier= JWT.require(algo1).build();
                DecodedJWT decoded=jwtVerifier.verify(jwt);
                String username=decoded.getSubject();

                AppUser appUser=accountService.loadUserByUsername(username);
                String jwtAccessToken= JWT.create()
                        .withSubject(appUser.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis()+JWTUtil.EXPIRE_ACCESS_TOKEN  ))//expiration apres 5 minute
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles",appUser.getAppRoles().stream().map(r->r.getRoleName()).collect(Collectors.toList()))
                        .sign(algo1);
                Map<String,String> idToken=new HashMap<>();
                idToken.put("access-token",jwtAccessToken);
                idToken.put("refrech-token",jwt);
                response.setContentType("application/json");
                new ObjectMapper().writeValue(response.getOutputStream(),idToken);
            }
            catch (Exception e){//si expirer
                response.setHeader("error-message",e.getMessage());
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
            }
        }
        else {
            throw new RuntimeException("Bad Refresh Token");
        }
    }
    @GetMapping(path = "/profile")
    public AppUser profile(Principal principal){
        return accountService.loadUserByUsername(principal.getName());
    }
}
@Data
class RoleUserForm{
    private String username,roleName;
}
