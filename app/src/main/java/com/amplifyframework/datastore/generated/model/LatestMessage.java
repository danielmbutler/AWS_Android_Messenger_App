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

/** This is an auto generated class representing the LatestMessage type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "LatestMessages", authRules = {
  @AuthRule(allow = AuthStrategy.PUBLIC, operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
public final class LatestMessage implements Model {
  public static final QueryField ID = field("LatestMessage", "id");
  public static final QueryField MESSAGE_TXT = field("LatestMessage", "messageTxt");
  public static final QueryField FROMID = field("LatestMessage", "fromid");
  public static final QueryField TOID = field("LatestMessage", "toid");
  public static final QueryField TIMESTAMP = field("LatestMessage", "timestamp");
  public static final QueryField READ_RECEIPT = field("LatestMessage", "readReceipt");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String") String messageTxt;
  private final @ModelField(targetType="String") String fromid;
  private final @ModelField(targetType="String") String toid;
  private final @ModelField(targetType="String") String timestamp;
  private final @ModelField(targetType="String") String readReceipt;
  public String getId() {
      return id;
  }
  
  public String getMessageTxt() {
      return messageTxt;
  }
  
  public String getFromid() {
      return fromid;
  }
  
  public String getToid() {
      return toid;
  }
  
  public String getTimestamp() {
      return timestamp;
  }
  
  public String getReadReceipt() {
      return readReceipt;
  }
  
  private LatestMessage(String id, String messageTxt, String fromid, String toid, String timestamp, String readReceipt) {
    this.id = id;
    this.messageTxt = messageTxt;
    this.fromid = fromid;
    this.toid = toid;
    this.timestamp = timestamp;
    this.readReceipt = readReceipt;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      LatestMessage latestMessage = (LatestMessage) obj;
      return ObjectsCompat.equals(getId(), latestMessage.getId()) &&
              ObjectsCompat.equals(getMessageTxt(), latestMessage.getMessageTxt()) &&
              ObjectsCompat.equals(getFromid(), latestMessage.getFromid()) &&
              ObjectsCompat.equals(getToid(), latestMessage.getToid()) &&
              ObjectsCompat.equals(getTimestamp(), latestMessage.getTimestamp()) &&
              ObjectsCompat.equals(getReadReceipt(), latestMessage.getReadReceipt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getMessageTxt())
      .append(getFromid())
      .append(getToid())
      .append(getTimestamp())
      .append(getReadReceipt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("LatestMessage {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("messageTxt=" + String.valueOf(getMessageTxt()) + ", ")
      .append("fromid=" + String.valueOf(getFromid()) + ", ")
      .append("toid=" + String.valueOf(getToid()) + ", ")
      .append("timestamp=" + String.valueOf(getTimestamp()) + ", ")
      .append("readReceipt=" + String.valueOf(getReadReceipt()))
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
  public static LatestMessage justId(String id) {
    try {
      UUID.fromString(id); // Check that ID is in the UUID format - if not an exception is thrown
    } catch (Exception exception) {
      throw new IllegalArgumentException(
              "Model IDs must be unique in the format of UUID. This method is for creating instances " +
              "of an existing object with only its ID field for sending as a mutation parameter. When " +
              "creating a new object, use the standard builder method and leave the ID field blank."
      );
    }
    return new LatestMessage(
      id,
      null,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      messageTxt,
      fromid,
      toid,
      timestamp,
      readReceipt);
  }
  public interface BuildStep {
    LatestMessage build();
    BuildStep id(String id) throws IllegalArgumentException;
    BuildStep messageTxt(String messageTxt);
    BuildStep fromid(String fromid);
    BuildStep toid(String toid);
    BuildStep timestamp(String timestamp);
    BuildStep readReceipt(String readReceipt);
  }
  

  public static class Builder implements BuildStep {
    private String id;
    private String messageTxt;
    private String fromid;
    private String toid;
    private String timestamp;
    private String readReceipt;
    @Override
     public LatestMessage build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new LatestMessage(
          id,
          messageTxt,
          fromid,
          toid,
          timestamp,
          readReceipt);
    }
    
    @Override
     public BuildStep messageTxt(String messageTxt) {
        this.messageTxt = messageTxt;
        return this;
    }
    
    @Override
     public BuildStep fromid(String fromid) {
        this.fromid = fromid;
        return this;
    }
    
    @Override
     public BuildStep toid(String toid) {
        this.toid = toid;
        return this;
    }
    
    @Override
     public BuildStep timestamp(String timestamp) {
        this.timestamp = timestamp;
        return this;
    }
    
    @Override
     public BuildStep readReceipt(String readReceipt) {
        this.readReceipt = readReceipt;
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
    private CopyOfBuilder(String id, String messageTxt, String fromid, String toid, String timestamp, String readReceipt) {
      super.id(id);
      super.messageTxt(messageTxt)
        .fromid(fromid)
        .toid(toid)
        .timestamp(timestamp)
        .readReceipt(readReceipt);
    }
    
    @Override
     public CopyOfBuilder messageTxt(String messageTxt) {
      return (CopyOfBuilder) super.messageTxt(messageTxt);
    }
    
    @Override
     public CopyOfBuilder fromid(String fromid) {
      return (CopyOfBuilder) super.fromid(fromid);
    }
    
    @Override
     public CopyOfBuilder toid(String toid) {
      return (CopyOfBuilder) super.toid(toid);
    }
    
    @Override
     public CopyOfBuilder timestamp(String timestamp) {
      return (CopyOfBuilder) super.timestamp(timestamp);
    }
    
    @Override
     public CopyOfBuilder readReceipt(String readReceipt) {
      return (CopyOfBuilder) super.readReceipt(readReceipt);
    }
  }
  
}
