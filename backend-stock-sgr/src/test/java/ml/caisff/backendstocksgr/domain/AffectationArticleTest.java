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
}
