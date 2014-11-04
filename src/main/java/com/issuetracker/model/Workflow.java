package com.issuetracker.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author mgottval
 */
@Entity
public class Workflow implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transition> transitions;

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
    
    public List<Transition> getTransitions() {
        return transitions;
    }

    public void setTransitions(List<Transition> transitions) {
        this.transitions = transitions;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Workflow)) {
            return false;
        }
        Workflow other = (Workflow) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return "com.issuetracker.model.Workflow[ id=" + id + " ]";
    }
    
}
