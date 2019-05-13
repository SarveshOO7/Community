package sarveshtandon.www.community;

import android.widget.EditText;

public class Community {

    public static final String DESCRIPTION = "description";
    public static final String COMMUNITY_NAME = "communityName";
    public static final String ADMIN_NAME = "adminName";
    public static final String ADMIN_PHONE_NUMBER = "adminPhoneNumber";
    public static final String IS_UNRESTRICTED = "isUnrestricted";
    private String description, communityName , adminName, adminPhoneNumber;
    private Boolean isUnrestricted;

    public Community(String description, String communityName, String adminName, String adminPhoneNumber, Boolean isUnrestricted) {
        this.description = description;
        this.communityName = communityName;
        this.adminName = adminName;
        this.adminPhoneNumber = adminPhoneNumber;
        this.isUnrestricted = isUnrestricted;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getAdminPhoneNumber() {
        return adminPhoneNumber;
    }

    public void setAdminPhoneNumber(String adminPhoneNumber) {
        this.adminPhoneNumber = adminPhoneNumber;
    }

    public Boolean getUnrestricted() {
        return isUnrestricted;
    }

    public void setUnrestricted(Boolean unrestricted) {
        isUnrestricted = unrestricted;
    }


}
