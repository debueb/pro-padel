package de.appsolve.padelcampus;

import de.appsolve.padelcampus.tasks.ContextAwarePoolExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class ExecutorConfig extends AsyncConfigurerSupport {

    /*
    used in @Async RankingUtil.updateRanking()
     */
    @Override
    @Bean
    public Executor getAsyncExecutor() {
        ContextAwarePoolExecutor executor = new ContextAwarePoolExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(1);
        executor.setQueueCapacity(100);
        executor.initialize();
        return executor;
    }
}
