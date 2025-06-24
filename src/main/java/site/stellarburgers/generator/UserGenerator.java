package site.stellarburgers.generator;

import site.stellarburgers.pojo.LoginUser;
import site.stellarburgers.pojo.RegisterUser;

public class UserGenerator {

    final static String DEFAULT_EMAIL = "test" + System.currentTimeMillis() + "@test.ru";
    final static String DEFAULT_PASSWORD = "123456";
    final static String DEFAULT_NAME = "User";
    final static String FAKE_EMAIL = "abcd.mail";
    final static String NEW_EMAIL = "new" + System.currentTimeMillis() + "@test.ru";
    final static String NEW_PASSWORD = "12345";
    final static String NEW_NAME = "NewUser";

    public static RegisterUser getDefaultRegistrationData() {
        return new RegisterUser(DEFAULT_EMAIL, DEFAULT_PASSWORD, DEFAULT_NAME);
    }

    public static RegisterUser getRegistrationDataWithOneEmptyField(UserField emptyField) {
        RegisterUser data = null;
        switch (emptyField) {
            case EMAIL:
                data = new RegisterUser("", DEFAULT_PASSWORD, DEFAULT_NAME);
                break;
            case PASSWORD:
                data = new RegisterUser(DEFAULT_EMAIL, "", DEFAULT_NAME);
                break;
            case NAME:
                data = new RegisterUser(DEFAULT_EMAIL, DEFAULT_PASSWORD, "");
                break;
        }
        return data;
    }

    public static LoginUser getDefaultLoginData() {
        return new LoginUser(DEFAULT_EMAIL, DEFAULT_PASSWORD);
    }

    public static LoginUser getNewLoginData() {
        return new LoginUser(NEW_EMAIL, DEFAULT_PASSWORD);
    }

    public static LoginUser getFakeLoginData() {
        return new LoginUser(FAKE_EMAIL, DEFAULT_PASSWORD);
    }

    public static RegisterUser getDefaultUpdateData() {
        return new RegisterUser(NEW_EMAIL, NEW_PASSWORD, NEW_NAME);
    }

    public static LoginUser getLoginDataWithWrongEmail() {
        return new LoginUser("wrong@example.com", DEFAULT_PASSWORD);
    }

    public static LoginUser getLoginDataWithWrongPassword() {
        return new LoginUser(DEFAULT_EMAIL, "wrongpassword");
    }

    public enum UserField {
        EMAIL,
        PASSWORD,
        NAME
    }
}
