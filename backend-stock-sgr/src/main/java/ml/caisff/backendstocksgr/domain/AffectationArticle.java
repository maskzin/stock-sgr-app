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

    @Column
    private String reference;

    @Column
    private LocalDate validAt;

    @Column
    private Boolean epave;

    @Column
    private boolean isValidity;

    @Column
    private boolean libere;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "update_at")
    private LocalDate updateAt;

    @ManyToOne
    @JsonIgnoreProperties(value = { "receptionArticles", "affectationArticles", "affectations", "categorie" }, allowSetters = true)
    private Article article;

    @ManyToMany(mappedBy = "affectationArticles",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "affectationArticles", "article" }, allowSetters = true)
    private Set<Affectation> affectations = new HashSet<>();

    @OneToMany(mappedBy = "affectationArticle")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = {"affectationArticle"}, allowSetters = true)
    private Set<Liberation> liberations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    @PrePersist
    public void onCreated()
    {
        this.createdAt=LocalDate.now();
        this.updateAt=LocalDate.now();
        this.isAffectation=false;
    }

    @PreUpdate
    public void onUpdated()
    {
        this.updateAt=LocalDate.now();
    }

    public Long getId() {
        return this.id;
    }

    public AffectationArticle id(Long id) {
        this.setId(id);
        return this;
    }

    public Boolean getEpave()
    {
        return this.epave;
    }

    public void setEpave(Boolean epave){
        this.epave=epave;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getIsAffectation() {
        return this.isAffectation;
    }


    public void setIsValidity(boolean isValidity) {
        this.isValidity = isValidity;
    }

    public Boolean getIsValidity() {
        return this.isValidity;
    }


    public void setLibere(boolean libere) {
        this.libere = libere;
    }

    public Boolean getLibere() {
        return this.libere;
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

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getReference() {
        return this.reference;
    }

    public AffectationArticle reference(String reference) {
        this.setReference(reference);
        return this;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }


    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public LocalDate getValidAt() {
        return this.validAt;
    }
    public void setValidAt(LocalDate validAt) {
        this.validAt = validAt;
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


    public Set<Liberation> getLiberations() {
        return this.liberations;
    }

    public void setLiberations(Set<Liberation> liberations) {
        if (this.liberations != null) {
            this.liberations.forEach(i -> i.setAffectationArticle(null));
        }
        if (liberations != null) {
            liberations.forEach(i -> i.setAffectationArticle(this));
        }
        this.liberations = liberations;
    }

    public AffectationArticle liberations(Set<Liberation> liberations) {
        this.setLiberations(liberations);
        return this;
    }

    public AffectationArticle addLiberation(Liberation liberation) {
        this.liberations.add(liberation);
        liberation.setAffectationArticle(this);
        return this;
    }

    public AffectationArticle removeLiberation(Liberation liberation) {
        this.liberations.remove(liberation);
        liberation.setAffectationArticle(null);
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
