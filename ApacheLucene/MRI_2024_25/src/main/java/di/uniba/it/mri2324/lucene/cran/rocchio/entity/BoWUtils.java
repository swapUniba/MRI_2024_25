/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package di.uniba.it.mri2324.lucene.cran.rocchio.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author pierpaolo
 */
public class BoWUtils {

    // built the centroid
    public static BoW average(BoW... bows) {
        BoW c = new BoW();
        for (BoW bow : bows) {
            Set<String> words = bow.getWords();
            for (String word : words) {
                Float w = c.getWeight(word);
                if (w == null) {
                    c.putWord(word, bow.getWeight(word));
                } else {
                    c.putWord(word, w + bow.getWeight(word));
                }
            }
        }
        Iterator<Map.Entry<String, Float>> entries = c.getEntriesIterator();
        while (entries.hasNext()) {
            Map.Entry<String, Float> e = entries.next();
            e.setValue(e.getValue() / (float) bows.length);
        }
        return c;
    }
    
    public static BoW add(BoW... bows) {
        BoW c = new BoW();
        for (BoW bow : bows) {
            Set<String> words = bow.getWords();
            for (String word : words) {
                Float w = c.getWeight(word);
                if (w == null) {
                    c.putWord(word, bow.getWeight(word));
                } else {
                    c.putWord(word, w + bow.getWeight(word));
                }
            }
        }
        return c;
    }

    public static float norma2(BoW bow) {
        double n = 0;
        Iterator<Map.Entry<String, Float>> entries = bow.getEntriesIterator();
        while (entries.hasNext()) {
            Map.Entry<String, Float> e = entries.next();
            n += Math.pow(e.getValue(), 2);
        }
        return (float) Math.sqrt(n);
    }

    public static void normalize(BoW bow) {
        float norma = norma2(bow);
        scalarProduct(1 / norma, bow);
    }

    //calcola la similarità del coseno tra due BoW
    public static float sim(BoW bow1, BoW bow2) {
        Set<String> keyset = new HashSet<>(bow1.getWords());
        keyset.retainAll(bow2.getWords()); //intersection
        float ip = 0;
        for (String word : keyset) {
            ip += bow1.getWeight(word) * bow2.getWeight(word);
        }
        return ip / (norma2(bow1) * norma2(bow2));
    }

    //multiplca gli elementi di bow per uno scalare
    public static void scalarProduct(float scalar, BoW bow) {
        Iterator<Map.Entry<String, Float>> entries = bow.getEntriesIterator();
        while (entries.hasNext()) {
            Map.Entry<String, Float> e = entries.next();
            e.setValue(scalar * e.getValue());
        }
    }

    public static List<TermEntry> getTopTerms(BoW bow, int k) {
        List<TermEntry> list = new ArrayList<>();
        Iterator<Map.Entry<String, Float>> entries = bow.getEntriesIterator();
        while (entries.hasNext()) {
            Map.Entry<String, Float> e = entries.next();
            list.add(new TermEntry(e.getKey(), e.getValue()));
        }
        Collections.sort(list, Collections.reverseOrder());
        if (k < list.size()) {
            return list.subList(0, k);
        } else {
            return list;
        }
    }

    public static void main(String[] args) {
        BoW bow1 = new BoW();
        bow1.putWord("a", 0.43f);
        bow1.putWord("b", 0.62f);
        bow1.putWord("c", 0.89f);
        BoW bow2 = new BoW();
        bow2.putWord("a", 0.24f);
        bow2.putWord("c", 0.56f);
        bow2.putWord("d", 0.75f);
        bow2.putWord("e", 0.32f);

        BoW abow = average(bow1, bow2);
        System.out.println(abow);
        scalarProduct(0.5f, abow);
        System.out.println(abow);
        System.out.println(sim(bow1, bow2));
        System.out.println(getTopTerms(abow, 3));
    }
}
