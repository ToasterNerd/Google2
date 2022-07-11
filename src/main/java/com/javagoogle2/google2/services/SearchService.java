package com.javagoogle2.google2.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javagoogle2.google2.entities.WebPage;
import com.javagoogle2.google2.repositories.SearchRepository;

//le doy una anotacion para decirle a springboot que es un servicio
@Service
public class SearchService {
    
    @Autowired
    private SearchRepository repository;
    
    public List<WebPage> search(String textSearch){
        return repository.search(textSearch);
    }

    public void save(WebPage webPage){
        repository.save(webPage);
    }

    public boolean exist(String link) {
        return repository.exist(link);
    }

    public  List<WebPage> getLinksToindex(){
        return repository.getLinksToindex();
    }
}


