/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package di.uniba.it.mri2324.lucene.cran.rocchio.entity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author pierpaolo
 */
public class BoW {

    private final Map<String, Float> bow = new HashMap<>();

    public void putWord(String word, float weight) {
        bow.put(word, weight);
    }

    public Float getWeight(String word) {
        return bow.get(word);
    }

    public Iterator<String> getWordsIterator() {
        return bow.keySet().iterator();
    }

    public Set<String> getWords() {
        return bow.keySet();
    }

    public Set<Map.Entry<String, Float>> getEntries() {
        return bow.entrySet();
    }

    public Iterator<Map.Entry<String, Float>> getEntriesIterator() {
        return bow.entrySet().iterator();
    }

    public Collection<Float> getWeights() {
        return bow.values();
    }
    
    public int size() {
        return bow.size();
    }

    @Override
    public String toString() {
        return "BoW{" + "bow=" + bow + '}';
    }

}
