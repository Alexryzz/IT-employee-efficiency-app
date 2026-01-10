package org.coursework.app.service.util;

import lombok.extern.slf4j.Slf4j;
import org.coursework.app.dto.UpdateGradeRequest;
import org.coursework.app.entity.Account;
import org.coursework.app.enums.Role;

@Slf4j
public class ValidatorUtil {

    public static void validatePasswordMatch(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            log.warn("Password and confirm-Password do not match");
            throw new IllegalArgumentException("Пароли не совпадают!");
        }
    }

    public static void validateNotAdminGrade(Account account) {
        if(account.getRole().equals(Role.ROLE_ADMIN)) {
            log.warn("unable to set worker grade");
            throw new IllegalArgumentException("Нельзя изменить уровень админа :)");
        }
    }

    public static boolean validateDifferentGrades(Account account, UpdateGradeRequest workerGrade) {
        log.info("Validate different grades");
        return !account.getGrade().equals(workerGrade.getWorkerGrade());
    }

    public static void validateSetGradeRequest(Account account){
        log.info("Validate set grade request");
        if (account.getGrade() != null) {
            log.warn("grade already set");
            throw new IllegalArgumentException("Invalid grade request");
        }
    }

    public static void validateUpdateGradeRequest(Account account) {
        log.info("Validate update grade request");
        if (account.getGrade() == null) {
            log.warn("grade not set");
            throw new IllegalArgumentException("Invalid grade request");
        }
    }
}
