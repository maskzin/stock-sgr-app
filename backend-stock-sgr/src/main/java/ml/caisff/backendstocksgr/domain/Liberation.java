package ml.caisff.backendstocksgr.domain;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "liberation")
public class Liberation implements Serializable{

    private static final long serialVersionUID= 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "date")
    private LocalDate date;

    @Column(name="etat")
    private String etat;

    private LocalDate createdAt;

    private LocalDate updateAt;

    @ManyToOne
    @JsonIgnoreProperties(value = {"liberations", "affectation", "article"}, allowSetters = true)
    private AffectationArticle affectationArticle;

    @PrePersist
    public void oncreated()
    {
        this.createdAt=LocalDate.now();
        this.updateAt=LocalDate.now();
    }

    @PreUpdate
    public void onUpdated()
    {
        this.updateAt=LocalDate.now();
    }


    public Long getId(){return this.id;}
    public void setId(Long id){this.id=id;}

    public String getEtat(){return this.etat;}
    public void setEtat(String etat){this.etat=etat;}

    public LocalDate getDate(){return this.date;}
    public void setDate(LocalDate date){this.date=date;}

    public AffectationArticle getAffectationArticle(){return this.affectationArticle;}
    public void setAffectationArticle(AffectationArticle affectationArticle){this.affectationArticle=affectationArticle;}

    public Liberation affectationArticle(AffectationArticle affectationArticle){
        this.setAffectationArticle(affectationArticle);
        return this;
    }


    
}
