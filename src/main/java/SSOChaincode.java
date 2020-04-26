import com.owlike.genson.Genson;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.*;
import org.hyperledger.fabric.shim.ChaincodeStub;

@Contract(
    name = "SSOChaincode",
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
    public void initLedger(final Context ctx){  // 체인코드 초기화 작업 수행.
        // TODO:: 배치성 작업을 위한 스레드 같은거 돌려놔도 잘 동작하는지 테스트해보기.. 안된다면 애플리케이션 단에 배치 작업 만들어두고 컨트렉트 호출하게끔 구현해야함..

        ChaincodeStub stub = ctx.getStub();


    }

    @Transaction()
    public String queryLedger(final Context context, String key){
        String asdf = "asdf";
        return asdf;
    }

}
