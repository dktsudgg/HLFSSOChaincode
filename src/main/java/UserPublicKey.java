import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.Objects;

@DataType()
public class UserPublicKey {

    @Property()
    private final String ci;

    @Property()
    private final String publicKey;

    public String getCi(){
        return ci;
    }

    public String getPublicKey(){
        return publicKey;
    }

    public UserPublicKey(@JsonProperty("ci") final String ci, @JsonProperty("publicKey") final String publicKey) {
        this.ci = ci;
        this.publicKey = publicKey;
    }

    @Override
    public boolean equals(final Object obj){
        if(this == obj){
            return true;
        }

        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }

        UserPublicKey other = (UserPublicKey) obj;

        return Objects.deepEquals(new String[] { getCi(), getPublicKey() },
                new String[] { other.getCi(), other.getPublicKey() });
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCi(), getPublicKey());
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + " [ci=" + ci + ", publicKey=" + publicKey + "]";
    }

}
