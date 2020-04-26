package com.cw.chaincode.ssochaincode;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.List;
import java.util.Objects;

@DataType()
public class Ci {

    @Property()
    private final String status;

    @Property()
    private final List<String> publicKeyList;

    public String getStatus(){
        return status;
    }

    public List<String> getPublicKeyList(){
        return publicKeyList;
    }

    public Ci(@JsonProperty("status") final String status, @JsonProperty("publicKeyList") final List<String> publicKeyList) {
        this.status = status;
        this.publicKeyList = publicKeyList;
    }

    @Override
    public boolean equals(final Object obj){
        if(this == obj){
            return true;
        }

        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }

        Ci other = (Ci) obj;

        return Objects.deepEquals(new String[] { getStatus(), getPublicKeyList().toString() },
                new String[] { other.getStatus(), getPublicKeyList().toString() });
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStatus(), getPublicKeyList().toString());
    }

    @Override
    public String toString() {
        String pubKListStr = "";
        for(int i=0; i<publicKeyList.size(); i++){
            if(i != 0)
                pubKListStr += ",";

            pubKListStr += publicKeyList.get(i);
        }

        return this.getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + " [status=" + status + ", publicKeyList=" + pubKListStr + "]";
    }
}
