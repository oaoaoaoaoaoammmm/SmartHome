package com.example.townservice.configuration

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info

@OpenAPIDefinition(
    info = Info(
        title = "Town Service API",
        description = "Town Service",
        version = "1.0",
        contact = Contact(
            name = "Малышка, если ты че то не поняла, то это не значит шо меня нада заеобувать",
            url = "https://den4ick/slaziet"
        )
    )
)
class SwaggerConfig