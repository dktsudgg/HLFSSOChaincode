package com.cw.chaincode.ssochaincode;

import com.owlike.genson.Genson;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.*;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;

@Contract(
    name = "com.cw.chaincode.ssochaincode.SSOChaincode",
    info = @Info(
        title = "SSOChain contract",
        description = "The hyperlegendary SSOChain contract",
        version = "0.0.1-SNAPSHOT",
        license = @License(
            name = "Apache 2.0 License",
            url = "http://www.apache.org/licenses/LICENSE-2.0.html"
        ),
        contact = @Contact(
            email = "limkj@codewise.co.kr",
            name = "Lim kyoujin",
            url = "http://codewise.co.kr"
        )
    )
)
@Default
public class SSOChaincode implements ContractInterface {

    private final Genson genson = new Genson();

    private enum SSOChainErrors {
        NOT_FOUND,
        ALREADY_EXISTS
    }

    @Transaction()
    public void initLedger(final Context ctx){

        // TODO:: Thread test..

        ChaincodeStub stub = ctx.getStub();

        String[] testData = {
                "{\"ci\":\"ci_0\",\"publicKey\":\"publicKey_0\",\"num1\":123,\"num2\":456}",
                "{\"ci\":\"ci_1\",\"publicKey\":\"publicKey_1\",\"num1\":123,\"num2\":456}",
                "{\"ci\":\"ci_2\",\"publicKey\":\"publicKey_2\",\"num1\":123,\"num2\":456}",
                "{\"ci\":\"ci_3\",\"publicKey\":\"publicKey_3\",\"num1\":123,\"num2\":456}",
                "{\"ci\":\"ci_4\",\"publicKey\":\"publicKey_4\",\"num1\":123,\"num2\":456}",
                "{\"ci\":\"ci_5\",\"publicKey\":\"publicKey_5\",\"num1\":123,\"num2\":456}",
        };

        for(int i=0; i<testData.length; i++){
            String key = String.format("PUBKEY%d", i);
            UserPublicKey userPublicKey = genson.deserialize(testData[i], UserPublicKey.class);
            String userPublicKeyState = genson.serialize(userPublicKey);
            stub.putStringState(key, userPublicKeyState);
        }

    }

    @Transaction()
    public UserPublicKey createUserPublicKey(final Context ctx, final String key, final String ci, final String publicKey){
        ChaincodeStub stub = ctx.getStub();

        String userPublicKeyState = stub.getStringState(key);
        if(!userPublicKeyState.isEmpty()){
            String errorMessage = String.format("UserPublicKey %s already exists", key);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, SSOChainErrors.ALREADY_EXISTS.toString());
        }

        UserPublicKey userPublicKey = new UserPublicKey(ci, publicKey, 123, 456);
        userPublicKeyState = genson.serialize(userPublicKey);
        stub.putStringState(key, userPublicKeyState);

        return userPublicKey;
    }

    @Transaction()
    public UserPublicKey queryLedger(final Context ctx, final String key, final int num1){
        ChaincodeStub stub = ctx.getStub();
        String userPublicKeyState = stub.getStringState(key);

        if (userPublicKeyState.isEmpty()) {
            String errorMessage = String.format("UserPublicKey %s does not exist", key);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, SSOChainErrors.NOT_FOUND.toString());
        }

        UserPublicKey userPublicKey = genson.deserialize(userPublicKeyState, UserPublicKey.class);
        UserPublicKey newUserPublicKey = new UserPublicKey(userPublicKey.getCi(), userPublicKey.getPublicKey(), num1, userPublicKey.getNum2()+num1);

        String newUserPublicKeyState = genson.serialize(newUserPublicKey);
        stub.putStringState(key, newUserPublicKeyState);

        return newUserPublicKey;
    }

    @Transaction()
    public UserPublicKey querySumLedger(final Context ctx, final String key, final String key2){
        ChaincodeStub stub = ctx.getStub();
        String userPublicKeyState = stub.getStringState(key);
        String userPublicKeyState2 = stub.getStringState(key2);

        if (userPublicKeyState.isEmpty()) {
            String errorMessage = String.format("UserPublicKey %s does not exist", key);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, SSOChainErrors.NOT_FOUND.toString());
        }

        if (userPublicKeyState2.isEmpty()) {
            String errorMessage = String.format("UserPublicKey2 %s does not exist", key2);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, SSOChainErrors.NOT_FOUND.toString());
        }

        UserPublicKey userPublicKey = genson.deserialize(userPublicKeyState, UserPublicKey.class);
        UserPublicKey userPublicKey2 = genson.deserialize(userPublicKeyState2, UserPublicKey.class);
        UserPublicKey newUserPublicKey = new UserPublicKey(userPublicKey.getCi(), userPublicKey.getPublicKey()
                , userPublicKey.getNum1()+userPublicKey2.getNum1(), userPublicKey.getNum2() + userPublicKey2.getNum2());

        String newUserPublicKeyState = genson.serialize(newUserPublicKey);
        stub.putStringState(key, newUserPublicKeyState);

        return newUserPublicKey;
    }

}
