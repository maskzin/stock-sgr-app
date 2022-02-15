package ml.caisff.backendstocksgr.service.impl;

import java.util.List;
import java.util.Optional;
import ml.caisff.backendstocksgr.domain.Article;
import ml.caisff.backendstocksgr.repository.ArticleRepository;
import ml.caisff.backendstocksgr.service.ArticleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Article}.
 */
@Service
@Transactional
public class ArticleServiceImpl implements ArticleService {

    private final Logger log = LoggerFactory.getLogger(ArticleServiceImpl.class);

    private final ArticleRepository articleRepository;

    public ArticleServiceImpl(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public Article save(Article article) {
        log.debug("Request to save Article : {}", article);
        return articleRepository.save(article);
    }

    @Override
    public Optional<Article> partialUpdate(Article article) {
        log.debug("Request to partially update Article : {}", article);

        return articleRepository
            .findById(article.getId())
            .map(existingArticle -> {
                if (article.getLibelleArticle() != null) {
                    existingArticle.setLibelleArticle(article.getLibelleArticle());
                }
                if (article.getCaracteristique() != null) {
                    existingArticle.setCaracteristique(article.getCaracteristique());
                }
                if (article.getNiveauAlerte() != null) {
                    existingArticle.setNiveauAlerte(article.getNiveauAlerte());
                }
                if (article.getCommentaire() != null) {
                    existingArticle.setCommentaire(article.getCommentaire());
                }
                if (article.getType() != null) {
                    existingArticle.setType(article.getType());
                }
                if (article.getStock() != null) {
                    existingArticle.setStock(article.getStock());
                }
                if (article.getCreatedAt() != null) {
                    existingArticle.setCreatedAt(article.getCreatedAt());
                }
                if (article.getUpdateAt() != null) {
                    existingArticle.setUpdateAt(article.getUpdateAt());
                }

                return existingArticle;
            })
            .map(articleRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Article> findAll() {
        log.debug("Request to get all Articles");
        List<Article> articles= articleRepository.findAll(Sort.by(Sort.Direction.DESC,"id"));
        return this.findAllArticleWithQuantite(articles);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Article> findOne(Long id) {
        log.debug("Request to get Article : {}", id);
        return articleRepository.findById(id);
    }

    @Override
    @Transactional
    public List<Article> findArticleByCategorieId(Long id){
        log.debug("liste des article par categorie  : {}", id);

        return this.findAllArticleWithQuantite(articleRepository.findByCategorieId(id));
    }
    public List<Article> findArticleByMarqueId(Long id){
        log.debug("liste des article par marque  : {}", id);

        return this.findAllArticleWithQuantite(articleRepository.findByMarqueId(id));
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Article : {}", id);
        articleRepository.deleteById(id);
    }

    public List<Article> findAllArticleWithQuantite( List<Article> articles)
    {

        // articles.stream().map(art-> art.setStock(
        //     if()
        // ));

        for (Article article : articles) {
            article.setStock(articleRepository.findQuantiteRestant(article.getId()));
        }
        return articles;
    }
}
