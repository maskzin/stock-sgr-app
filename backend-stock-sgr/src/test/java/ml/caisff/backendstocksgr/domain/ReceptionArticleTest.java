package ml.caisff.backendstocksgr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ml.caisff.backendstocksgr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReceptionArticleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReceptionArticle.class);
        ReceptionArticle receptionArticle1 = new ReceptionArticle();
        receptionArticle1.setId(1L);
        ReceptionArticle receptionArticle2 = new ReceptionArticle();
        receptionArticle2.setId(receptionArticle1.getId());
        assertThat(receptionArticle1).isEqualTo(receptionArticle2);
        receptionArticle2.setId(2L);
        assertThat(receptionArticle1).isNotEqualTo(receptionArticle2);
        receptionArticle1.setId(null);
        assertThat(receptionArticle1).isNotEqualTo(receptionArticle2);
    }
}
