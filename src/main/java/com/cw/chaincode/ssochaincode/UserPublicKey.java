package com.cw.chaincode.ssochaincode;

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

    @Property()
    private int num1;

    @Property()
    private int num2;

    public String getCi(){
        return ci;
    }

    public String getPublicKey(){
        return publicKey;
    }

    public int getNum1(){
        return num1;
    }

    public int getNum2(){
        return num2;
    }

    public UserPublicKey(@JsonProperty("ci") final String ci, @JsonProperty("publicKey") final String publicKey
            , @JsonProperty("num1") final int num1, @JsonProperty("num2") final int num2) {
        this.ci = ci;
        this.publicKey = publicKey;
        this.num1 = num1;
        this.num2 = num2;
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

        return Objects.deepEquals(new String[] { getCi(), getPublicKey(), getNum1()+"", getNum2()+"" },
                new String[] { other.getCi(), other.getPublicKey(), other.getNum1()+"", other.getNum2()+"" });
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCi(), getPublicKey(), getNum1(), getNum2());
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + " [ci=" + ci + ", publicKey=" + publicKey + ", num1=" + num1 + ", num2=" + num2 + "]";
    }

}
