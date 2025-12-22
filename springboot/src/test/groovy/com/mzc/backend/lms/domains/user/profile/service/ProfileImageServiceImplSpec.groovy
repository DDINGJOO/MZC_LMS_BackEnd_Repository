package com.mzc.backend.lms.domains.user.profile.service

import com.mzc.backend.lms.domains.user.profile.entity.UserProfileImage
import com.mzc.backend.lms.domains.user.profile.repository.UserProfileImageRepository
import com.mzc.backend.lms.domains.user.user.entity.User
import com.mzc.backend.lms.domains.user.user.exceptions.UserException
import com.mzc.backend.lms.domains.user.user.repository.UserRepository
import com.mzc.backend.lms.util.image.ImageProcessor
import com.mzc.backend.lms.util.image.LocalImageStorageStrategy
import org.springframework.web.multipart.MultipartFile
import spock.lang.Specification
import spock.lang.Subject

/**
 * ProfileImageServiceImpl 테스트
 * 프로필 이미지 업로드, 삭제 기능 테스트
 */
class ProfileImageServiceImplSpec extends Specification {

    def userRepository = Mock(UserRepository)
    def userProfileImageRepository = Mock(UserProfileImageRepository)
    def imageProcessor = Mock(ImageProcessor)
    def imageStorageStrategy = Mock(LocalImageStorageStrategy)

    @Subject
    ProfileImageServiceImpl profileImageService

    def setup() {
        profileImageService = new ProfileImageServiceImpl(
                userRepository,
                userProfileImageRepository,
                imageProcessor,
                imageStorageStrategy
        )
        // self-injection 설정
        profileImageService.setSelf(profileImageService)
    }

    def "존재하지 않는 사용자의 이미지 업로드 시 예외가 발생한다"() {
        given: "존재하지 않는 사용자 ID와 파일"
        def userId = 999L
        def file = Mock(MultipartFile)
        userRepository.findById(userId) >> Optional.empty()

        when: "이미지 업로드를 요청하면"
        profileImageService.uploadProfileImage(userId, file)

        then: "UserException이 발생한다"
        thrown(UserException)
    }

    def "유효하지 않은 파일 업로드 시 검증 단계에서 예외가 발생한다"() {
        given: "유효하지 않은 파일"
        def userId = 1L
        def user = Mock(User)
        def file = Mock(MultipartFile)

        userRepository.findById(userId) >> Optional.of(user)
        imageProcessor.validate(file) >> { throw new IllegalArgumentException("지원하지 않는 파일 형식입니다") }

        when: "이미지 업로드를 요청하면"
        profileImageService.uploadProfileImage(userId, file)

        then: "예외가 발생한다"
        thrown(IllegalArgumentException)
    }

    def "파일 읽기 실패 시 RuntimeException이 발생한다"() {
        given: "읽기 실패하는 파일"
        def userId = 1L
        def user = Mock(User)
        def file = Mock(MultipartFile) {
            getBytes() >> { throw new IOException("읽기 실패") }
        }

        userRepository.findById(userId) >> Optional.of(user)
        userProfileImageRepository.findByUserId(userId) >> Optional.empty()

        when: "이미지 업로드를 요청하면"
        profileImageService.uploadProfileImage(userId, file)

        then: "RuntimeException이 발생한다"
        def exception = thrown(RuntimeException)
        exception.message == "파일 읽기 실패"
    }

    def "비동기 이미지 처리 및 저장이 정상적으로 동작한다"() {
        given: "이미지 데이터와 사용자 ID"
        def userId = 1L
        def fileBytes = "image data".getBytes()
        def webpData = "webp data".getBytes()
        def thumbnailData = "thumbnail data".getBytes()

        imageProcessor.generateFileName() >> "test-file-name"
        imageProcessor.convertToWebpFromBytes(fileBytes) >> webpData
        imageProcessor.createThumbnailFromBytes(fileBytes) >> thumbnailData
        imageStorageStrategy.store(webpData, "test-file-name", "image/webp") >> "http://example.com/image.webp"
        imageStorageStrategy.storeThumbnail(thumbnailData, "test-file-name") >> "http://example.com/thumb.webp"
        userProfileImageRepository.findByUserId(userId) >> Optional.empty()

        when: "비동기 이미지 처리를 수행하면"
        def result = profileImageService.processAndSaveImageAsync(userId, fileBytes)
        result.join()

        then: "이미지가 저장된다"
        1 * userProfileImageRepository.save(_)
    }

