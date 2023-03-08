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
 * A Subject.
 */
@Entity
@Table(name = "subject")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Subject implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "name", length = 50, nullable = false, unique = true)
    private String name;

    @Column(name = "req_hrs")
    private Integer reqHrs;

    @ManyToMany(mappedBy = "subjects")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "subjects" }, allowSetters = true)
    private Set<Lecturer> lecturers = new HashSet<>();

    @ManyToMany(mappedBy = "subjects")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "subjects", "semisters" }, allowSetters = true)
    private Set<Course> courses = new HashSet<>();

    @ManyToMany(mappedBy = "subjects")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "courses", "subjects" }, allowSetters = true)
    private Set<Semister> semisters = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Subject id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Subject name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getReqHrs() {
        return this.reqHrs;
    }

    public Subject reqHrs(Integer reqHrs) {
        this.setReqHrs(reqHrs);
        return this;
    }

    public void setReqHrs(Integer reqHrs) {
        this.reqHrs = reqHrs;
    }

    public Set<Lecturer> getLecturers() {
        return this.lecturers;
    }

    public void setLecturers(Set<Lecturer> lecturers) {
        if (this.lecturers != null) {
            this.lecturers.forEach(i -> i.removeSubject(this));
        }
        if (lecturers != null) {
            lecturers.forEach(i -> i.addSubject(this));
        }
        this.lecturers = lecturers;
    }

    public Subject lecturers(Set<Lecturer> lecturers) {
        this.setLecturers(lecturers);
        return this;
    }

    public Subject addLecturer(Lecturer lecturer) {
        this.lecturers.add(lecturer);
        lecturer.getSubjects().add(this);
        return this;
    }

    public Subject removeLecturer(Lecturer lecturer) {
        this.lecturers.remove(lecturer);
        lecturer.getSubjects().remove(this);
        return this;
    }

    public Set<Course> getCourses() {
        return this.courses;
    }

    public void setCourses(Set<Course> courses) {
        if (this.courses != null) {
            this.courses.forEach(i -> i.removeSubject(this));
        }
        if (courses != null) {
            courses.forEach(i -> i.addSubject(this));
        }
        this.courses = courses;
    }

    public Subject courses(Set<Course> courses) {
        this.setCourses(courses);
        return this;
    }

    public Subject addCourse(Course course) {
        this.courses.add(course);
        course.getSubjects().add(this);
        return this;
    }

    public Subject removeCourse(Course course) {
        this.courses.remove(course);
        course.getSubjects().remove(this);
        return this;
    }

    public Set<Semister> getSemisters() {
        return this.semisters;
    }

    public void setSemisters(Set<Semister> semisters) {
        if (this.semisters != null) {
            this.semisters.forEach(i -> i.removeSubject(this));
        }
        if (semisters != null) {
            semisters.forEach(i -> i.addSubject(this));
        }
        this.semisters = semisters;
    }

    public Subject semisters(Set<Semister> semisters) {
        this.setSemisters(semisters);
        return this;
    }

    public Subject addSemister(Semister semister) {
        this.semisters.add(semister);
        semister.getSubjects().add(this);
        return this;
    }

    public Subject removeSemister(Semister semister) {
        this.semisters.remove(semister);
        semister.getSubjects().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Subject)) {
            return false;
        }
        return id != null && id.equals(((Subject) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Subject{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", reqHrs=" + getReqHrs() +
            "}";
    }
}
