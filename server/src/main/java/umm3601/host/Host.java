package umm3601.host;

import org.mongojack.Id;
import org.mongojack.ObjectId;



@SuppressWarnings({"VisibilityModifier"})
public class Host {

  @ObjectId @Id
  @SuppressWarnings({"MemberName"})
  public String _id;

  public String text;

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Host)) {
      return false;
    }
    Host other = (Host) obj;
    return _id.equals(other._id);
  }

  @Override
  public int hashCode() {
    return _id.hashCode();
  }

  @Override
  public String toString() {
    return text;
  }

}
