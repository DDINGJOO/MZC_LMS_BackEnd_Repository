package com.mzc.lms.media.adapter.out.persistence.entity;

import com.mzc.lms.media.domain.model.MediaStatus;
import com.mzc.lms.media.domain.model.MediaType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "media")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class MediaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "original_file_name", nullable = false, length = 500)
    private String originalFileName;

    @Column(name = "stored_file_name", nullable = false, length = 500)
    private String storedFileName;

    @Column(name = "storage_path", nullable = false, length = 1000)
    private String storagePath;

    @Column(name = "mime_type", length = 100)
    private String mimeType;

    @Enumerated(EnumType.STRING)
    @Column(name = "media_type", nullable = false, length = 20)
    private MediaType mediaType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private MediaStatus status;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "url", nullable = false, length = 1000)
    private String url;

    @ElementCollection
    @CollectionTable(name = "media_thumbnails", joinColumns = @JoinColumn(name = "media_id"))
    @MapKeyColumn(name = "size_key")
    @Column(name = "thumbnail_url", length = 1000)
    @Builder.Default
    private Map<String, String> thumbnailUrls = new HashMap<>();

    @Column(name = "width")
    private Integer width;

    @Column(name = "height")
    private Integer height;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "uploaded_by", nullable = false)
    private Long uploadedBy;

    @Column(name = "uploaded_by_name", length = 100)
    private String uploadedByName;

    @Column(name = "description", length = 1000)
    private String description;

    @ElementCollection
    @CollectionTable(name = "media_metadata", joinColumns = @JoinColumn(name = "media_id"))
    @MapKeyColumn(name = "meta_key")
    @Column(name = "meta_value", length = 500)
    @Builder.Default
    private Map<String, String> metadata = new HashMap<>();

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
