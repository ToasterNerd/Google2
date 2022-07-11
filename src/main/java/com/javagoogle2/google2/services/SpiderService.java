package com.javagoogle2.google2.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javagoogle2.google2.entities.WebPage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hibernate.internal.util.StringHelper.isBlank;
//estas son las famosas arañas de googles que van recorriendo toda la red para traer la info
@Service
public class SpiderService {

    @Autowired
    private SearchService searchService;


    //COMO FUNCIONA
    //funcion para descargue la pagina web, la procesa y la guarda con el searchService.save en base de datos
    public void indexWebPages() {
        List<WebPage> linksToIndex = searchService.getLinksToindex(); 
        
        linksToIndex.forEach(WebPage ->{
            try {
                System.out.println("indexandos las paginas");
                indexWebPage(WebPage);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });

    }

    private void indexWebPage(WebPage WebPage) {
        String url = WebPage.getUrl();
        String content = getWebContent(url);
        if (isBlank(content)){
            System.out.println("es blanco");
            return;
        }
        
        indexAndSaveWebPage(WebPage, content);
        
        String domain = getDomain(url);
        saveLinks(domain, content);
    }

    private String getDomain(String url) {
        String[] aux = url.split("/");
        return aux[0] + "//" + aux[2];

    }

    private void saveLinks(String domain, String content) {
        List<String> links = getLinks(domain, content);
        //recorro los links
        links.stream().filter(link -> !searchService.exist(link))
                .map(link -> new WebPage(link))
                .forEach(webPage -> searchService.save(webPage));
        
    }

    //funcion para traer todos los links que tiene la pagina (los HREF)
    public List<String> getLinks(String domain, String content){
        List<String> links = new ArrayList<>();

        //para traer los links hay que filtrar los href
        String[] splitHref = content.split("href=\"");

        //traigo los link y elimino la primer parte para que quede la segunda nomas
        List<String> listHref = Arrays.asList(splitHref);
        //listHref.remove(0);


        //aca saco la segunda parte del href y finalmente me queda el link sin nada mas
        listHref.forEach(strHref -> {
            String[] aux2 = strHref.split("\""); 
            links.add(aux2[0]);
        });
        
        return cleanLinks(domain, links);
        
    }

    //funcion para limpiar los links de basura, solo me interesa los links posta
    private List<String> cleanLinks (String domain, List<String> links){
        String[] excludedExtensions = new String[]{"css","js","json","jpg","png"};
        //tambien tenemos que tener en cuenta lo que no aparece con http al principio
        List <String> resultLinks= links.stream()
                .filter(link -> Arrays.stream(excludedExtensions)
                .noneMatch(link::endsWith))
                .map(link -> link.startsWith("/") ? domain + link : link)
                .collect((Collectors.toList()));

        //con esto hago que no se repitan los links, porque los hasheo y se hacen unicos
        List <String> uniqueLinks = new ArrayList<>();
        uniqueLinks.addAll(new HashSet<>(resultLinks));


        return uniqueLinks;
    }



    private void indexAndSaveWebPage(WebPage webPage, String content) {
                /*<meta name="description" content="Breaking news, sport, TV, radio and a whole lot more.
        The BBC informs, educates and entertains - wherever you are, whatever your age.">
        <meta name="keywords" content="BBC, bbc.co.uk, bbc.com, Search, British Broadcasting Corporation, BBC iPlayer, BBCi">
        <title>BBC - Homepage</title> */
        
        String title = getTitle(content);
        String description = getDescription(content);
        webPage.setDescription(description);
        webPage.setTitle(title);
        //ahora el guardado lo hace el searchservice con una funcion
        searchService.save(webPage);
    }

    //creo una funcion que me devuelve el titulo
    public String getTitle(String content){
        String[] aux = content.split("<title>"); //agarro todo el texto y separo en 2 partes antes y despues de ese parametro que mando
        String[] aux2 = aux[1].split("</title>"); //otra vez agarro la segunda parte y hago otro split. y así voy tomando elvalor que uqiero
        return aux2[0];
    }


        //creo una funcion que me devuelve la descripcion
        public String getDescription(String content){
            String[] aux = content.split("<meta name=\"description\" content=\""); //agarro todo el texto y separo en 2 partes antes y despues de ese parametro que mando
            String[] aux2 = aux[1].split("\">"); //otra vez agarro la segunda parte y hago otro split. y así voy tomando elvalor que uqiero
            return aux2[0];
        }

    private String getWebContent(String link) { // con esto le pasamos un link y nos devuelve el html de ese link
        try {
            URL url = new URL(link);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            String encoding = conn.getContentEncoding();

            InputStream input = conn.getInputStream();

            Stream<String> lines = new BufferedReader(new InputStreamReader(input))
                    .lines();

            return lines.collect(Collectors.joining());
        } catch (IOException e) {
            System.out.print(e.getMessage());
        }
        return "";
    }
}
