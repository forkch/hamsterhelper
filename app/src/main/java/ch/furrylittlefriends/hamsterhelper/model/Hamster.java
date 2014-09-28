package ch.furrylittlefriends.hamsterhelper.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by fork on 30.08.14.
 */
public class Hamster {

    @Expose
    private String name;
    @Expose
    private boolean male;
    @Expose
    private String gencode;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("__v")
    @Expose
    private int v;
    private int weight;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Hamster withName(String name) {
        this.name = name;
        return this;
    }

    public boolean isMale() {
        return male;
    }

    public void setMale(boolean male) {
        this.male = male;
    }

    public Hamster withMale(boolean male) {
        this.male = male;
        return this;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Hamster withId(String id) {
        this.id = id;
        return this;
    }

    public int getV() {
        return v;
    }

    public void setV(int v) {
        this.v = v;
    }

    public Hamster withV(int v) {
        this.v = v;
        return this;
    }

    public String getGencode() {
        return gencode;
    }

    public void setGencode(String gencode) {
        this.gencode = gencode;
    }

    public Hamster withGencode(String gencode) {
        this.gencode = gencode;
        return this;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }
}
