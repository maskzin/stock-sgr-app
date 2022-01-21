package ml.caisff.backendstocksgr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AffectationArticle.
 */
@Entity
@Table(name = "affectation_article")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AffectationArticle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "is_affectation")
    private Boolean isAffectation;

    @Column(name = "quantite")
    private Integer quantite;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "update_at")
    private LocalDate updateAt;

    @ManyToOne
    @JsonIgnoreProperties(value = { "receptionArticles", "affectationArticles", "affectations", "categorie" }, allowSetters = true)
    private Article article;

    @ManyToMany(mappedBy = "affectationArticles")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "affectationArticles", "employee", "article" }, allowSetters = true)
    private Set<Affectation> affectations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AffectationArticle id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getIsAffectation() {
        return this.isAffectation;
    }

    public AffectationArticle isAffectation(Boolean isAffectation) {
        this.setIsAffectation(isAffectation);
        return this;
    }

    public void setIsAffectation(Boolean isAffectation) {
        this.isAffectation = isAffectation;
    }

    public Integer getQuantite() {
        return this.quantite;
    }

    public AffectationArticle quantite(Integer quantite) {
        this.setQuantite(quantite);
        return this;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public AffectationArticle createdAt(LocalDate createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdateAt() {
        return this.updateAt;
    }

    public AffectationArticle updateAt(LocalDate updateAt) {
        this.setUpdateAt(updateAt);
        return this;
    }

    public void setUpdateAt(LocalDate updateAt) {
        this.updateAt = updateAt;
    }

    public Article getArticle() {
        return this.article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public AffectationArticle article(Article article) {
        this.setArticle(article);
        return this;
    }

    public Set<Affectation> getAffectations() {
        return this.affectations;
    }

    public void setAffectations(Set<Affectation> affectations) {
        if (this.affectations != null) {
            this.affectations.forEach(i -> i.removeAffectationArticle(this));
        }
        if (affectations != null) {
            affectations.forEach(i -> i.addAffectationArticle(this));
        }
        this.affectations = affectations;
    }

    public AffectationArticle affectations(Set<Affectation> affectations) {
        this.setAffectations(affectations);
        return this;
    }

    public AffectationArticle addAffectation(Affectation affectation) {
        this.affectations.add(affectation);
        affectation.getAffectationArticles().add(this);
        return this;
    }

    public AffectationArticle removeAffectation(Affectation affectation) {
        this.affectations.remove(affectation);
        affectation.getAffectationArticles().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AffectationArticle)) {
            return false;
        }
        return id != null && id.equals(((AffectationArticle) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AffectationArticle{" +
            "id=" + getId() +
            ", isAffectation='" + getIsAffectation() + "'" +
            ", quantite=" + getQuantite() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updateAt='" + getUpdateAt() + "'" +
            "}";
    }
}
