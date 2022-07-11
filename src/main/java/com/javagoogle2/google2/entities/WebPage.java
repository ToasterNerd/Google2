package com.javagoogle2.google2.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//el entity es para la persistencia (JPA, hibernate) relacionar objetos java con cosas de base de datos
//con esta anotacion le digo que la clase webpage va a ser una entidad para la base de datos
@Entity
@Table (name = "webpage") //le digo que nombre tiene la tabla de la que esta relacionada
@ToString @EqualsAndHashCode //me genera los métodos para usarlos en la clase
@Getter @Setter
public class WebPage {
    
    //tambien para el jpa le indico el nombre de columna que va a tener cada una de estas variables
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //hago que sea autoincremental
    @Column(name = "id")
    private Long id;

    @Column(name = "url")
    private String url;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;


    public WebPage (){
        
    }

    public WebPage (String url){
        this.url = url;
    }
}
