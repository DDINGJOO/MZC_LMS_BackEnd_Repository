package com.mzc.backend.lms.common.config.cluster;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.ReadFrom;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import java.time.Duration;

/**
 * Redis Cluster 설정
 *
 * 특징:
 * - cluster.enabled=true 일 때만 활성화
 * - Lettuce 기반 클러스터 토폴로지 자동 갱신
 * - Adaptive refresh triggers로 노드 장애 감지
 * - Read From Replica 지원으로 읽기 부하 분산
 *
 * 사용법:
 * - application.yaml에서 spring.data.redis.cluster.enabled=true 설정
 * - 또는 application-cluster.yaml 프로파일 활성화
 */
@Configuration
@ConditionalOnProperty(name = "spring.data.redis.cluster.enabled", havingValue = "true")
public class RedisClusterConfig {

    /**
     * Redis Cluster Connection Factory
     *
     * Lettuce 클라이언트의 클러스터 토폴로지 자동 갱신 기능 활성화:
     * - Periodic Refresh: 30초마다 클러스터 토폴로지 갱신
     * - Adaptive Refresh: 노드 장애, 리다이렉션 발생 시 즉시 갱신
     *
     * @param properties Spring Boot Redis 속성
     * @return Lettuce 기반 Redis Cluster Connection Factory
     */
    @Bean
    @Primary
    public RedisConnectionFactory redisConnectionFactory(RedisProperties properties) {
        RedisClusterConfiguration clusterConfiguration = new RedisClusterConfiguration(
                properties.getCluster().getNodes()
        );
        clusterConfiguration.setMaxRedirects(properties.getCluster().getMaxRedirects());

        // 클러스터 토폴로지 자동 갱신 설정
        ClusterTopologyRefreshOptions topologyRefreshOptions = ClusterTopologyRefreshOptions.builder()
                // 주기적 갱신: 30초마다 클러스터 토폴로지 갱신
                .enablePeriodicRefresh(Duration.ofSeconds(30))
                // 적응형 갱신: 다음 상황에서 즉시 토폴로지 갱신
                .enableAdaptiveRefreshTrigger(
                        ClusterTopologyRefreshOptions.RefreshTrigger.MOVED_REDIRECT,  // MOVED 리다이렉션
                        ClusterTopologyRefreshOptions.RefreshTrigger.ASK_REDIRECT,    // ASK 리다이렉션
                        ClusterTopologyRefreshOptions.RefreshTrigger.PERSISTENT_RECONNECTS  // 지속적 재연결
                )
                .build();

        // 클러스터 클라이언트 옵션 설정
        ClientOptions clientOptions = ClusterClientOptions.builder()
                .topologyRefreshOptions(topologyRefreshOptions)
                .autoReconnect(true)
                .build();

        // Lettuce 클라이언트 설정
        LettuceClientConfiguration clientConfiguration = LettuceClientConfiguration.builder()
                .clientOptions(clientOptions)
                .commandTimeout(Duration.ofSeconds(10))
                .readFrom(ReadFrom.REPLICA_PREFERRED)  // Replica 우선 읽기로 부하 분산
                .build();

        return new LettuceConnectionFactory(clusterConfiguration, clientConfiguration);
    }
}