    def "기존 프로필 이미지가 있으면 업데이트한다"() {
        given: "기존 프로필 이미지가 있는 사용자"
        def userId = 1L
        def imageUrl = "http://example.com/new-image.webp"
        def thumbnailUrl = "http://example.com/new-thumb.webp"
        def existingImage = Mock(UserProfileImage)

        userProfileImageRepository.findByUserId(userId) >> Optional.of(existingImage)

        when: "프로필 이미지를 저장하면"
        profileImageService.saveProfileImage(userId, imageUrl, thumbnailUrl)

        then: "기존 이미지가 업데이트된다"
        1 * existingImage.updateImage(imageUrl, thumbnailUrl)
        0 * userProfileImageRepository.save(_)
    }

    def "프로필 이미지가 없으면 새로 생성한다"() {
        given: "프로필 이미지가 없는 사용자"
        def userId = 1L
        def imageUrl = "http://example.com/image.webp"
        def thumbnailUrl = "http://example.com/thumb.webp"

        userProfileImageRepository.findByUserId(userId) >> Optional.empty()

        when: "프로필 이미지를 저장하면"
        profileImageService.saveProfileImage(userId, imageUrl, thumbnailUrl)

        then: "새 이미지가 저장된다"
        1 * userProfileImageRepository.save(_)
    }

    def "존재하지 않는 사용자의 이미지 삭제 시 예외가 발생한다"() {
        given: "존재하지 않는 사용자 ID"
        def userId = 999L
        userRepository.findById(userId) >> Optional.empty()

        when: "이미지 삭제를 요청하면"
        profileImageService.deleteProfileImage(userId)

        then: "UserException이 발생한다"
        thrown(UserException)
    }

    def "프로필 이미지를 정상적으로 삭제한다"() {
        given: "프로필 이미지가 있는 사용자"
        def userId = 1L
        def user = Mock(User)
        def existingImage = Mock(UserProfileImage) {
            getImageUrl() >> "http://example.com/image.webp"
            getThumbnailUrl() >> "http://example.com/thumb.webp"
        }

        userRepository.findById(userId) >> Optional.of(user)
        userProfileImageRepository.findByUserId(userId) >> Optional.of(existingImage)

        when: "이미지 삭제를 요청하면"
        profileImageService.deleteProfileImage(userId)

        then: "파일이 삭제된다"
        1 * imageStorageStrategy.delete("http://example.com/image.webp")
        1 * imageStorageStrategy.delete("http://example.com/thumb.webp")

        and: "DB에서 삭제된다"
        1 * userProfileImageRepository.deleteByUserIdQuery(userId)
    }

    def "프로필 이미지가 없는 사용자의 이미지 삭제도 정상 처리된다"() {
        given: "프로필 이미지가 없는 사용자"
        def userId = 1L
        def user = Mock(User)

        userRepository.findById(userId) >> Optional.of(user)
        userProfileImageRepository.findByUserId(userId) >> Optional.empty()

        when: "이미지 삭제를 요청하면"
        profileImageService.deleteProfileImage(userId)

        then: "파일 삭제는 호출되지 않는다"
        0 * imageStorageStrategy.delete(_)

        and: "DB 삭제는 호출된다"
        1 * userProfileImageRepository.deleteByUserIdQuery(userId)
    }

    def "비동기 처리 중 예외 발생 시 CompletableFuture가 실패한다"() {
        given: "이미지 처리 중 예외가 발생하는 경우"
        def userId = 1L
        def fileBytes = "image data".getBytes()

        imageProcessor.generateFileName() >> { throw new RuntimeException("처리 실패") }

        when: "비동기 이미지 처리를 수행하면"
        def result = profileImageService.processAndSaveImageAsync(userId, fileBytes)

        then: "CompletableFuture가 예외로 완료된다"
        result.isCompletedExceptionally()
    }
}
