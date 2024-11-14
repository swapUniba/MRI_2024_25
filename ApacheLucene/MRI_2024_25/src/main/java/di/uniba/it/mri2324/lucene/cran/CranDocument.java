/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package di.uniba.it.mri2324.lucene.cran;

import java.util.Objects;

/**
 *
 * @author pierpaolo
 */
public class CranDocument {

    private String id;

    private String text;

    private String authors;

    private String title;

    private String biblio;

    /**
     *
     * @param string
     */
    public CranDocument(String id) {
        this.id = id;
    }

    /**
     *
     * @param string
     * @param string1
     * @param string3
     * @param string2
     * @param string4
     */
    public CranDocument(String id, String text, String authors, String title, String biblio) {
        this.id = id;
        this.text = text;
        this.authors = authors;
        this.title = title;
        this.biblio = biblio;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CranDocument other = (CranDocument) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    /**
     *
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public String getText() {
        return text;
    }

    /**
     *
     * @param text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     *
     * @return
     */
    public String getAuthors() {
        return authors;
    }

    /**
     *
     * @param authors
     */
    public void setAuthors(String authors) {
        this.authors = authors;
    }

    /**
     *
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return
     */
    public String getBiblio() {
        return biblio;
    }

    /**
     *
     * @param biblio
     */
    public void setBiblio(String biblio) {
        this.biblio = biblio;
    }

}
