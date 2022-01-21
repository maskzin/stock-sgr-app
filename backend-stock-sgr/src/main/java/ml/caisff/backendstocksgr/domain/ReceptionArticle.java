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
 * A ReceptionArticle.
 */
@Entity
@Table(name = "reception_article")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ReceptionArticle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "is_reception")
    private Boolean isReception;

    @Column(name = "quantite")
    private Integer quantite;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "update_at")
    private LocalDate updateAt;

    @ManyToOne
    @JsonIgnoreProperties(value = { "receptionArticles", "affectationArticles", "affectations", "categorie" }, allowSetters = true)
    private Article article;

    @ManyToMany(mappedBy = "receptionArticles", cascade = CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "employees", "receptionArticles", "fournisseur" }, allowSetters = true)
    private Set<Reception> receptions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ReceptionArticle id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getIsReception() {
        return this.isReception;
    }

    public ReceptionArticle isReception(Boolean isReception) {
        this.setIsReception(isReception);
        return this;
    }

    public void setIsReception(Boolean isReception) {
        this.isReception = isReception;
    }

    public Integer getQuantite() {
        return this.quantite;
    }

    public ReceptionArticle quantite(Integer quantite) {
        this.setQuantite(quantite);
        return this;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public ReceptionArticle createdAt(LocalDate createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdateAt() {
        return this.updateAt;
    }

    public ReceptionArticle updateAt(LocalDate updateAt) {
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

    public ReceptionArticle article(Article article) {
        this.setArticle(article);
        return this;
    }

    public Set<Reception> getReceptions() {
        return this.receptions;
    }

    public void setReceptions(Set<Reception> receptions) {
        if (this.receptions != null) {
            this.receptions.forEach(i -> i.removeReceptionArticle(this));
        }
        if (receptions != null) {
            receptions.forEach(i -> i.addReceptionArticle(this));
        }
        this.receptions = receptions;
    }

    public ReceptionArticle receptions(Set<Reception> receptions) {
        this.setReceptions(receptions);
        return this;
    }

    public ReceptionArticle addReception(Reception reception) {
        this.receptions.add(reception);
        reception.getReceptionArticles().add(this);
        return this;
    }

    public ReceptionArticle removeReception(Reception reception) {
        this.receptions.remove(reception);
        reception.getReceptionArticles().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReceptionArticle)) {
            return false;
        }
        return id != null && id.equals(((ReceptionArticle) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReceptionArticle{" +
            "id=" + getId() +
            ", isReception='" + getIsReception() + "'" +
            ", quantite=" + getQuantite() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updateAt='" + getUpdateAt() + "'" +
            "}";
    }
}
