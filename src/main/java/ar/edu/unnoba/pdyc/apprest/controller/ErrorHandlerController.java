package ar.edu.unnoba.pdyc.apprest.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ErrorHandlerController implements ErrorController {
    private String httpStatusDescription(Integer code) {
        String description;
        switch (code) {
            case 400:
                description = "Petición mal formada";
                break;
            case 401:
            case 403:
                description = "No autorizado";
                break;
            case 404:
                description = "Recurso no encontrado";
                break;
            case 405:
                description = "Método no permitido";
                break;
            case 500:
                // Error interno
                description = "Error del sistema";
                break;
            default:
                description = "";
                break;
        }
        return description;
    }

    @RequestMapping("/error")
    @ResponseBody
    public String handleError(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");
        return String.format("<html><body><h2>Error %s</h2><div><b>%s</div></b><p>%s</p></body></html>",
                statusCode, httpStatusDescription(statusCode), exception == null ? "" : exception.getMessage());
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
