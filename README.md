# spring boot 3.4.2 kotlin multimodule boilerplate

## Version
```properties
kotlinVersion=1.9.25
springBootVersion=3.4.2
springDependencyManagementVersion=1.1.7

### External Dependency versions ###
ktlintVersion=12.1.2
querydslVersion=5.0.0
coroutinesVersion=1.8.0-RC
kotestVersion=5.8.0
kotestSpringVersion=1.1.3
mockKVersion=1.13.8
```

## Multi Module

```bash
❯ tree -L 4 .
.
├── app
│   ├── app-api     # APP Api
│   ├── app-batch   # APP Batch
├── core                  # Core
├── domain
│   └── rds         # Domain RDS
└── framework
   ├── cache        # Cache
   └── client       # Client
       ├── base     # Client Base
       └── kakao    # Kakao Client

```

## Restdocs + OpeanAPI + SwaggerUI

```bash
plugins {
    id("com.epages.restdocs-api-spec")
    id("org.hidetake.swagger.generator")
}

dependencies {
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.5")

    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    testImplementation("com.epages:restdocs-api-spec-mockmvc:0.19.4")
}

openapi3 {
    this.setServer("http://localhost:8080")
    title = "APP API"
    description = "APP API"
    version = "1.0.0"
    format = "yaml"
    snippetsDirectory = "build/generated-snippets"
    outputDirectory = "build/api-spec"
}

tasks {
    withType<GenerateSwaggerUI> {
        dependsOn("openapi3")
        delete { delete("src/main/resources/static/docs/") }
        copy {
            from("build/api-spec/")
            into("src/main/resources/static/docs/")
        }
    }
    withType<BootJar> {
        dependsOn("openapi3")
    }
}
```

## Security

```kotlin
@Bean
    @Throws(Exception::class)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .cors { it.disable() }
            .headers { headers -> headers.frameOptions { it.disable() } }
            .httpBasic { it.disable() }
            .formLogin { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .exceptionHandling {
                it.authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            }
            .authorizeHttpRequests {
                it.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                it.requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                it.requestMatchers(AntPathRequestMatcher("/docs/**")).permitAll()
                it.requestMatchers(AntPathRequestMatcher("/v3/api-docs/**")).permitAll()
                it.requestMatchers(AntPathRequestMatcher("/actuator/**")).permitAll()
                it.requestMatchers(AntPathRequestMatcher("/api/v1/posts", HttpMethod.GET.name())).permitAll()
                it.requestMatchers(AntPathRequestMatcher("/api/v1/users/**")).hasAnyRole(AuthRole.USER.name)
                it.anyRequest().authenticated()
            }
            .addFilterBefore(
                JwtAuthenticationFilter(
                    userDetailsService = customUserDetailsService,
                ),
                UsernamePasswordAuthenticationFilter::class.java,
            )
        return http.build()
    }
```

## WithMockCustomerUser

```kotlin
@Retention(AnnotationRetention.RUNTIME)
@WithMockUser(roles = ["USER"])
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory::class)
annotation class WithMockCustomerUser(
    val id: Long = 1L,
    val email: String = "test@weproud.com",
    val name: String = "test",
)

class WithMockCustomUserSecurityContextFactory : WithSecurityContextFactory<WithMockCustomerUser> {
    override fun createSecurityContext(withMockCustomerUser: WithMockCustomerUser): SecurityContext {
        val context = SecurityContextHolder.createEmptyContext()
        val principal =
            CustomUserDetails(
                id = withMockCustomerUser.id,
                email = withMockCustomerUser.email,
                name = withMockCustomerUser.name,
            )
        val auth: Authentication = UsernamePasswordAuthenticationToken(principal, "password", principal.authorities)
        context.authentication = auth
        return context
    }
}
```

## github action

```yaml

name: "[dev] Deploy app-api"
run-name: "[dev] Deploy app-api"

on:
    workflow_dispatch:
        inputs:
            what:
                description: 'ready?'
                required: true
                default: 'go'

concurrency:
    group: ${{ github.workflow }}-${{ github.ref }}
    cancel-in-progress: true

jobs:
    deploy:
        uses: ./.github/workflows/dev-deploy-workflow-call.yml
        with:
            environment: dev
            EB_APPLICATION_NAME: app-api
            EB_ENVIRONMENT_NAME: app-api-dev
            APP_MODULE: :app:app-api
            APP_MODULE_PATH: app/app-api
            APP_GENERATE_OPENAPI: true
        secrets: inherit

---

```
