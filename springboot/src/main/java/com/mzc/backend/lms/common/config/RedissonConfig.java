package com.mzc.backend.lms.common.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Redisson 설정
 * 비즈니스 로직용 분산 락을 위한 설정 클래스
 *
 * ShedLock과의 역할 분담:
 *
 *   ShedLock: 스케줄러 중복 실행 방지 (선언적, @SchedulerLock)
 *   Redisson: 비즈니스 로직 동시성 제어 (프로그래밍 방식 또는 @DistributedLock)
 *
 * 모드 분기:
 *   - Standalone: spring.data.redis.cluster.enabled=false (기본값)
 *   - Cluster: spring.data.redis.cluster.enabled=true
 */
@Configuration
public class RedissonConfig {

    /**
     * Redisson Standalone 설정
     *
     * cluster.enabled=false 일 때 활성화 (기본값)
     */
    @Configuration
    @ConditionalOnProperty(name = "spring.data.redis.cluster.enabled", havingValue = "false", matchIfMissing = true)
    static class RedissonStandaloneConfig {

        @Value("${spring.data.redis.host}")
        private String redisHost;

        @Value("${spring.data.redis.port}")
        private int redisPort;

        /**
         * Standalone RedissonClient Bean
         *
         * @return RedissonClient 인스턴스
         */
        @Bean(destroyMethod = "shutdown")
        public RedissonClient redissonClient() {
            Config config = new Config();
            config.useSingleServer()
                    .setAddress("redis://" + redisHost + ":" + redisPort)
                    .setConnectionMinimumIdleSize(1)
                    .setConnectionPoolSize(2);

            return Redisson.create(config);
        }
    }

    /**
     * Redisson Cluster 설정
     *
     * cluster.enabled=true 일 때 활성화
     */
    @Configuration
    @ConditionalOnProperty(name = "spring.data.redis.cluster.enabled", havingValue = "true")
    static class RedissonClusterConfig {

        @Value("${spring.data.redis.cluster.nodes}")
        private List<String> clusterNodes;

        /**
         * Cluster RedissonClient Bean
         *
         * 클러스터 모드 특징:
         * - 자동 노드 발견 및 재연결
         * - 마스터 노드 장애 시 슬레이브 자동 승격
         * - 슬롯 리디렉션 자동 처리
         *
         * @return RedissonClient 인스턴스
         */
        @Bean(destroyMethod = "shutdown")
        public RedissonClient redissonClient() {
            Config config = new Config();

            // Redis Cluster 노드 주소 변환 (host:port -> redis://host:port)
            String[] nodeAddresses = clusterNodes.stream()
                    .map(node -> "redis://" + node)
                    .toArray(String[]::new);

            config.useClusterServers()
                    .addNodeAddress(nodeAddresses)
                    .setScanInterval(2000)  // 클러스터 상태 스캔 간격 (2초)
                    .setMasterConnectionMinimumIdleSize(1)
                    .setMasterConnectionPoolSize(2)
                    .setSlaveConnectionMinimumIdleSize(1)
                    .setSlaveConnectionPoolSize(2)
                    .setReadMode(org.redisson.config.ReadMode.SLAVE)  // 슬레이브에서 읽기
                    .setSubscriptionMode(org.redisson.config.SubscriptionMode.SLAVE);  // 슬레이브에서 구독

            return Redisson.create(config);
        }
    }
}
