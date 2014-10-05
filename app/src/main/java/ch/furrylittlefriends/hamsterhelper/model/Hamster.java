package ch.furrylittlefriends.hamsterhelper.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Update;
import com.google.common.collect.Lists;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.List;

/**
 * Created by fork on 30.08.14.
 */
@Table(name = "Hamsters")
public class Hamster extends Model implements Serializable {

    private static final long serialVersionUID = 0L;

    @SerializedName("_id")
    @Expose
    @Column(name = "serverId", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
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

    @SerializedName("motherId")
    @Expose
    @Column(name = "motherServerId")
    private String motherServerId;

    @SerializedName("fatherId")
    @Expose
    @Column(name = "fatherServerId")
    private String fatherServerId;

    @Column(name="mother")
    private Hamster mother;

    @Column(name="father")
    private Hamster father;

    // This method is optional, does not affect the foreign key creation.
    public List<Hamster> children() {
        List<Hamster> mother1 = getMany(Hamster.class, "mother");
        List<Hamster> father1 = getMany(Hamster.class, "father");
        mother1.addAll(father1);
        return mother1;
    }

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

    public String getMotherServerId() {
        return motherServerId;
    }

    public void setMotherServerId(String motherServerId) {
        this.motherServerId = motherServerId;
    }

    public String getFatherServerId() {
        return fatherServerId;
    }

    public void setFatherServerId(String fatherServerId) {
        this.fatherServerId = fatherServerId;
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

    public boolean hasChildren() {
        return children().size() > 0;
    }


}
