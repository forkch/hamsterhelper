package ch.furrylittlefriends.hamsterhelper.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

import java.io.Serializable;

/**
 * Created by fork on 30.08.14.
 */
@Table(name = "Hamsters")
public class Hamster extends Model implements Serializable {
    @SerializedName("_id")
    @Expose
    @Column(name = "serverId")
    private String serverId;

    @SerializedName("__v")
    @Expose
    @Column(name = "version")
    private int version;

    @Expose
    @Column(name = "name")
    private String name;
    @Expose
    @Column(name = "male")
    private boolean male;
    @Expose
    @Column(name = "gencode")
    private String gencode;

    @Expose
    @Column(name = "weight")
    private double weight;
    @Expose
    @Column(name = "birthday")
    private DateTime birthday;

    @Expose
    @Column(name = "motherId")
    private String motherId;
    @Expose
    @Column(name = "fatherId")
    private String fatherId;

    private Hamster mother;

    private Hamster father;

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isMale() {
        return male;
    }

    public void setMale(boolean male) {
        this.male = male;
    }


    public String getGencode() {
        return gencode;
    }

    public void setGencode(String gencode) {
        this.gencode = gencode;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }

    public void setBirthday(DateTime birthday) {
        this.birthday = birthday;
    }

    public DateTime getBirthday() {
        return birthday;
    }

    public String getMotherId() {
        return motherId;
    }

    public void setMotherId(String motherId) {
        this.motherId = motherId;
    }

    public String getFatherId() {
        return fatherId;
    }

    public void setFatherId(String fatherId) {
        this.fatherId = fatherId;
    }

    public Hamster getMother() {
        return mother;
    }

    public void setMother(Hamster mother) {
        this.mother = mother;
    }

    public Hamster getFather() {
        return father;
    }

    public void setFather(Hamster father) {
        this.father = father;
    }
}
