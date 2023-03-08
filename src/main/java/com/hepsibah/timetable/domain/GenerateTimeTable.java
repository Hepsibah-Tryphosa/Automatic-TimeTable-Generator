package com.hepsibah.timetable.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A GenerateTimeTable.
 */
@Entity
@Table(name = "generate_time_table")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class GenerateTimeTable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "wok_days")
    private Integer wokDays;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public GenerateTimeTable id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getWokDays() {
        return this.wokDays;
    }

    public GenerateTimeTable wokDays(Integer wokDays) {
        this.setWokDays(wokDays);
        return this;
    }

    public void setWokDays(Integer wokDays) {
        this.wokDays = wokDays;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GenerateTimeTable)) {
            return false;
        }
        return id != null && id.equals(((GenerateTimeTable) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GenerateTimeTable{" +
            "id=" + getId() +
            ", wokDays=" + getWokDays() +
            "}";
    }
}
