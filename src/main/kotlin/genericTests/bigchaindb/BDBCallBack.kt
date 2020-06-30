package genericTests.bigchaindb

import com.bigchaindb.model.GenericCallback
import okhttp3.Response

class BDBCallBack(val success: (value: Boolean) -> Unit) : GenericCallback {
    override fun pushedSuccessfully(response: Response?) {
        success(true)
    }

    override fun transactionMalformed(response: Response?) {
        println("ERROR: " + response?.body?.string())
        success(false)
    }

    override fun otherError(response: Response?) {
        println("ERROR: " + response?.body?.string())
        success(false)
    }
}