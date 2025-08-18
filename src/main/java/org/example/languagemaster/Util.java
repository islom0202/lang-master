package org.example.languagemaster;

import lombok.experimental.UtilityClass;
import org.example.languagemaster.entity.Users;
import org.springframework.beans.factory.annotation.Value;

import java.util.Random;

@UtilityClass
public class Util {
    @Value("${app.base-url}")
    private String baseUrl;
    public static String generateCode(){
        Random random = new Random();
        int code = 1000 + random.nextInt(9000);
        return String.valueOf(code);
    }
    public static String buildImageUrl(Users user){
        if (user.getImage() == null)
            return "";
        return "/api/user/profile-image/" + user.getId();
    }
}
