package ml.caisff.backendstocksgr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ml.caisff.backendstocksgr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AffectationArticleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AffectationArticle.class);
        AffectationArticle affectationArticle1 = new AffectationArticle();
        affectationArticle1.setId(1L);
        AffectationArticle affectationArticle2 = new AffectationArticle();
        affectationArticle2.setId(affectationArticle1.getId());
        assertThat(affectationArticle1).isEqualTo(affectationArticle2);
        affectationArticle2.setId(2L);
        assertThat(affectationArticle1).isNotEqualTo(affectationArticle2);
        affectationArticle1.setId(null);
        assertThat(affectationArticle1).isNotEqualTo(affectationArticle2);
    }

    @Test
    void onCreated() {
    }

    @Test
    void onUpdated() {
    }

    @Test
    void getId() {
    }

    @Test
    void id() {
    }

    @Test
    void setId() {
    }

    @Test
    void getIsAffectation() {
    }

    @Test
    void setValidity() {
    }

    @Test
    void getValidatidy() {
    }

    @Test
    void isAffectation() {
    }

    @Test
    void setIsAffectation() {
    }

    @Test
    void getQuantite() {
    }

    @Test
    void quantite() {
    }

    @Test
    void setReference() {
    }

    @Test
    void getReference() {
    }

    @Test
    void reference() {
    }

    @Test
    void setQuantite() {
    }

    @Test
    void getCreatedAt() {
    }

    @Test
    void getValidAt() {
    }

    @Test
    void setValidAt() {
    }

    @Test
    void createdAt() {
    }

    @Test
    void setCreatedAt() {
    }

    @Test
    void getUpdateAt() {
    }

    @Test
    void updateAt() {
    }

    @Test
    void setUpdateAt() {
    }

    @Test
    void getArticle() {
    }

    @Test
    void setArticle() {
    }

    @Test
    void article() {
    }

    @Test
    void getAffectations() {
    }

    @Test
    void setAffectations() {
    }

    @Test
    void affectations() {
    }

    @Test
    void addAffectation() {
    }

    @Test
    void removeAffectation() {
    }

    @Test
    void testEquals() {
    }

    @Test
    void testHashCode() {
    }

    @Test
    void testToString() {
    }
}
