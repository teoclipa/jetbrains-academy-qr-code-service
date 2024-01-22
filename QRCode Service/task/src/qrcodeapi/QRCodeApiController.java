package qrcodeapi;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

@RestController
class QRCodeApiController {

    @GetMapping("/api/health")
    public ResponseEntity<String> getHealthStatus() {
        return ResponseEntity.ok("200 OK");
    }

    @GetMapping(path = "/api/qrcode")
    public ResponseEntity<?> generateQRCode(@RequestParam(value = "contents") String contents, @RequestParam(value = "size", defaultValue = "250") int size, @RequestParam(value = "type", defaultValue = "png") String type, @RequestParam(value = "correction", defaultValue = "L") String correction) {

        // Validate contents
        if (StringUtils.isBlank(contents)) {
            return ResponseEntity.badRequest().body("{\"error\": \"Contents cannot be null or blank\"}");
        }

        // Validate size
        if (size < 150 || size > 350) {
            return ResponseEntity.badRequest().body("{\"error\": \"Image size must be between 150 and 350 pixels\"}");
        }

        // Validate correction
        if (!correction.equalsIgnoreCase("L") && !correction.equalsIgnoreCase("M") && !correction.equalsIgnoreCase("Q") && !correction.equalsIgnoreCase("H")) {
            return ResponseEntity.badRequest().body("{\"error\": \"Permitted error correction levels are L, M, Q, H\"}");
        }

        // Validate type
        if (!type.equalsIgnoreCase("png") && !type.equalsIgnoreCase("jpeg") && !type.equalsIgnoreCase("gif")) {
            return ResponseEntity.badRequest().body("{\"error\": \"Only png, jpeg and gif image types are supported\"}");
        }


        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            Map<EncodeHintType, ErrorCorrectionLevel> hints = Map.of(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.valueOf(correction.toUpperCase()));
            BitMatrix bitMatrix = qrCodeWriter.encode(contents, BarcodeFormat.QR_CODE, size, size, hints);

            BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, type, baos);
            MediaType mediaType = MediaType.parseMediaType("image/" + type.toLowerCase());

            return ResponseEntity.ok().contentType(mediaType).body(baos.toByteArray());
        } catch (WriterException | IOException e) {
            return ResponseEntity.status(500).body("{\"error\": \"Error generating QR Code\"}");
        }
    }
}
