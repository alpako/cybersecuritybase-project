package sec.project.domain;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;

@Entity
public class Signup extends AbstractPersistable<Long> {

    private String userId;
    private String name;
    private String address;
    private String course;

    public Signup() {
        super();
    }

    public Signup(String userId, String name, String address, String course) {
        this.userId = userId;
        this.name = name;
        this.address = address;
        this.course = course;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }
}
