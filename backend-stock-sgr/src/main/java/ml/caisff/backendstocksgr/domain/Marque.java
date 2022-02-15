package ml.caisff.backendstocksgr.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Marque.
 */
@Entity
@Table(name = "marque")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Marque implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "libelle")
    private String libelle;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "update_at")
    private LocalDate updateAt;

    @OneToMany(mappedBy = "marque")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "receptionArticles", "affectationArticles", "affectations", "categorie", "marque" },
        allowSetters = true
    )
    private Set<Article> articles = new HashSet<>();


    // jhipster-needle-entity-add-field - JHipster will add fields here

    @PrePersist
    public void onCreated()
    {
        this.createdAt=LocalDate.now();
        this.updateAt=LocalDate.now();
    }

    @PreUpdate
    public void onUpdated()
    {
        this.updateAt=LocalDate.now();
    }

    public Long getId() {
        return this.id;
    }


    public Marque id(Long id) {
        this.setId(id);
        return this;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public Marque createdAt(LocalDate createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdateAt() {
        return this.updateAt;
    }

    public Marque updateAt(LocalDate updateAt) {
        this.setUpdateAt(updateAt);
        return this;
    }

    public void setUpdateAt(LocalDate updateAt) {
        this.updateAt = updateAt;
    }




    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public Marque libelle(String libelle) {
        this.setLibelle(libelle);
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    //ajout des articles dans marque
    public Set<Article> getArticles() {
        return this.articles;
    }

    public void setArticles(Set<Article> articles) {
        if (this.articles != null) {
            this.articles.forEach(i -> i.setMarque(null));
        }
        if (articles != null) {
            articles.forEach(i -> i.setMarque(this));
        }
        this.articles = articles;
    }

    public Marque articles(Set<Article> articles) {
        this.setArticles(articles);
        return this;
    }

    public Marque addArticle(Article article) {
        this.articles.add(article);
        article.setMarque(this);
        return this;
    }

    public Marque removeArticle(Article article) {
        this.articles.remove(article);
        article.setMarque(null);
        return this;
    }


    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Marque)) {
            return false;
        }
        return id != null && id.equals(((Marque) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Marque{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            "}";
    }
}
