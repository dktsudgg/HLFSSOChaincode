package com.cw.chaincode.ssochaincode;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.mockito.InOrder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.mockito.Mockito.*;

public class SSOChaincodeTest {

    private final class MockKeyValue implements KeyValue {

        private final String key;
        private final String value;

        MockKeyValue(final String key, final String value){
            super();
            this.key = key;
            this.value = value;
        }

        @Override
        public String getKey() {
            return this.key;
        }

        @Override
        public byte[] getValue() {
            return this.value.getBytes();
        }

        @Override
        public String getStringValue() {
            return this.value;
        }

    }

    private final class MockUserPublicKeysIterator implements QueryResultsIterator<KeyValue>{

        private final List<KeyValue> userPublicKeyList;

        MockUserPublicKeysIterator(){
            super();

            userPublicKeyList = new ArrayList<KeyValue>();

            userPublicKeyList.add(new MockKeyValue("PUBKEY0", "{ \"ci\": \"ci_0\", \"publicKey\": \"publicKey_0\",\"num1\":123,\"num2\":456}"));
            userPublicKeyList.add(new MockKeyValue("PUBKEY1", "{ \"ci\": \"ci_1\", \"publicKey\": \"publicKey_1\",\"num1\":123,\"num2\":456}"));
            userPublicKeyList.add(new MockKeyValue("PUBKEY2", "{ \"ci\": \"ci_2\", \"publicKey\": \"publicKey_2\",\"num1\":123,\"num2\":456}"));
            userPublicKeyList.add(new MockKeyValue("PUBKEY3", "{ \"ci\": \"ci_3\", \"publicKey\": \"publicKey_3\",\"num1\":123,\"num2\":456}"));
            userPublicKeyList.add(new MockKeyValue("PUBKEY4", "{ \"ci\": \"ci_4\", \"publicKey\": \"publicKey_4\",\"num1\":123,\"num2\":456}"));
            userPublicKeyList.add(new MockKeyValue("PUBKEY5", "{ \"ci\": \"ci_5\", \"publicKey\": \"publicKey_5\",\"num1\":123,\"num2\":456}"));

        }

        @Override
        public void close() throws Exception {
            // do nothing..
        }

        @Override
        public Iterator<KeyValue> iterator() {
            return userPublicKeyList.iterator();
        }
    }

    @Test
    @DisplayName("정의되지않은 트랜잭션 발생 시")
    public void invokeUnknownTransaction() {
        SSOChaincode contract = new SSOChaincode();
        Context ctx = mock(Context.class);

        Throwable thrown = catchThrowable(() -> {
            contract.unknownTransaction(ctx);
        });

        assertThat(thrown).isInstanceOf(ChaincodeException.class).hasNoCause()
                .hasMessage("Undefined contract method called");
        assertThat(((ChaincodeException) thrown).getPayload()).isEqualTo(null);

        verifyZeroInteractions(ctx);
    }

    @Test
    @DisplayName("데이터 조회 트랜잭션 - 데이터 있는 경우")
    public void query_whenUserPublicKeyExists(){
        SSOChaincode contract = new SSOChaincode();
        Context ctx = mock(Context.class);
        ChaincodeStub stub = mock(ChaincodeStub.class);
        when(ctx.getStub()).thenReturn(stub);
        when(stub.getStringState("PUBKEY1"))
                .thenReturn("{ \"ci\": \"ci_1\", \"publicKey\": \"publicKey_1\",\"num1\":123,\"num2\":456}");

        UserPublicKey userPublicKey = contract.queryLedger(ctx, "PUBKEY1", 1);

        assertThat(userPublicKey).isEqualTo(new UserPublicKey("ci_1", "publicKey_1", 1, 457));
    }

