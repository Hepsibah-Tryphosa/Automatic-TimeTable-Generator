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
 * A Semister.
 */
@Entity
@Table(name = "semister")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Semister implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 10)
    @Column(name = "name", length = 10, nullable = false, unique = true)
    private String name;

    @ManyToMany
    @JoinTable(
        name = "rel_semister__course",
        joinColumns = @JoinColumn(name = "semister_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "subjects", "semisters" }, allowSetters = true)
    private Set<Course> courses = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_semister__subject",
        joinColumns = @JoinColumn(name = "semister_id"),
        inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "lecturers", "courses", "semisters" }, allowSetters = true)
    private Set<Subject> subjects = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Semister id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Semister name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Course> getCourses() {
        return this.courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    public Semister courses(Set<Course> courses) {
        this.setCourses(courses);
        return this;
    }

    public Semister addCourse(Course course) {
        this.courses.add(course);
        course.getSemisters().add(this);
        return this;
    }

    public Semister removeCourse(Course course) {
        this.courses.remove(course);
        course.getSemisters().remove(this);
        return this;
    }

    public Set<Subject> getSubjects() {
        return this.subjects;
    }

    public void setSubjects(Set<Subject> subjects) {
        this.subjects = subjects;
    }

    public Semister subjects(Set<Subject> subjects) {
        this.setSubjects(subjects);
        return this;
    }

    public Semister addSubject(Subject subject) {
        this.subjects.add(subject);
        subject.getSemisters().add(this);
        return this;
    }

    public Semister removeSubject(Subject subject) {
        this.subjects.remove(subject);
        subject.getSemisters().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Semister)) {
            return false;
        }
        return id != null && id.equals(((Semister) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Semister{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
