// src/main/java/com/batallanaval/HomeController.java
package com.batallanaval;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // Cambiado a /home para evitar choque con WebController
    @GetMapping("/home")
    public String index() {
        return "index";
    }
}
