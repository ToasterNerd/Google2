package com.javagoogle2.google2.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.javagoogle2.google2.entities.WebPage;
import com.javagoogle2.google2.services.SearchService;
import com.javagoogle2.google2.services.SpiderService;

//le pongo esta anotacion para que sepa spring que aca hago el servicio REST
@RestController
public class SearchController {

    // desde el controller llamamos al servicio
    @Autowired // me genera automaticamente una instancia de la clase service sin necesidad de
               // poner un igual ni nada de eso. wtf. Inyeccion de dependencia
    private SearchService service;

    @Autowired
    private SpiderService spiderService;

    // FUNCION PARA REALIZAR LA BUSQUEDA
    //@CrossOrigin("*") //esto se puede poner para que cualquier persona pueda usar
    // este request
    @GetMapping(value = "api/results") // cuando se llame a esta url con GET va aplicar esta funcion
    public List<WebPage> search(@RequestParam Map<String, String> params){
        // si quiero entonces por get varios parametros hago ->
        // api/search?query=download windows&lang=en
        String query = params.get("query");
        return service.search(query);
    }

    //hago que funcione el servicio de spiderService y hago una prueba para ver que pasa
    @GetMapping(value = "api/test")
    public void test() {
        spiderService.indexWebPages();
    }
}
