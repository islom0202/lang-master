package org.example.languagemaster.constraint;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApplicationMessages {
    NO_IMAGE_UPLOADED("no_image_uploaded"),
    GRAMMAR_TOPIC_NOT_FOUND("grammar_topic_not_found"),
    IMAGE_UPLOAD_FAILED("image_upload_failed"),
    USER_NOT_FOUND("user_not_found");
    public final String code;
}
