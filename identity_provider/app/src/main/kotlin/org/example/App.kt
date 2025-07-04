/*
 * This source file was generated by the Gradle 'init' task
 */
package org.example

import io.ktor.http.Cookie
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.install
import io.ktor.server.application.log
import io.ktor.server.engine.embeddedServer
import io.ktor.server.html.respondHtml
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.NotFoundException
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.respond
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.get
import io.ktor.server.routing.head
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import kotlinx.html.FormMethod
import kotlinx.html.body
import kotlinx.html.form
import kotlinx.html.p
import kotlinx.html.passwordInput
import kotlinx.html.style
import kotlinx.html.submitInput
import kotlinx.html.textInput
import kotlinx.html.title
import org.example.application.usecase.AddUserContract
import org.example.application.usecase.Login
import org.example.application.usecase.LoginInput
import org.example.application.usecase.Logout
import org.example.application.usecase.RefreshToken
import org.example.application.usecase.RefreshTokenInput
import org.example.application.usecase.RetrieveSessionByID
import org.example.application.usecase.RetrieveUser
import org.example.application.usecase.GetTokensInput
import org.example.application.usecase.UserRegistration
import org.example.infra.hash.BCryptPasswordHash
import org.example.infra.redis.RedisConnection
import org.example.infra.redis.RedisInMemoryDao
import org.example.infra.repository.InMemoryUserRepository
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.request.receive
import kotlinx.serialization.Serializable
import org.example.application.AuthenticationException
import org.example.application.SessionExpiredException
import org.example.application.usecase.ContractInput
import org.example.application.usecase.GetSession
import org.example.application.usecase.GetTokens
import org.example.application.usecase.ListUserContracts
import org.example.application.usecase.UserRegistrationWithIdInput

@Serializable
data class LogoutInput(
    val refreshToken: String
)

/**
 * RFC 9457 - “Problem Details for HTTP APIs”
 */
@Serializable
data class Problem(
    val title: String,
    val detail: String,
    val status: Int
)

