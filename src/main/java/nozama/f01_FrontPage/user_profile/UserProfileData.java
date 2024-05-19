package nozama.f01_FrontPage.user_profile;

public class UserProfileData {
    private final int userID;
    private final byte[] profilePicture;
    private final String fullName;
    private final String publicEmail;
    private final String location;
    
    public UserProfileData(int userID, byte[] profilePicture, String fullName, String publicEmail, String location) {
        this.userID = userID;
        this.profilePicture = profilePicture;
        this.fullName = fullName;
        this.publicEmail = publicEmail;
        this.location = location;
    }
    
    public int getUserID() {
        return userID;
    }
    
    public byte[] getProfilePicture() {
        return profilePicture;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public String getPublicEmail() {
        return publicEmail;
    }
    
    public String getLocation() {
        return location;
    }
}
