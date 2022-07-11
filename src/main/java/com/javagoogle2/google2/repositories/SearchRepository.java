package com.javagoogle2.google2.repositories;

import java.util.List;

import com.javagoogle2.google2.entities.WebPage;

public interface SearchRepository {
    
    WebPage getByUrl(String url);

    List<WebPage> getLinksToindex();

    //vamos a crear una lista acá
    public List<WebPage> search (String textSearch);

    public void save(WebPage webPage);

    public boolean exist(String link);
}