fun main() {
    val userRepository = InMemoryUserRepository()
    val passwordHash = BCryptPasswordHash()
    val redisConn = RedisConnection
    val inMemoryDao = RedisInMemoryDao(redisConn)
    val userRegistration = UserRegistration(userRepository, passwordHash)
    val retrieveUser = RetrieveUser(userRepository)
    val login = Login(userRepository, passwordHash)
    val addUserContract = AddUserContract(userRepository)
    val getTokens = GetTokens(userRepository, inMemoryDao, passwordHash)
    val logout = Logout(inMemoryDao)
    val retrieveSessionByID = RetrieveSessionByID(inMemoryDao)
    val refreshToken = RefreshToken(userRepository, inMemoryDao, passwordHash)
    val getSession = GetSession(inMemoryDao)
    val listUserContracts = ListUserContracts(userRepository)

    val output = userRegistration.execute(
        UserRegistrationWithIdInput(
            "805a852d-61e8-4a07-9e1f-02141ae74e94",
            "pedroteste",
            "12345",
            "pedro@email.com")
    )

    addUserContract.execute(output.userId,
        ContractInput(
            id = "0042d963-6c54-4c9f-a60c-bfcde866d29e",
            matricula = "123456",
            organization = "Organization A",
            department = "Department X"
        )
    )

    embeddedServer(Netty, port = 8080) {
        install(ContentNegotiation) {
            json()
        }
        install(CORS) {
            allowHost("localhost:5173", schemes = listOf("http"))
            anyMethod()
            allowCredentials = true
            allowHeader(HttpHeaders.ContentType)
            allowHeader(HttpHeaders.Authorization)
        }
        install(StatusPages) {
            exception<BadRequestException> { call, cause ->
                call.respond(HttpStatusCode.BadRequest, Problem(
                    title = "BadRequest",
                    detail = cause.message ?: "Invalid request",
                    status = HttpStatusCode.BadRequest.value
                ))
            }
            exception<AuthenticationException.MissingToken> { call, cause ->
                call.respond(HttpStatusCode.Unauthorized, Problem(
                    title = "Unauthorized",
                    detail = cause.message!!,
                    status = HttpStatusCode.Unauthorized.value
                ))
            }
            exception<AuthenticationException.CredentialsFailed> { call, cause ->
                call.respond(HttpStatusCode.Unauthorized, Problem(
                    title = "Unauthorized",
                    detail = cause.message!!,
                    status = HttpStatusCode.Unauthorized.value
                ))
            }
            exception<AuthenticationException.ExpiredToken> { call, cause ->
                call.respond(HttpStatusCode.Unauthorized, Problem(
                    title = "Unauthorized",
                    detail = cause.message!!,
                    status = HttpStatusCode.Unauthorized.value
                ))
            }
            exception<AuthenticationException.InvalidToken> { call, cause ->
                call.respond(HttpStatusCode.Unauthorized, Problem(
                    title = "Unauthorized",
                    detail = cause.message!!,
                    status = HttpStatusCode.Unauthorized.value
                ))
            }
            exception<SessionExpiredException> { call, cause ->
                call.application.log.warn("Refresh token is missing")
                call.respond(HttpStatusCode.Unauthorized, Problem(
                    title = "Unauthorized",
                    detail = cause.message,
                    status = HttpStatusCode.Unauthorized.value
                ))
            }
            exception<Exception> { call, cause ->
                call.respond(HttpStatusCode.InternalServerError, Problem(
                    title = "Unknown error",
                    detail = cause.message ?: "Unknown error: ${cause.cause}",
                    status = HttpStatusCode.InternalServerError.value
                ))
            }
        }
        routing {
            get("/auth") {
                val erro = call.request.queryParameters["error"]
                call.respondHtml(HttpStatusCode.OK) {
                    head {
                        title
                        ("Login page")
                    }
                    body {
                        form(action = "/login", method = FormMethod.post) {
                            textInput(name = "username") { placeholder = "username" }
                            passwordInput(name = "password") { placeholder = "password" }

                            submitInput { value = "Login" }
                        }
                        if (erro != null) {
                            p {
                                style = "color:red;"
                                +"Invalid username or password"
                            }
                        }
                    }
                }
            }
            post("/login") {
                val formParameters = call.receiveParameters()
                val username = formParameters["username"].toString()
                val password = formParameters["password"].toString()

                try {
                    val result = login.execute(LoginInput(username, password))

                    call.response.cookies.append(
                        "isAuth",
                        "1",
                        path = "/",
                        httpOnly = true,
                        secure = true,
                        maxAge = 120,
                        extensions = mapOf("SameSite" to "Lax")
                    )
                    call.response.cookies.append(
                        "authToken",
                        result.token,
                        path = "/",
                        httpOnly = true,
                        secure = true,
                        maxAge = 120,
                        extensions = mapOf("SameSite" to "Lax")
                    )
//                    call.respondRedirect("http://localhost:5173/callback?token=${result.token}")
                    call.respondRedirect("http://localhost:5173/contracts")
                } catch (e: Exception) {
                    call.respondRedirect("/auth?error=1")
                }
            }
            post("/authenticate") {
                val input = call.receive<LoginInput>()
                val output = login.execute(LoginInput(input.username, input.password))
                call.response.cookies.append(
                    "isAuth",
                    "1",
                    path = "/",
                    httpOnly = true,
                    secure = true,
                    maxAge = 120,
                    extensions = mapOf("SameSite" to "Lax")
                )
                call.response.cookies.append(
                    "authToken",
                    output.token,
                    path = "/",
                    httpOnly = true,
                    secure = true,
                    maxAge = 120,
                    extensions = mapOf("SameSite" to "Lax")
                )
                call.respond(HttpStatusCode.OK, output)
            }
            get("/user-contracts") {
                val authToken = call.request.cookies["authToken"]
                    ?: throw Exception("AuthToken token is missing")
                val output = listUserContracts.execute(authToken)
                call.respond(HttpStatusCode.OK, output)
            }
            post("/tokens/{contractId}") {
//                val partialToken = call.request.headers["Authorization"]
//                    ?: throw AuthenticationException.MissingToken()
                val authToken = call.request.cookies["authToken"]
                    ?: throw Exception("AuthToken token is missing")

                val contractId = call.parameters["contractId"]
                    ?: throw BadRequestException("Contract ID is mandatory")

                val input = GetTokensInput(authToken, contractId)
                val output = getTokens.execute(input)

                call.response.cookies.append(
                    "refreshToken",
                    output.refreshToken,
                    path = "/",
                    httpOnly = true,
                    secure = true,
                    maxAge = 120,
                    extensions = mapOf("SameSite" to "Lax")
                )
                call.response.cookies.append(
                    "isAuth",
                    "2",
                    path = "/",
                    httpOnly = true,
                    secure = true,
                    maxAge = 120,
                    extensions = mapOf("SameSite" to "Lax")
                )

                call.respond(HttpStatusCode.OK, mapOf(
                    "sessionId" to output.sessionId,
                    "accessToken" to output.accessToken
                ))
            }
            post("/refresh-tokens") {
                val refreshTokenCookie = call.request.cookies["refreshToken"]
                    ?: throw SessionExpiredException("Refresh token is missing")

                val input = RefreshTokenInput(refreshTokenCookie)
                val output = refreshToken.execute(input)

                call.response.cookies.append(
                    "refreshToken",
                    output.refreshToken,
                    path = "/",
                    httpOnly = true,
                    secure = true,
                    maxAge = output.remainTtl,
                    extensions = mapOf("SameSite" to "Lax")
                )

                call.respond(HttpStatusCode.OK, mapOf(
                    "sessionId" to output.sessionId,
                    "accessToken" to output.accessToken
                ))
            }
            post("/logout") {
                val refreshToken = call.request.cookies["refreshToken"]
                    ?: throw SessionExpiredException("Refresh token is missing")

                logout.execute(refreshToken)
                call.response.cookies.append(
                    Cookie(name = "refreshToken", value = "", path = "/", maxAge = 0)
                )
                call.response.cookies.append(
                    Cookie(name = "isAuth", value = "", path = "/", maxAge = 0)
                )

                call.respond(HttpStatusCode.OK)
            }
            get("/sessions/{sessionId}") {
                val sessionId = call.parameters["sessionId"]
                    ?: throw BadRequestException("Session ID is mandatory")

                val output = getSession.execute(sessionId)
                    ?: throw NotFoundException("Session not found")

                call.respond(HttpStatusCode.OK, output)
            }
            get("/health") {
                try {
                    RedisConnection.jedis.ping()
                    call.respond(HttpStatusCode.OK)
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.ServiceUnavailable, "Redis is not available")
                }
            }
        }
    }.start(wait = true)