    @Test
    @DisplayName("데이터 조회 트랜잭션 - 데이터 없는 경우")
    public void query_whenUserPublicKeyDoesNotExist(){
        SSOChaincode contract = new SSOChaincode();
        Context ctx = mock(Context.class);
        ChaincodeStub stub = mock(ChaincodeStub.class);
        when(ctx.getStub()).thenReturn(stub);
        when(stub.getStringState("PUBKEY0")).thenReturn("");

        Throwable thrown = catchThrowable(() -> {
            contract.queryLedger(ctx, "PUBKEY0", 1);
        });

        assertThat(thrown).isInstanceOf(ChaincodeException.class).hasNoCause()
                .hasMessage("UserPublicKey PUBKEY0 does not exist");
        assertThat(((ChaincodeException)thrown).getPayload()).isEqualTo("NOT_FOUND".getBytes());
    }

    @Test
    @DisplayName("체인코드 초기세팅 트랜잭션")
    public void invokeInitLedgerTransaction() {
        SSOChaincode contract = new SSOChaincode();
        Context ctx = mock(Context.class);
        ChaincodeStub stub = mock(ChaincodeStub.class);
        when(ctx.getStub()).thenReturn(stub);

        contract.initLedger(ctx);

        InOrder inOrder = inOrder(stub);
        inOrder.verify(stub).putStringState("PUBKEY0",
                "{\"ci\":\"ci_0\",\"num1\":123,\"num2\":456,\"publicKey\":\"publicKey_0\"}");
        inOrder.verify(stub).putStringState("PUBKEY1",
                "{\"ci\":\"ci_1\",\"num1\":123,\"num2\":456,\"publicKey\":\"publicKey_1\"}");
        inOrder.verify(stub).putStringState("PUBKEY2",
                "{\"ci\":\"ci_2\",\"num1\":123,\"num2\":456,\"publicKey\":\"publicKey_2\"}");
        inOrder.verify(stub).putStringState("PUBKEY3",
                "{\"ci\":\"ci_3\",\"num1\":123,\"num2\":456,\"publicKey\":\"publicKey_3\"}");
        inOrder.verify(stub).putStringState("PUBKEY4",
                "{\"ci\":\"ci_4\",\"num1\":123,\"num2\":456,\"publicKey\":\"publicKey_4\"}");
        inOrder.verify(stub).putStringState("PUBKEY5",
                "{\"ci\":\"ci_5\",\"num1\":123,\"num2\":456,\"publicKey\":\"publicKey_5\"}");
    }

    @Test
    @DisplayName("데이터 생성 트랜잭션 - 이미 해당 키에 데이터가 존재하는 경우")
    public void create_whenUserPublicKeyExists(){
        SSOChaincode contract = new SSOChaincode();
        Context ctx = mock(Context.class);
        ChaincodeStub stub = mock(ChaincodeStub.class);
        when(ctx.getStub()).thenReturn(stub);
        when(stub.getStringState("PUBKEY0"))
                .thenReturn("{\"ci\":\"ci_0\",\"publicKey\":\"publicKey_0\",\"num1\":123,\"num2\":456}");

        Throwable thrown = catchThrowable(() -> {
            contract.createUserPublicKey(ctx, "PUBKEY0", "ci_0", "publicKey_0");
        });

        assertThat(thrown).isInstanceOf(ChaincodeException.class).hasNoCause()
                .hasMessage("UserPublicKey PUBKEY0 already exists");
        assertThat(((ChaincodeException) thrown).getPayload()).isEqualTo("ALREADY_EXISTS".getBytes());
    }

    @Test
    @DisplayName("데이터 생성 트랜잭션 - 해당 키에 데이터가 아직 없는 경우")
    public void create_whenUserPublicKeyDoesNotExist(){
        SSOChaincode contract = new SSOChaincode();
        Context ctx = mock(Context.class);
        ChaincodeStub stub = mock(ChaincodeStub.class);
        when(ctx.getStub()).thenReturn(stub);
        when(stub.getStringState("PUBKEY0")).thenReturn("");

        UserPublicKey car = contract.createUserPublicKey(ctx, "PUBKEY0", "ci_0", "publicKey_0");

        assertThat(car).isEqualTo(new UserPublicKey("ci_0", "publicKey_0", 123, 456));
    }

}
