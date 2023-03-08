package com.hepsibah.timetable.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Lecturer.
 */
@Entity
@Table(name = "lecturer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Lecturer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$")
    @Column(name = "email_id", length = 50, nullable = false, unique = true)
    private String emailId;

    @NotNull
    @Size(max = 50)
    @Pattern(regexp = "^[A-Za-z0-9? ]+$")
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Size(max = 50)
    @Column(name = "emp_id", length = 50)
    private String empId;

    @Column(name = "exp_years")
    private Integer expYears;

    @ManyToMany
    @JoinTable(
        name = "rel_lecturer__subject",
        joinColumns = @JoinColumn(name = "lecturer_id"),
        inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "lecturers", "courses", "semisters" }, allowSetters = true)
    private Set<Subject> subjects = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Lecturer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmailId() {
        return this.emailId;
    }

    public Lecturer emailId(String emailId) {
        this.setEmailId(emailId);
        return this;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getName() {
        return this.name;
    }

    public Lecturer name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmpId() {
        return this.empId;
    }

    public Lecturer empId(String empId) {
        this.setEmpId(empId);
        return this;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public Integer getExpYears() {
        return this.expYears;
    }

    public Lecturer expYears(Integer expYears) {
        this.setExpYears(expYears);
        return this;
    }

    public void setExpYears(Integer expYears) {
        this.expYears = expYears;
    }

    public Set<Subject> getSubjects() {
        return this.subjects;
    }

    public void setSubjects(Set<Subject> subjects) {
        this.subjects = subjects;
    }

    public Lecturer subjects(Set<Subject> subjects) {
        this.setSubjects(subjects);
        return this;
    }

    public Lecturer addSubject(Subject subject) {
        this.subjects.add(subject);
        subject.getLecturers().add(this);
        return this;
    }

    public Lecturer removeSubject(Subject subject) {
        this.subjects.remove(subject);
        subject.getLecturers().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Lecturer)) {
            return false;
        }
        return id != null && id.equals(((Lecturer) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Lecturer{" +
            "id=" + getId() +
            ", emailId='" + getEmailId() + "'" +
            ", name='" + getName() + "'" +
            ", empId='" + getEmpId() + "'" +
            ", expYears=" + getExpYears() +
            "}";
    }
}
