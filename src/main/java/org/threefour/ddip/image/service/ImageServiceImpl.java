/*
package org.threefour.ddip.image.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.threefour.ddip.image.domain.Image;
import org.threefour.ddip.image.domain.TargetType;
import org.threefour.ddip.image.exception.ImageNotFoundException;
import org.threefour.ddip.image.exception.S3UploadFailedException;
import org.threefour.ddip.image.repository.ImageRepository;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.transaction.annotation.Isolation.READ_COMMITTED;
import static org.threefour.ddip.image.exception.ExceptionMessage.IMAGE_NOT_FOUND_EXCEPTION_MESSAGE;
import static org.threefour.ddip.image.exception.ExceptionMessage.S3_UPLOAD_FAILED_EXCEPTION_MESSAGE;

@Service
//@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;
    private final S3Client s3Client;

    private static final String TLS_HTTP_PROTOCOL = "https://";
    private static final String S3_ADDRESS = "s3.amazonaws.com";

    @Value("${cloud.aws.s3.bucket-name}")
    private String s3BucketName;

    @Override
    @Transactional(isolation = READ_COMMITTED, timeout = 10)
    public void createImages(TargetType targetType, Long targetId, List<MultipartFile> imageRequests) {
        List<Image> images = imageRequests.stream()
                .map(file -> createImage(file, targetType, targetId))
                .collect(Collectors.toList());

        imageRepository.saveAll(images);
    }

    private Image createImage(MultipartFile file, TargetType targetType, Long targetId) {
        return Image.of(targetType, targetId, uploadToS3(file, targetType, targetId));
    }

    private String uploadToS3(MultipartFile file, TargetType targetType, Long productId) {
        String originalFilename = file.getOriginalFilename();
        long ms = System.currentTimeMillis();
        String key = String.format("%s/%d/%s_%s", targetType.toString().toLowerCase(), productId, ms, originalFilename);

        try {
            File tempFile = convertMultipartFileToFile(file);
            s3Client.putObject(PutObjectRequest.builder()
                    .bucket(s3BucketName)
                    .key(key)
                    .build(), Paths.get(tempFile.getPath()));
            tempFile.delete();
        } catch (IOException ie) {
            throw new S3UploadFailedException(S3_UPLOAD_FAILED_EXCEPTION_MESSAGE, ie);
        }

        return String.format("%s%s.%s/%s", TLS_HTTP_PROTOCOL, s3BucketName, S3_ADDRESS, key);
    }

    private File convertMultipartFileToFile(MultipartFile file) throws IOException {
        File convertedFile
                = new File(String.format("%s/%s", System.getProperty("java.io.tmpdir"), file.getOriginalFilename()));
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        }
        return convertedFile;
    }

    @Override
    public List<Image> getImages(TargetType targetType, Long targetId) {
        return imageRepository.findByTargetTypeAndTargetIdAndDeleteYnFalse(targetType, targetId);
    }

    private Image getImage(Long id) {
        return imageRepository.findByIdAndDeleteYnFalse(id)
                .orElseThrow(() -> new ImageNotFoundException(String.format(IMAGE_NOT_FOUND_EXCEPTION_MESSAGE, id)));
    }

    @Override
    public void deleteImage(Long id) {
        Image image = getImage(id);
        image.delete();
        imageRepository.save(image);
    }
}
*/
