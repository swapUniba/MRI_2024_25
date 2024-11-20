/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package di.uniba.it.mri2324.lucene.cran.rocchio.entity;

import java.util.Objects;

/**
 *
 * @author pierpaolo
 */
public class TermEntry implements Comparable<TermEntry> {

    private String word;

    private float weight;

    public TermEntry(String word, float weight) {
        this.word = word;
        this.weight = weight;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.word);
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
        final TermEntry other = (TermEntry) obj;
        if (!Objects.equals(this.word, other.word)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TermEntry{" + "word=" + word + ", weight=" + weight + '}';
    }

    @Override
    public int compareTo(TermEntry o) {
        return Float.compare(weight, o.weight);
    }

}
