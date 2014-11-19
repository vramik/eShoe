package com.issuetracker.model;

import com.github.holmistr.esannotations.indexing.annotations.Field;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author mgottval
 */
@Entity
public class Comment implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    @Field
    private String content;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date created;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date updated;
    
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Permission viewPermission;
    
    private String author;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreated() {
        return created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setCreated(Date date) {
        this.created = new Date(date.getTime());
    }

    @PrePersist
    public void setCreationDate() {
        this.created = new Date();
    }

    @PreUpdate
    public void setUpdatedDate() {
        this.updated = new Date();
    }

    public void setUpdated(Date date) {
        this.updated = new Date(date.getTime());
    }
    
    public Permission getViewPermission() {
        return viewPermission;
    }

    public void setViewPermission(Permission viewPermission) {
        this.viewPermission = viewPermission;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Comment other = (Comment) obj;
        if (!Objects.equals(this.id, other.id) && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return !((this.content == null) ? (other.content != null) : !this.content.equals(other.content));
    }

    @Override
    public String toString() {
        return "com.issuetracker.model.Comment[ id=" + id + " ]";
    }
}
