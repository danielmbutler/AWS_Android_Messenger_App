package com.amplifyframework.datastore.generated.model;


import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.AuthStrategy;
import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.ModelOperation;
import com.amplifyframework.core.model.annotations.AuthRule;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the User type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Users", authRules = {
  @AuthRule(allow = AuthStrategy.PUBLIC, operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
public final class User implements Model {
  public static final QueryField ID = field("User", "id");
  public static final QueryField USERNAME = field("User", "Username");
  public static final QueryField PROFILE_PHOTO_URL = field("User", "ProfilePhotoUrl");
  public static final QueryField IS_PROFILE_COMPLETE = field("User", "isProfileComplete");
  public static final QueryField EMAIL = field("User", "email");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String") String Username;
  private final @ModelField(targetType="String") String ProfilePhotoUrl;
  private final @ModelField(targetType="Boolean") Boolean isProfileComplete;
  private final @ModelField(targetType="String") String email;
  public String getId() {
      return id;
  }
  
  public String getUsername() {
      return Username;
  }
  
  public String getProfilePhotoUrl() {
      return ProfilePhotoUrl;
  }
  
  public Boolean getIsProfileComplete() {
      return isProfileComplete;
  }
  
  public String getEmail() {
      return email;
  }
  
  private User(String id, String Username, String ProfilePhotoUrl, Boolean isProfileComplete, String email) {
    this.id = id;
    this.Username = Username;
    this.ProfilePhotoUrl = ProfilePhotoUrl;
    this.isProfileComplete = isProfileComplete;
    this.email = email;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      User user = (User) obj;
      return ObjectsCompat.equals(getId(), user.getId()) &&
              ObjectsCompat.equals(getUsername(), user.getUsername()) &&
              ObjectsCompat.equals(getProfilePhotoUrl(), user.getProfilePhotoUrl()) &&
              ObjectsCompat.equals(getIsProfileComplete(), user.getIsProfileComplete()) &&
              ObjectsCompat.equals(getEmail(), user.getEmail());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getUsername())
      .append(getProfilePhotoUrl())
      .append(getIsProfileComplete())
      .append(getEmail())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("User {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("Username=" + String.valueOf(getUsername()) + ", ")
      .append("ProfilePhotoUrl=" + String.valueOf(getProfilePhotoUrl()) + ", ")
      .append("isProfileComplete=" + String.valueOf(getIsProfileComplete()) + ", ")
      .append("email=" + String.valueOf(getEmail()))
      .append("}")
      .toString();
  }
  
  public static BuildStep builder() {
      return new Builder();
  }
  
  /** 
   * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
   * This is a convenience method to return an instance of the object with only its ID populated
   * to be used in the context of a parameter in a delete mutation or referencing a foreign key
   * in a relationship.
   * @param id the id of the existing item this instance will represent
   * @return an instance of this model with only ID populated
   * @throws IllegalArgumentException Checks that ID is in the proper format
   */
  public static User justId(String id) {
    try {
      UUID.fromString(id); // Check that ID is in the UUID format - if not an exception is thrown
    } catch (Exception exception) {
      throw new IllegalArgumentException(
              "Model IDs must be unique in the format of UUID. This method is for creating instances " +
              "of an existing object with only its ID field for sending as a mutation parameter. When " +
              "creating a new object, use the standard builder method and leave the ID field blank."
      );
    }
    return new User(
      id,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      Username,
      ProfilePhotoUrl,
      isProfileComplete,
      email);
  }
  public interface BuildStep {
    User build();
    BuildStep id(String id) throws IllegalArgumentException;
    BuildStep username(String username);
    BuildStep profilePhotoUrl(String profilePhotoUrl);
    BuildStep isProfileComplete(Boolean isProfileComplete);
    BuildStep email(String email);
  }
  

  public static class Builder implements BuildStep {
    private String id;
    private String Username;
    private String ProfilePhotoUrl;
    private Boolean isProfileComplete;
    private String email;
    @Override
     public User build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new User(
          id,
          Username,
          ProfilePhotoUrl,
          isProfileComplete,
          email);
    }
    
    @Override
     public BuildStep username(String username) {
        this.Username = username;
        return this;
    }
    
    @Override
     public BuildStep profilePhotoUrl(String profilePhotoUrl) {
        this.ProfilePhotoUrl = profilePhotoUrl;
        return this;
    }
    
    @Override
     public BuildStep isProfileComplete(Boolean isProfileComplete) {
        this.isProfileComplete = isProfileComplete;
        return this;
    }
    
    @Override
     public BuildStep email(String email) {
        this.email = email;
        return this;
    }
    
    /** 
     * WARNING: Do not set ID when creating a new object. Leave this blank and one will be auto generated for you.
     * This should only be set when referring to an already existing object.
     * @param id id
     * @return Current Builder instance, for fluent method chaining
     * @throws IllegalArgumentException Checks that ID is in the proper format
     */
    public BuildStep id(String id) throws IllegalArgumentException {
        this.id = id;
        
        try {
            UUID.fromString(id); // Check that ID is in the UUID format - if not an exception is thrown
        } catch (Exception exception) {
          throw new IllegalArgumentException("Model IDs must be unique in the format of UUID.",
                    exception);
        }
        
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String id, String username, String profilePhotoUrl, Boolean isProfileComplete, String email) {
      super.id(id);
      super.username(username)
        .profilePhotoUrl(profilePhotoUrl)
        .isProfileComplete(isProfileComplete)
        .email(email);
    }
    
    @Override
     public CopyOfBuilder username(String username) {
      return (CopyOfBuilder) super.username(username);
    }
    
    @Override
     public CopyOfBuilder profilePhotoUrl(String profilePhotoUrl) {
      return (CopyOfBuilder) super.profilePhotoUrl(profilePhotoUrl);
    }
    
    @Override
     public CopyOfBuilder isProfileComplete(Boolean isProfileComplete) {
      return (CopyOfBuilder) super.isProfileComplete(isProfileComplete);
    }
    
    @Override
     public CopyOfBuilder email(String email) {
      return (CopyOfBuilder) super.email(email);
    }
  }
  
}