//    val output = userRegistration.execute(
//        UserRegistrationInput("pedroteste", "12345", "pedro@email.com")
//    )
//    println(output)

//    try {
//        val output = retrieveUser.execute(output.userId)
//        println(output)
//    } catch (e: UserNotFoundException) {
//        println(e.message)
//    }
//
//    try {
//        val output = retrieveUser.execute("ddadaafdsadadadasdadadsad")
//        println(output)
//    } catch (e: UserNotFoundByIdException) {
//        println(e.message)
//    } catch (ex: Exception) {
//        println(ex.message)
//    }

//    println("---------------------")


//    try {
//        // to login
//        val result = login.execute(LoginInput("pedroteste", "12345"))
//        println(result)
//
//        // add contract
//        val contractId = UUID.randomUUID().toString()
//        addUserContract.execute(output.userId, contractId)
//
//        // set contract
//        val output = setUserContract.execute(SetUserContractInput(result.token, contractId))
//        println(output)
//
//        println("Sessão antes: ${retrieveSessionByID.execute(output.sessionId)}")
//
//        Thread.sleep(2000)
//
//        // refresh token
//        val refresh = refreshToken.execute(RefreshTokenInput(output.refreshToken))
//        println(refresh)
//
//        println("Sessão depois: ${retrieveSessionByID.execute(output.sessionId)}")
//
//        // logout
//        logout.execute(output.refreshToken)
//    } catch (e: Exception) {
//        println(e.message)
//    }

    /**
     * http://localhost:8081/auth?response_type=code&redirect_uri=localhost:8082/select_contract
     */
}

