package com.projeto1cc.grevia.core.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    /**
     * Faz o upload de uma imagem para o Cloudinary
     *
     * @param file a imagem (MultipartFile)
     * @return a URL pública e segura (https) da imagem no Cloudinary
     * @throws IOException em caso de erro no upload
     */
    public String uploadImage(MultipartFile file) throws IOException {
        String publicId = UUID.randomUUID().toString();

        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                "public_id", publicId,
                "folder", "grevia_images"
        ));

        return uploadResult.get("secure_url").toString();
    }

    /**
     * Remove uma imagem do Cloudinary pelo seu ID público
     *
     * @param publicId ID da imagem retornado no upload
     * @throws IOException em caso de erro na deleção
     */
    public void deleteImage(String publicId) throws IOException {
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }
}
