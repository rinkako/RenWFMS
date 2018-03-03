/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing;

import org.sysu.renResourcing.principle.RPrinciple;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Author: Rinkako
 * Date  : 2018/2/2
 * Usage : This class is inherited by all selector implement class
 *         like allocator, constraint and filter, for their data set
 *         maintaining and comparing.
 */
public abstract class RSelector implements Comparable<RSelector>, Serializable {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Resourcing principle.
     */
    protected RPrinciple principle;

    /**
     * Create a new Selector.
     */
    public RSelector() { }

    /**
     * Create a new Selector.
     * @param id unique id for selector fetching
     * @param type type name string
     */
    public RSelector(String id, String type) {
        this(id, type, "");
    }

    /**
     * Create a new Selector.
     * @param id unique id for selector fetching
     * @param type type name string
     * @param description selector description text
     */
    public RSelector(String id, String type, String description) {
        this(id, type, description, null);
    }

    /**
     * Create a new Selector.
     * @param id unique id for selector fetching
     * @param type type name string
     * @param description selector description text
     * @param args parameter dictionary in HashMap
     */
    public RSelector(String id, String type, String description, HashMap<String, String> args) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.argsDict = args;
    }

    /**
     * Binding principle to this selector.
     * @param principle principle package
     */
    public void BindingPrinciple(RPrinciple principle) {
        this.principle = principle;
        this.ApplyPrinciple();
    }

    /**
     * Add a new parameter key value pair to parameter dictionary.
     * @param key key of param
     * @param value value of param
     * @return whether replace exist param
     */
    public boolean AddOrUpdateParam(String key, String value) {
        return this.argsDict.put(key, value) != null;
    }

    /**
     * Get value in the parameter dictionary by a key.
     * @param key key to fetch
     * @return value of this key in parameter dictionary, null if not exist
     */
    public String RetrieveParam(String key) {
        return this.argsDict.get(key);
    }

    /**
     * Comparable equal function.
     * @param other another object to compare
     * @return true if equal
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof RSelector) &&
                ((RSelector) other).getId().equals(this.id);
    }

    /**
     * Get hash code of this selector.
     * @return hash code
     */
    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    /**
     * Comparable compare function.
     * @param other another object to compare
     * @return compare result int
     */
    @Override
    public int compareTo(RSelector other) {
        return this.id != null ? this.id.compareTo(other.getId()) : 1;
    }


    /**
     * Get selector unique id.
     * @return id string
     */
    public String getId() {
        return id;
    }

    /**
     * Set selector unique id.
     * @param id id to be set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get type name.
     * @return type name string
     */
    public String getType() {
        return type;
    }

    /**
     * Get description of this selector.
     * @return description string
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set description of this selector.
     * @param description description to be set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get argument dictionary of this selector.
     * @return HashMap of selector argument dictionary
     */
    public HashMap<String, String> getArgsDict() {
        return argsDict;
    }

    /**
     * Set argument dictionary of this selector.
     * @param argsDict HashMap of selector argument dictionary to be set
     */
    public void setArgsDict(HashMap<String, String> argsDict) {
        this.argsDict = argsDict;
    }

    /**
     * Apply principle to configure selector.
     */
    protected abstract void ApplyPrinciple();

    /**
     * Evaluate an ArrayList of HashSet according to a operation description string.
     * Operation descriptor using `|` for set union and `&` for set intersect.
     * @param setList list of set to be calculated
     * @param opDescriptor operation descriptor, length must be setList count minus one
     * @param <Ty> Type of data in set
     * @return calculated result set
     */
    protected <Ty> HashSet<Ty> Evaluate(ArrayList<HashSet<Ty>> setList, String opDescriptor) {
        if (setList == null || setList.isEmpty()) {
            return new HashSet<>();
        }
        if (setList.size() > 1) {
            for (char c : opDescriptor.toCharArray()) {
                if (c == '&') {
                    setList.set(0, this.Intersection(setList.get(0), setList.get(1)));
                    setList.remove(1);
                } else if (c == '|') {
                    setList.set(0, this.Union(setList.get(0), setList.get(1)));
                    setList.remove(1);
                }
                if (setList.size() == 1) {
                    break;
                }
            }
        }
        return setList.get(0);
    }

    /**
     * Evaluate two sets by intersection.
     * @param setA left set
     * @param setB right set
     * @param <Ty> Type of data in set
     * @return calculated result set
     */
    protected <Ty> HashSet<Ty> Intersection(HashSet<Ty> setA, HashSet<Ty> setB) {
        HashSet<Ty> retSet = new HashSet<>();
        for (Ty t : setA) {
            if (setB.contains(t)) {
                retSet.add(t);
            }
        }
        return retSet;
    }

    /**
     * Evaluate two sets by union.
     * @param setA left set
     * @param setB right set
     * @param <Ty> Type of data in set
     * @return calculated result set
     */
    protected <Ty> HashSet<Ty> Union(HashSet<Ty> setA, HashSet<Ty> setB) {
        HashSet<Ty> retSet = new HashSet<>();
        retSet.addAll(setA);
        retSet.addAll(setB);
        return retSet;
    }

    /**
     * id of this selector, should be unique.
     */
    protected String id;

    /**
     * Type of this selector.
     */
    protected String type;

    /**
     * Description text for this selector.
     */
    protected String description;

    /**
     * A dictionary to store parameters for this selector.
     */
    protected HashMap<String, String> argsDict = new HashMap<>();
}
