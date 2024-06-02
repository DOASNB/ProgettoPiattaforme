package com.progettopiattaforme.security.authentication;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    //classe usata per convertire un oggetto Jwt (json web token) in oggetto di autenticazione SpringSecurity di tipo AbstractAuthenticationToken

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter =
            new JwtGrantedAuthoritiesConverter();//converte i ruoli/gruppi presenti nel JWT in oggetti GrantedAuthority


    @Value("${jwt.auth.converter.principle-attribute}")
    private String principleAttribute;//nome dell'attributo principale nel token JWT

    @Value("progetto1")
    private String resourceId;//identificatore della risorsa associato al token JWT

    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {//implementa la logica di conversione del token JWT in oggetto per SpringSecurity
        Collection<GrantedAuthority> authorities = Stream.concat(//rappresenta una collezione dei ruoli/gruppi presenti nel token JWT
                jwtGrantedAuthoritiesConverter.convert(jwt).stream(),//convertiamo
                extractResourceRoles(jwt).stream()//estrare i ruoli/gruppi dalla risorsa jwt
                //concateniamo in un unico stream
        ).collect(Collectors.toSet());

        return new JwtAuthenticationToken(//viene restituito un nuovo oggetto JwtAuthentication
                jwt,
                authorities,
                getPrincipleClaimName(jwt)
        );
    }

    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt){
        Map<String, Object> resourceAccess;
        Map<String, Object> resource;
        Collection<String> resourceRoles;

        if(jwt.getClaim("resource_access") == null){//non abbiamo resource_access
            return Set.of(); //restituiamo una Collection vuota
        }

        resourceAccess = jwt.getClaim("resource_access");

        if(resourceAccess.get(resourceId) == null){
            return Set.of();//non ho alcun ruolo per questa specifica risorsa
        }

        resource =(Map<String, Object>)resourceAccess.get(resourceId);

        resourceRoles = (Collection<String>)resource.get("roles");

        return resourceRoles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role)).collect(Collectors.toSet());
    }

    private String getPrincipleClaimName(Jwt jwt){//restituisce il valore dell'attributo principale
        String claimName = JwtClaimNames.SUB;//default di keycloak restituito

        if(principleAttribute != null){
            claimName = principleAttribute;

        }
        return jwt.getClaim(claimName);
    }

}
