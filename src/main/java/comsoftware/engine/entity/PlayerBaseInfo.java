package comsoftware.engine.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerBaseInfo {

    @Id
    private int id;

    private int teamId;

    private String url;

    private String club;

    private String country;

    private String role;

    private int height;

    private int weight;

    private int age;

    private int number;

    private String birthday;

    private int preferedFoot;

    private String name;

    private String englishName;

    private int capablity;

    private int speed;

    private int strength;

    private int defence;

    private int dribbling;

    private int pass;

    private int shoot;

    public String getUrl() {
        return url;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public int getCapablity() {
        return capablity;
    }

    public int getDefence() {
        return defence;
    }

    public int getDribbling() {
        return dribbling;
    }

    public int getHeight() {
        return height;
    }

    public int getNumber() {
        return number;
    }

    public int getPass() {
        return pass;
    }

    public int getPreferedFoot() {
        return preferedFoot;
    }

    public int getShoot() {
        return shoot;
    }

    public int getSpeed() {
        return speed;
    }

    public int getStrength() {
        return strength;
    }

    public int getTeamId() {
        return teamId;
    }

    public int getWeight() {
        return weight;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getClub() {
        return club;
    }

    public String getCountry() {
        return country;
    }

    public String getEnglishName() {
        return englishName;
    }

    public String getRole() {
        return role;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setCapablity(int capablity) {
        this.capablity = capablity;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setDefence(int defence) {
        this.defence = defence;
    }

    public void setDribbling(int dribbling) {
        this.dribbling = dribbling;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setPass(int pass) {
        this.pass = pass;
    }

    public void setPreferedFoot(int preferedFoot) {
        this.preferedFoot = preferedFoot;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setShoot(int shoot) {
        this.shoot = shoot;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
