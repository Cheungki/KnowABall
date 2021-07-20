package comsoftware.engine.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "team")
public class TeamBaseInfo {
    @Id
    private int id;

    private String imgURL;
    private String url;

    private int birthYear;

    private String country;

    private String city;

    private String stadium;

    private int audience;

    private String phone;

    private String email;

    private String name;

    private String englishName;

    private String address;

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public int getId() {
        return id;
    }

    public String getEnglishName() {
        return englishName;
    }

    public String getCountry() {
        return country;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public int getAudience() {
        return audience;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getStadium() {
        return stadium;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAudience(int audience) {
        this.audience = audience;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setStadium(String stadium) {
        this.stadium = stadium;
    }
}
