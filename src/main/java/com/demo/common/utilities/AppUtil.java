package com.demo.common.utilities;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

/**
 * @author DiGiEx
 */
@Component
public class AppUtil {
    public static final Random RANDOM = new SecureRandom();
    public static final String UTF8_BOM = "\uFEFF";

    private static final Map<String, String> MIME_TYPE_TO_EXTENSION_MAP;

    static {
        MIME_TYPE_TO_EXTENSION_MAP = new HashMap<>();

        try {
            InputStream is = MimeTypeUtils.class.getResourceAsStream("/data/mimetypes.default");
            if (is != null) {
                try {
                    loadStream(is);
                } finally {
                    is.close();
                }
            }
        } catch (IOException e) {
            // ignore
            System.err.println(e.getMessage());
        }
    }
    private static void loadStream(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = reader.readLine()) != null) {
            addMimeTypes(line);
        }
    }
    public static synchronized void addMimeTypes(String mime_types) {
        int hashPos = mime_types.indexOf('#');
        if (hashPos != -1) {
            mime_types = mime_types.substring(0, hashPos);
        }
        StringTokenizer tok = new StringTokenizer(mime_types);
        if (!tok.hasMoreTokens()) {
            return;
        }
        String contentType = tok.nextToken();
        while (tok.hasMoreTokens()) {
            String fileType = tok.nextToken();
            MIME_TYPE_TO_EXTENSION_MAP.put(fileType, contentType);
        }
    }

    /**
     * Encrypt a String to Hash MD5
     * @param value
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String encryptMD5(String value) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(value.getBytes());
        byte byteData[] = md.digest();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
    /**
     * Generate random password
     * @param len
     * @return
     */
    public static String generateRandomPassword(int len) {
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghi"
                +"jklmnopqrstuvwxyz!@#$%&";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        return sb.toString();
    }

    /**
     * Generate Salt for the password
     *
     * @return
     */
    public static String generateSalt() {
        byte[] salt = new byte[Constant.SALT_LENGTH];
        RANDOM.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public static String removeUTF8BOM(String s) {
        if (s.startsWith(UTF8_BOM)) {
            s = s.substring(1);
        }
        return s;
    }


//    public static String getFileExtension(MultipartFile file) {
//        String extensionType = tils.getExtension(file.getOriginalFilename());
//        if (extensionType != null) {
//            extensionType = extensionType.toLowerCase();
//        }
//        return extensionType;
//    }

    public static String replaceCharacter(String text, char replaceChar, int index) {
        // convert the given string to a character array
        char[] chars = text.toCharArray();

        // replace character at the specified position in a char array
        chars[index] = replaceChar;

        // convert the character array back into a string
        text = String.valueOf(chars);
        return String.valueOf(chars);
    }


//    public static void downloadTeamRecieptFromServer(HttpServletResponse response, String RealPath, String userName)  {
//        InputStream inputStream = null;
//        try {
//            java.io.File initialFile = new java.io.File(RealPath);
//            inputStream = new FileInputStream(initialFile);
//            // Do Download
//            if (inputStream != null) {
//                int contentLength = inputStream.available();
//                response.setContentLength(contentLength);
//                response.addHeader("Content-Length", Long.toString(contentLength));
//                //read from the file; write to the ServletOutputStream
//                String ext = tils.getExtension(RealPath);
//
//                response.setContentType(getMineType(ext)); // Fixing bug: Lack extention on IE9
//                response.setHeader("Content-Type", getMineType(ext));
//                response.setHeader("Content-Disposition", "attachment; filename=\"" + userName + "." + ext + "\""); // Fix bug: Lack file name on Firefox
//                ServletOutputStream out = response.getOutputStream();
//                byte[] buffer = new byte[1024];
//                try {
//                    int bytesRead;
//                    while ((bytesRead = inputStream.read(buffer)) >= 0) {
//                        out.write(buffer, 0, bytesRead);
//                    }
//                } catch (Exception e) {
//                    System.err.println(e);
//                } finally {
//                    if (inputStream != null) {
//                        inputStream.close();
//                    }
//                }
//            }
//        } catch (IOException ex) {
//            Logger.getLogger(FileUtil.class.getName()).log(Level.INFO, null, ex);
//        }
//        finally {
//            assert (inputStream != null);
//            inputStream.close();
//        }
//    }


    public static String getMineType(String ext) {
        String mine = "application/octet-stream";
        for (Map.Entry<String, String> en : MIME_TYPE_TO_EXTENSION_MAP.entrySet()) {
            if (ext.equals(en.getValue())) {
                mine = en.getKey();
            }
        }
        return mine;
    }

    public static String getClientIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        if (ip == null || ip.length() == 0) {
            return "unknown";
        } else {
            return ip;
        }
    }
    public static String getFileExtension(MultipartFile file) {
        String extensionType = FilenameUtils.getExtension(file.getOriginalFilename());
        if (extensionType != null) {
            extensionType = extensionType.toLowerCase();
        }
        return extensionType;
    }
}
