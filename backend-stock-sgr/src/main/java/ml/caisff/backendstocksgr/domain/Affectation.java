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
 * A Affectation.
 */
@Entity
@Table(name = "affectation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Affectation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "date_affectation")
    private LocalDate dateAffectation;

    @Column(name = "quantite")
    private Integer quantite;

    @Column(name = "isExterne")
    private boolean isExterne;

    @Column(name = "structure")
    private String structure;

    @Column(name = "affectant")
    private String affectant;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "update_at")
    private LocalDate updateAt;



    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
        name = "rel_affectation__affectation_article",
        joinColumns = @JoinColumn(name = "affectation_id"),
        inverseJoinColumns = @JoinColumn(name = "affectation_article_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "affectations" }, allowSetters = true)
    private Set<AffectationArticle> affectationArticles = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "affectations", "division", "receptions" }, allowSetters = true)
    private Employee employee;

    @ManyToOne
    @JsonIgnoreProperties(value = { "receptionArticles", "affectationArticles", "affectations", "categorie" }, allowSetters = true)
    private Article article;

    @OneToMany(mappedBy = "affectation",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "reception","affectation","fileUpload" }, allowSetters = true)
    private Set<FileUpload> fileUploads = new HashSet<>();

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

    public Affectation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus(){return  this.status;}

    public LocalDate getDateAffectation() {
        return this.dateAffectation;
    }

    public Affectation dateAffectation(LocalDate dateAffectation) {
        this.setDateAffectation(dateAffectation);
        return this;
    }

    public void setDateAffectation(LocalDate dateAffectation) {
        this.dateAffectation = dateAffectation;
    }

    public Integer getQuantite() {
        return this.quantite;
    }

    public Affectation quantite(Integer quantite) {
        this.setQuantite(quantite);
        return this;
    }

    public void setExterne(boolean externe) {
        this.isExterne = externe;
    }


    public boolean getExterne() {
        return this.isExterne;
    }

    public Affectation isExterne(boolean isExterne) {
        this.setExterne(isExterne);
        return this;
    }

    public void setStructure(String structure) {
        this.structure = structure;
    }


    public String getStructure() {
        return this.structure;
    }

    public Affectation structure(String structure) {
        this.setStructure(structure);
        return this;
    }

    public void setAffectant(String affectant) {
        this.affectant = affectant;
    }


    public String getAffectant() {
        return this.affectant;
    }

    public Affectation affectant(String affectant) {
        this.setAffectant(affectant);
        return this;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }


    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public Affectation createdAt(LocalDate createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdateAt() {
        return this.updateAt;
    }

    public Affectation updateAt(LocalDate updateAt) {
        this.setUpdateAt(updateAt);
        return this;
    }

    public void setUpdateAt(LocalDate updateAt) {
        this.updateAt = updateAt;
    }

    public Set<AffectationArticle> getAffectationArticles() {
        return this.affectationArticles;
    }

    public void setAffectationArticles(Set<AffectationArticle> affectationArticles) {
        this.affectationArticles = affectationArticles;
    }

    public Affectation affectationArticles(Set<AffectationArticle> affectationArticles) {
        this.setAffectationArticles(affectationArticles);
        return this;
    }

    public Affectation addAffectationArticle(AffectationArticle affectationArticle) {
        this.affectationArticles.add(affectationArticle);
        affectationArticle.getAffectations().add(this);
        return this;
    }

    public Affectation removeAffectationArticle(AffectationArticle affectationArticle) {
        this.affectationArticles.remove(affectationArticle);
        affectationArticle.getAffectations().remove(this);
        return this;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Affectation employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    public Article getArticle() {
        return this.article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Affectation article(Article article) {
        this.setArticle(article);
        return this;
    }


    public Set<FileUpload> getFileUploads() {
        return this.fileUploads;
    }

    public void setFileUploads(Set<FileUpload> fileUploads) {
        if (this.fileUploads != null) {
            this.fileUploads.forEach(i -> i.setAffectation(null));
        }
        if (fileUploads != null) {
            fileUploads.forEach(i -> i.setAffectation(this));
        }
        this.fileUploads = fileUploads;
    }

    public Affectation fileUploads(Set<FileUpload> fileUploads) {
        this.setFileUploads(fileUploads);
        return this;
    }

    public Affectation addFileUpload(FileUpload fileUpload) {
        this.fileUploads.add(fileUpload);
        fileUpload.setAffectation(this);
        return this;
    }

    public Affectation removeFileUpload(FileUpload fileUpload) {
        this.fileUploads.remove(fileUpload);
        fileUpload.setAffectation(null);
        return this;
    }

    // public Affectation fileUpload(FileUpload fileUpload) {
    //     this.setFileUpload(fileUpload);
    //     return this;
    // }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Affectation)) {
            return false;
        }
        return id != null && id.equals(((Affectation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Affectation{" +
            "id=" + getId() +
            ", dateAffectation='" + getDateAffectation() + "'" +
            ", quantite=" + getQuantite() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updateAt='" + getUpdateAt() + "'" +
            "}";
    }
}
