package com.issuetracker.model;

import static com.issuetracker.web.Constants.JPATablePreffix;
import javax.persistence.*;
import java.io.Serializable;

/**
 *
 * @author mgottval
 */
@Entity
@Table(name = JPATablePreffix + "Transition")
public class Transition implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne
    private Status fromStatus;
    @ManyToOne
    private Status toStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }    
    
     public Status getFromStatus() {
        return fromStatus;
    }

    public void setFromStatus(Status fromStatus) {
        this.fromStatus = fromStatus;
    }

    public Status getToStatus() {
        return toStatus;
    }

    public void setToStatus(Status toStatus) {
        this.toStatus = toStatus;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {      
        if (!(object instanceof Transition)) {
            return false;
        }
        Transition other = (Transition) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return "com.issuetracker.model.Transition[ id=" + id + " ]";
    }
    
}
