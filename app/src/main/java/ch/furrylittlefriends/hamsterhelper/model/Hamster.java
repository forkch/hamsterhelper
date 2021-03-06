package ch.furrylittlefriends.hamsterhelper.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.List;

/**
 * Created with love by fork on 30.08.14.
 */
@Table(name = "Hamsters")
public class Hamster extends Model implements Serializable, Comparable<Hamster> {

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

    @Expose
    @Column(name = "image")
    private String image;

    @Column(name = "mother")
    private Hamster mother;

    @Column(name = "father")
    private Hamster father;

    @Column(name = "tempImageUri")
    private String tempImageUri;

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


    @Override
    public int compareTo(Hamster another) {
        return new CompareToBuilder().append(name, another.name).build();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTempImageUri() {
        return tempImageUri;
    }

    public void setTempImageUri(String tempUri) {
        this.tempImageUri = tempUri;
    }
}
