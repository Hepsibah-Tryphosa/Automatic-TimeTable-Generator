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
 * A Course.
 */
@Entity
@Table(name = "course")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Course implements Serializable {

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
        name = "rel_course__subject",
        joinColumns = @JoinColumn(name = "course_id"),
        inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "lecturers", "courses", "semisters" }, allowSetters = true)
    private Set<Subject> subjects = new HashSet<>();

    @ManyToMany(mappedBy = "courses")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "courses", "subjects" }, allowSetters = true)
    private Set<Semister> semisters = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Course id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Course name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Subject> getSubjects() {
        return this.subjects;
    }

    public void setSubjects(Set<Subject> subjects) {
        this.subjects = subjects;
    }

    public Course subjects(Set<Subject> subjects) {
        this.setSubjects(subjects);
        return this;
    }

    public Course addSubject(Subject subject) {
        this.subjects.add(subject);
        subject.getCourses().add(this);
        return this;
    }

    public Course removeSubject(Subject subject) {
        this.subjects.remove(subject);
        subject.getCourses().remove(this);
        return this;
    }

    public Set<Semister> getSemisters() {
        return this.semisters;
    }

    public void setSemisters(Set<Semister> semisters) {
        if (this.semisters != null) {
            this.semisters.forEach(i -> i.removeCourse(this));
        }
        if (semisters != null) {
            semisters.forEach(i -> i.addCourse(this));
        }
        this.semisters = semisters;
    }

    public Course semisters(Set<Semister> semisters) {
        this.setSemisters(semisters);
        return this;
    }

    public Course addSemister(Semister semister) {
        this.semisters.add(semister);
        semister.getCourses().add(this);
        return this;
    }

    public Course removeSemister(Semister semister) {
        this.semisters.remove(semister);
        semister.getCourses().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Course)) {
            return false;
        }
        return id != null && id.equals(((Course) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Course{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
