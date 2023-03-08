package com.hepsibah.timetable.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CollegeInfo.
 */
@Entity
@Table(name = "college_info")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CollegeInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "name", length = 50, nullable = false, unique = true)
    private String name;

    @NotNull
    @Size(max = 10)
    @Column(name = "contact_no", length = 10, nullable = false)
    private String contactNo;

    @Size(max = 100)
    @Column(name = "address", length = 100)
    private String address;

    @NotNull
    @Size(max = 50)
    @Column(name = "city", length = 50, nullable = false)
    private String city;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CollegeInfo id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public CollegeInfo name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNo() {
        return this.contactNo;
    }

    public CollegeInfo contactNo(String contactNo) {
        this.setContactNo(contactNo);
        return this;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getAddress() {
        return this.address;
    }

    public CollegeInfo address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return this.city;
    }

    public CollegeInfo city(String city) {
        this.setCity(city);
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CollegeInfo)) {
            return false;
        }
        return id != null && id.equals(((CollegeInfo) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CollegeInfo{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", contactNo='" + getContactNo() + "'" +
            ", address='" + getAddress() + "'" +
            ", city='" + getCity() + "'" +
            "}";
    }
}
