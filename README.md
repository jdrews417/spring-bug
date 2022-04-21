# spring-bug
Demonstration Project to show undesired behavior with the OAuth2 client in spring security

# Setup
To use this test project, you will need to supply the OAuth2 configuration.

Update the src/main/resources/application.yaml file and enter the client ID and secret for your OAuth2 source.

```
spring:
  security:
    oauth2:
      client:
        registration:
          demo1:
            client-id: <my client id>
            client-secret: <my client secret>
          demo2:
            client-id: <my client id>
            client-secret: <my client secret>            
```

If you modify the redirect URL, you will need to update the href entries on the demo\index.html file as well.

You will also need to provide valid OAuth2 Provider configuration

```
spring:
  security:
    oauth2:
      client:
        provider:
          demo1:
            authorization-uri: https://host/idp/profile/oidc/authorize
            token-uri: https://host/idp/profile/oidc/token
            user-info-uri: https://host/idp/profile/oidc/userinfo
            user-name-attribute: sub
            jwk-set-uri: https://host/idp/profile/oidc/keyset
```

The Demo1 and Demo2 providers can be the same if both demo1 and demo2 can be the same source provider as long as it 
has the registration information both demo1 and demo2 clients.

# Running

Now compile and run the application and open a web browser to https://localhost:8443/
A Self-signed "localhost" certificat file is provided in the project to enable
SSL support.
