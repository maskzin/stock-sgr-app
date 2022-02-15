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
 * A Article.
 */
@Entity
@Table(name = "article")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "libelle_article")
    private String libelleArticle;

    @Column(name = "caracteristique")
    private String caracteristique;

    @Column(name = "niveau_alerte")
    private Integer niveauAlerte;

    @Column(name = "commentaire")
    private String commentaire;

    @Column(name = "stock")
    private Long stock;
    @Column
    private String type;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "update_at")
    private LocalDate updateAt;

    @OneToMany(mappedBy = "article",cascade = CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    //@JsonIgnoreProperties(value = { "article", "receptions" }, allowSetters = true)
    private Set<ReceptionArticle> receptionArticles = new HashSet<>();

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "article", "affectations" }, allowSetters = true)
    private Set<AffectationArticle> affectationArticles = new HashSet<>();

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "affectationArticles", "employee", "article" }, allowSetters = true)
    private Set<Affectation> affectations = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "articles" }, allowSetters = true)
    private Categorie categorie;

    @ManyToOne
    @JsonIgnoreProperties(value = { "articles" }, allowSetters = true)
    private Marque marque;

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

    public Article id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleArticle() {
        return this.libelleArticle;
    }

    public Article libelleArticle(String libelleArticle) {
        this.setLibelleArticle(libelleArticle);
        return this;
    }

    public void setLibelleArticle(String libelleArticle) {
        this.libelleArticle = libelleArticle;
    }

    public String getCaracteristique() {
        return this.caracteristique;
    }

    public Article caracteristique(String caracteristique) {
        this.setCaracteristique(caracteristique);
        return this;
    }

    public void setCaracteristique(String caracteristique) {
        this.caracteristique = caracteristique;
    }

    public Integer getNiveauAlerte() {
        return this.niveauAlerte;
    }

    public Article niveauAlerte(Integer niveauAlerte) {
        this.setNiveauAlerte(niveauAlerte);
        return this;
    }

    public void setNiveauAlerte(Integer niveauAlerte) {
        this.niveauAlerte = niveauAlerte;
    }

    public String getCommentaire() {
        return this.commentaire;
    }

    public Article commentaire(String commentaire) {
        this.setCommentaire(commentaire);
        return this;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }



    public String getType() {
        return this.type;
    }
    public Article type(String type) {
        this.setType(type);
        return this;
    }
    public void setType(String type) {
        this.type = type;
    }

    public Long getStock() {
        return this.stock;
    }

    public Article stock(Long stock) {
        this.setStock(stock);
        return this;
    }

    public void setStock(Long stock) {
        this.stock = stock;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public Article createdAt(LocalDate createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdateAt() {
        return this.updateAt;
    }

    public Article updateAt(LocalDate updateAt) {
        this.setUpdateAt(updateAt);
        return this;
    }

    public void setUpdateAt(LocalDate updateAt) {
        this.updateAt = updateAt;
    }

    public Set<ReceptionArticle> getReceptionArticles() {
        return this.receptionArticles;
    }

    public void setReceptionArticles(Set<ReceptionArticle> receptionArticles) {
        if (this.receptionArticles != null) {
            this.receptionArticles.forEach(i -> i.setArticle(null));
        }
        if (receptionArticles != null) {
            receptionArticles.forEach(i -> i.setArticle(this));
        }
        this.receptionArticles = receptionArticles;
    }

    public Article receptionArticles(Set<ReceptionArticle> receptionArticles) {
        this.setReceptionArticles(receptionArticles);
        return this;
    }

    public Article addReceptionArticle(ReceptionArticle receptionArticle) {
        this.receptionArticles.add(receptionArticle);
        receptionArticle.setArticle(this);
        return this;
    }

    public Article removeReceptionArticle(ReceptionArticle receptionArticle) {
        this.receptionArticles.remove(receptionArticle);
        receptionArticle.setArticle(null);
        return this;
    }

    public Set<AffectationArticle> getAffectationArticles() {
        return this.affectationArticles;
    }

    public void setAffectationArticles(Set<AffectationArticle> affectationArticles) {
        if (this.affectationArticles != null) {
            this.affectationArticles.forEach(i -> i.setArticle(null));
        }
        if (affectationArticles != null) {
            affectationArticles.forEach(i -> i.setArticle(this));
        }
        this.affectationArticles = affectationArticles;
    }

    public Article affectationArticles(Set<AffectationArticle> affectationArticles) {
        this.setAffectationArticles(affectationArticles);
        return this;
    }

    public Article addAffectationArticle(AffectationArticle affectationArticle) {
        this.affectationArticles.add(affectationArticle);
        affectationArticle.setArticle(this);
        return this;
    }

    public Article removeAffectationArticle(AffectationArticle affectationArticle) {
        this.affectationArticles.remove(affectationArticle);
        affectationArticle.setArticle(null);
        return this;
    }

    public Set<Affectation> getAffectations() {
        return this.affectations;
    }

    public void setAffectations(Set<Affectation> affectations) {
        if (this.affectations != null) {
            this.affectations.forEach(i -> i.setArticle(null));
        }
        if (affectations != null) {
            affectations.forEach(i -> i.setArticle(this));
        }
        this.affectations = affectations;
    }

    public Article affectations(Set<Affectation> affectations) {
        this.setAffectations(affectations);
        return this;
    }

    public Article addAffectation(Affectation affectation) {
        this.affectations.add(affectation);
        affectation.setArticle(this);
        return this;
    }

    public Article removeAffectation(Affectation affectation) {
        this.affectations.remove(affectation);
        affectation.setArticle(null);
        return this;
    }

    public Categorie getCategorie() {
        return this.categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public Article categorie(Categorie categorie) {
        this.setCategorie(categorie);
        return this;
    }

    public Marque getMarque() {
        return this.marque;
    }

    public void setMarque(Marque marque) {
        this.marque = marque;
    }

    public Article marque(Marque marque) {
        this.setMarque(marque);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Article)) {
            return false;
        }
        return id != null && id.equals(((Article) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Article{" +
            "id=" + getId() +
            ", libelleArticle='" + getLibelleArticle() + "'" +
            ", caracteristique='" + getCaracteristique() + "'" +
            ", niveauAlerte=" + getNiveauAlerte() +
            ", commentaire='" + getCommentaire() + "'" +
            ", stock=" + getStock() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updateAt='" + getUpdateAt() + "'" +
            "}";
    }
}
