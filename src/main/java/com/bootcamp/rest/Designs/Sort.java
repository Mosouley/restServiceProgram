/**
 *
 * @Bignon
 */
package com.bootcamp.rest.Designs;

public class Sort {
    private  String attribut;
    private  String ordre;

    public Sort( ) {
        this.attribut = "id";
        this.ordre = "ASC";
    }   

    public  String getAttribut() {
        return attribut;
    }

    public  void setAttribut(String attribut) {
        this.attribut = attribut;
    }

    public  String getOrdre() {
        return ordre;
    }

    public  void setOrdre(String ordre) {
        this.ordre = ordre;
    }
    
}
