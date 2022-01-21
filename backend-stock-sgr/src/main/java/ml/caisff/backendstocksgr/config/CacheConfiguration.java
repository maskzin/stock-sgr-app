package ml.caisff.backendstocksgr.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, ml.caisff.backendstocksgr.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, ml.caisff.backendstocksgr.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, ml.caisff.backendstocksgr.domain.User.class.getName());
            createCache(cm, ml.caisff.backendstocksgr.domain.Authority.class.getName());
            createCache(cm, ml.caisff.backendstocksgr.domain.User.class.getName() + ".authorities");
            createCache(cm, ml.caisff.backendstocksgr.domain.Article.class.getName());
            createCache(cm, ml.caisff.backendstocksgr.domain.Article.class.getName() + ".detailReceptions");
            createCache(cm, ml.caisff.backendstocksgr.domain.Article.class.getName() + ".affectations");
            createCache(cm, ml.caisff.backendstocksgr.domain.Fournisseur.class.getName());
            createCache(cm, ml.caisff.backendstocksgr.domain.Fournisseur.class.getName() + ".receptions");
            createCache(cm, ml.caisff.backendstocksgr.domain.Employee.class.getName());
            createCache(cm, ml.caisff.backendstocksgr.domain.Employee.class.getName() + ".affectations");
            createCache(cm, ml.caisff.backendstocksgr.domain.Employee.class.getName() + ".receptions");
            createCache(cm, ml.caisff.backendstocksgr.domain.Categorie.class.getName());
            createCache(cm, ml.caisff.backendstocksgr.domain.Categorie.class.getName() + ".articles");
            createCache(cm, ml.caisff.backendstocksgr.domain.Division.class.getName());
            createCache(cm, ml.caisff.backendstocksgr.domain.Division.class.getName() + ".employees");
            createCache(cm, ml.caisff.backendstocksgr.domain.Reception.class.getName());
            createCache(cm, ml.caisff.backendstocksgr.domain.Reception.class.getName() + ".detailReceptions");
            createCache(cm, ml.caisff.backendstocksgr.domain.Affectation.class.getName());
            createCache(cm, ml.caisff.backendstocksgr.domain.DetailReception.class.getName());
            createCache(cm, ml.caisff.backendstocksgr.domain.Marque.class.getName());
            createCache(cm, ml.caisff.backendstocksgr.domain.Article.class.getName() + ".receptions");
            createCache(cm, ml.caisff.backendstocksgr.domain.Reception.class.getName() + ".articles");
            createCache(cm, ml.caisff.backendstocksgr.domain.Article.class.getName() + ".receptionArticles");
            createCache(cm, ml.caisff.backendstocksgr.domain.Reception.class.getName() + ".receptionArticles");
            createCache(cm, ml.caisff.backendstocksgr.domain.Reception.class.getName() + ".employees");
            createCache(cm, ml.caisff.backendstocksgr.domain.ReceptionArticle.class.getName());
            createCache(cm, ml.caisff.backendstocksgr.domain.AffectationArticle.class.getName());
            createCache(cm, ml.caisff.backendstocksgr.domain.Article.class.getName() + ".affectationArticles");
            createCache(cm, ml.caisff.backendstocksgr.domain.Affectation.class.getName() + ".affectationArticles");
            createCache(cm, ml.caisff.backendstocksgr.domain.ReceptionArticle.class.getName() + ".receptions");
            createCache(cm, ml.caisff.backendstocksgr.domain.AffectationArticle.class.getName() + ".affectations");
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
