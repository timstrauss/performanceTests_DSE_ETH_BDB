package genericTests.bigchaindb

import com.bigchaindb.model.GenericCallback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Response

class BDBCallBack(val success: (value: Boolean) -> Unit) : GenericCallback {
    companion object {
        val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
    }

    override fun pushedSuccessfully(response: Response?) {
        success(true)
    }

    override fun transactionMalformed(response: Response?) {
        success(false)
    }

    override fun otherError(response: Response?) {
        success(false)
    }
}