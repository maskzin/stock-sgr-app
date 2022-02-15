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
 * A Reception.
 */
@Entity
@Table(name = "reception")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Reception implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "num_contrat")
    private String numContrat;

    @Column(name = "date_contrat")
    private LocalDate dateContrat;

    @Column(name = "date_reception")
    private LocalDate dateReception;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "update_at")
    private LocalDate updateAt;

    @ManyToMany
    @JoinTable(
        name = "rel_reception__employee",
        joinColumns = @JoinColumn(name = "reception_id"),
        inverseJoinColumns = @JoinColumn(name = "employee_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "affectations", "receptions" }, allowSetters = true)
    private Set<Employee> employees = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
        name = "rel_reception__reception_article",
        joinColumns = @JoinColumn(name = "reception_id"),
        inverseJoinColumns = @JoinColumn(name = "reception_article_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = {"receptions" }, allowSetters = true)
    private Set<ReceptionArticle> receptionArticles = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "receptions" }, allowSetters = true)
    private Fournisseur fournisseur;

    @OneToMany(mappedBy = "reception",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "reception", "affectation" }, allowSetters = true)
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

    public Reception id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumContrat() {
        return this.numContrat;
    }

    public Reception numContrat(String numContrat) {
        this.setNumContrat(numContrat);
        return this;
    }

    public void setNumContrat(String numContrat) {
        this.numContrat = numContrat;
    }

    public LocalDate getDateContrat() {
        return this.dateContrat;
    }

    public Reception dateContrat(LocalDate dateContrat) {
        this.setDateContrat(dateContrat);
        return this;
    }

    public void setDateContrat(LocalDate dateContrat) {
        this.dateContrat = dateContrat;
    }

    public LocalDate getDateReception() {
        return this.dateReception;
    }

    public Reception dateReception(LocalDate dateReception) {
        this.setDateReception(dateReception);
        return this;
    }

    public void setDateReception(LocalDate dateReception) {
        this.dateReception = dateReception;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public Reception createdAt(LocalDate createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdateAt() {
        return this.updateAt;
    }

    public Reception updateAt(LocalDate updateAt) {
        this.setUpdateAt(updateAt);
        return this;
    }

    public void setUpdateAt(LocalDate updateAt) {
        this.updateAt = updateAt;
    }

    public Set<Employee> getEmployees() {
        return this.employees;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }

    public Reception employees(Set<Employee> employees) {
        this.setEmployees(employees);
        return this;
    }

    public Reception addEmployee(Employee employee) {
        this.employees.add(employee);
        employee.getReceptions().add(this);
        return this;
    }

    public Reception removeEmployee(Employee employee) {
        this.employees.remove(employee);
        employee.getReceptions().remove(this);
        return this;
    }

    public Set<ReceptionArticle> getReceptionArticles() {
        return this.receptionArticles;
    }

    public void setReceptionArticles(Set<ReceptionArticle> receptionArticles) {
        this.receptionArticles = receptionArticles;
    }

    public Reception receptionArticles(Set<ReceptionArticle> receptionArticles) {
        this.setReceptionArticles(receptionArticles);
        return this;
    }

    public Reception addReceptionArticle(ReceptionArticle receptionArticle) {
        this.receptionArticles.add(receptionArticle);
        receptionArticle.getReceptions().add(this);
        return this;
    }

    public Reception removeReceptionArticle(ReceptionArticle receptionArticle) {
        this.receptionArticles.remove(receptionArticle);
        receptionArticle.getReceptions().remove(this);
        return this;
    }

    public Fournisseur getFournisseur() {
        return this.fournisseur;
    }

    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }

    public Reception fournisseur(Fournisseur fournisseur) {
        this.setFournisseur(fournisseur);
        return this;
    }

    public Set<FileUpload> getFileUploads() {
        return this.fileUploads;
    }

    public void setFileUploads(Set<FileUpload> fileUploads) {
        if (this.fileUploads != null) {
            this.fileUploads.forEach(i -> i.setReception(null));
        }
        if (fileUploads != null) {
            fileUploads.forEach(i -> i.setReception(this));
        }
        this.fileUploads = fileUploads;
    }

    public Reception fileUploads(Set<FileUpload> fileUploads) {
        this.setFileUploads(fileUploads);
        return this;
    }

    public Reception addFileUpload(FileUpload fileUpload) {
        this.fileUploads.add(fileUpload);
        fileUpload.setReception(this);
        return this;
    }

    public Reception removeFileUpload(FileUpload fileUpload) {
        this.fileUploads.remove(fileUpload);
        fileUpload.setReception(null);
        return this;
    }


    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reception)) {
            return false;
        }
        return id != null && id.equals(((Reception) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Reception{" +
            "id=" + getId() +
            ", numContrat='" + getNumContrat() + "'" +
            ", dateContrat='" + getDateContrat() + "'" +
            ", dateReception='" + getDateReception() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updateAt='" + getUpdateAt() + "'" +
            "}";
    }
}
