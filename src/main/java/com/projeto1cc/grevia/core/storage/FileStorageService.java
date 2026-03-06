package com.projeto1cc.grevia.core.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;

@Service
public class FileStorageService {

    // Extensões permitidas (imagens + PDF para quem insiste em usar kkk)
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
        ".jpg", ".jpeg", ".png", ".webp", ".gif",
        ".bmp", ".tiff", ".tif", ".heic", ".heif",
        ".avif", ".svg", ".pdf"
    );

    // MIME types permitidos (segunda camada — valida o conteúdo real do arquivo)
    private static final Set<String> ALLOWED_MIME_TYPES = Set.of(
        "image/jpeg", "image/png", "image/webp", "image/gif",
        "image/bmp", "image/tiff", "image/heic", "image/heif",
        "image/avif", "image/svg+xml",
        "application/pdf"
    );

    // Limite de tamanho: 5 MB
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    private final Path fileStorageLocation;

    public FileStorageService(@Value("${file.upload-dir:./uploads}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Não foi possível criar o diretório onde os arquivos enviados serão armazenados.", ex);
        }
    }

    public String storeFile(MultipartFile file) {
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // 1. Bloqueia path traversal
            if (originalFileName.contains("..")) {
                throw new RuntimeException("Desculpe! O nome do arquivo contém uma sequência de caminho inválida: " + originalFileName);
            }

            // 2. Valida tamanho
            if (file.getSize() > MAX_FILE_SIZE) {
                throw new RuntimeException("Arquivo muito grande. O limite é de 5 MB.");
            }

            // 3. Valida extensão
            String fileExtension = "";
            if (originalFileName.contains(".")) {
                fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".")).toLowerCase();
            }
            if (!ALLOWED_EXTENSIONS.contains(fileExtension)) {
                throw new RuntimeException(
                    "Tipo de arquivo não permitido. Envie uma imagem (JPG, PNG, WEBP, GIF, BMP, TIFF, HEIC, AVIF, SVG) ou PDF.");
            }

            // 4. Valida MIME type (conteúdo real do arquivo, não só a extensão)
            String mimeType = file.getContentType();
            if (mimeType == null || !ALLOWED_MIME_TYPES.contains(mimeType.toLowerCase())) {
                throw new RuntimeException(
                    "Conteúdo do arquivo inválido. Apenas imagens e PDFs são aceitos.");
            }

            // 5. Salva com nome aleatório (UUID) para evitar sobreposição
            String newFileName = UUID.randomUUID().toString() + fileExtension;
            Path targetLocation = this.fileStorageLocation.resolve(newFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return newFileName;

        } catch (IOException ex) {
            throw new RuntimeException("Não foi possível armazenar o arquivo " + originalFileName + ". Por favor, tente novamente!", ex);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("Arquivo não encontrado " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("Arquivo não encontrado " + fileName, ex);
        }
    }
}
