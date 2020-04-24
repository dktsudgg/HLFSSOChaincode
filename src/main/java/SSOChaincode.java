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
    public void initLedger(final Context ctx){
        ChaincodeStub stub = ctx.getStub();

        String[] userData = {

        };

        for (int i = 0; i < userData.length; i++) {
            String key = String.format("USER%d", i);

        }
    }

}
