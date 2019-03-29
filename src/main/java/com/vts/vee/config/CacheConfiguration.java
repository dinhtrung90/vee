package com.vts.vee.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import io.github.jhipster.config.jcache.BeanClassLoaderAwareJCacheRegionFactory;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        BeanClassLoaderAwareJCacheRegionFactory.setBeanClassLoader(this.getClass().getClassLoader());
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(com.vts.vee.repository.UserRepository.USERS_BY_LOGIN_CACHE, jcacheConfiguration);
            cm.createCache(com.vts.vee.repository.UserRepository.USERS_BY_EMAIL_CACHE, jcacheConfiguration);
            cm.createCache(com.vts.vee.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(com.vts.vee.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(com.vts.vee.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(com.vts.vee.domain.Survey.class.getName(), jcacheConfiguration);
            cm.createCache(com.vts.vee.domain.Survey.class.getName() + ".questionOrders", jcacheConfiguration);
            cm.createCache(com.vts.vee.domain.Survey.class.getName() + ".surveyResponses", jcacheConfiguration);
            cm.createCache(com.vts.vee.domain.QuestionOrder.class.getName(), jcacheConfiguration);
            cm.createCache(com.vts.vee.domain.Question.class.getName(), jcacheConfiguration);
            cm.createCache(com.vts.vee.domain.Question.class.getName() + ".questionTypes", jcacheConfiguration);
            cm.createCache(com.vts.vee.domain.Question.class.getName() + ".responseChoices", jcacheConfiguration);
            cm.createCache(com.vts.vee.domain.Question.class.getName() + ".res", jcacheConfiguration);
            cm.createCache(com.vts.vee.domain.QuestionType.class.getName(), jcacheConfiguration);
            cm.createCache(com.vts.vee.domain.ResponseChoice.class.getName(), jcacheConfiguration);
            cm.createCache(com.vts.vee.domain.VeeResponse.class.getName(), jcacheConfiguration);
            cm.createCache(com.vts.vee.domain.SurveyResponse.class.getName(), jcacheConfiguration);
            cm.createCache(com.vts.vee.domain.Respondent.class.getName(), jcacheConfiguration);
            cm.createCache(com.vts.vee.domain.Respondent.class.getName() + ".res", jcacheConfiguration);
            cm.createCache(com.vts.vee.domain.Respondent.class.getName() + ".surveyResponses", jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
