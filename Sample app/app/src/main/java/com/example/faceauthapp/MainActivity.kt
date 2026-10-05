package com.example.faceauthapp

import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.uidai.faceauth.FaceAuthSDK
import com.uidai.faceauth.helper.InitializationProgressCallback
import com.uidai.faceauth.RegistrationRequestPayload
import com.uidai.faceauth.helper.SDKResult
import com.uidai.faceauth.SDKState
import com.uidai.faceauth.network.models.*
import com.uidai.faceauth.auths.AuthError
import com.uidai.faceauth.auths.AuthParams
import com.uidai.faceauth.auths.AuthResult
import com.uidai.faceauth.auths.AuthenticationCallback
import com.uidai.faceauth.auths.CaptureProgress
import com.uidai.faceauth.auths.Opts
import com.uidai.faceauth.auths.PidOptions
import com.uidai.faceauth.auths.PidResult
import com.uidai.faceauth.helper.Environment
import com.uidai.faceauth.helper.FaceAuthConfig
import java.nio.charset.StandardCharsets
import java.security.KeyFactory
import java.security.Signature
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.UUID

class MainActivity : AppCompatActivity() {

    companion object {
        private const val AUA_PRIVATE_KEY =
            "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCLQl8x1hPk+YEE" +
                    "UQgOpLri4HnK3lUv7+DUiiIlGb8U5ZeptGk4dJdAiZA49UqXqKAsepJ/fIqUUHQy" +
                    "aeuReIpF8/+mXRQSR78fL7xOtq97LyJLu6nlcgnHFAuBG5PGS9jIaQKZpLbfl9ni" +
                    "s8ytcDQfalie3fdr28FzxE4pdo4wgIHcPHYKvRmnD61lg7YA2i3XIxQT40T2dWSu4" +
                    "YkIeSOX/YXYcbBX+aIp5ydmR6jXCLXfwD91LsFkVnQl8AQHF1ScodgLmMXB2T3Og" +
                    "JA9DIaiuaTVpVY67uPCqvEQS+vJG2yDG2kW2CFWtplxt6XM/w2jeRTzX4mHY4Ia/" +
                    "3QIBLVVAgMBAAECggEAAdrje713TGCKeG0/5zoIh0coOCyYA+h1HT+tzsK2Ceg5Cfi" +
                    "uQiNDdIeglTGnSbfkUlNbqjkVe35K3fUNP0XUybPDXwr0Xn34LMks//ttAP4SveGF" +
                    "FNpdCPraYGDmqgiOnbTQZQ2NphLWwy9T9ad9+kEnZw5b4mLuA8XEqMa8hCbc//DIE" +
                    "SoIvY1v96tnutdDO6pVRdew9bErnKlKQ0hGC9W/axU6D2hM2fthNDgXCTH8DTmrvw" +
                    "tM21jq/IWpz5tLGZ28Mr+FeFcX2v22Hl8clzEAkq7B2JNbWH90f8ntviCwI/I/OZQ" +
                    "yXp3r0LaFsGv/vrQZHLchTtdHOmVBtM+NAQKBgQDYvvphB1/+aSA7iYEWtOW4RhBy" +
                    "KNbpStXVjWE4LExMUEwJ8GrPzvWwCAkdJtJ0xDK4nzhX2YPh6zC2inSmZgpY8UyYV" +
                    "ibtjKfxLf6MX5K+zuZXgymjT+TDlcxbm7Tz2VitzRMlkbTY3xW5T/ZUuyWNkpCur" +
                    "QDiGR+urzgyT+5gQQKBgQCket3Ki7EAYonUpofuG0dsymHTbKvwhlfrQhRs1+mXnWF" +
                    "a0dg1rrznyT2VEaA8M2E06A2hm1xQLm/VOxur2HFblfmExae8rwQVLfo2R0S3BhbRF" +
                    "IMrDLPvii3Ciws6x3XWRxLFLxgnrcr5wEzIJGpQD8fnWx5tYJqa68hmWsLQFQKBgQ" +
                    "CC23JwyDIVeESkjVTpb5fNlEdzlOKDgNHvF2ddsajy3fu6eHMUOYAtofxWpEylgJqa" +
                    "yk4LA+DSsdr8KkVY/Pw8TJqQ0lq4SpZOTxWCjGUdW+7HYQHvflnAF52EpVIvcsZOU" +
                    "CTCJuEHmlAUTImNCFwYyCcfvoVs+/wby9/iyE1DwQKBgA+2oTu5brZ/s3U6V6AawQ8" +
                    "WopPlPOPkob+a94KSegF/Co73nzU5tVhbdGOqZGl76xx30t2Ojxol5Y80b2dSbTn3s" +
                    "PfLPkQbYEN5uf63fkwR69oboJBbTVFkHQ0J5Y+/AHXxIkzuO9uGke6cPXmdaP6vE0" +
                    "T6AoUmhn6oaePFZfklAoGAFFhS5N/hfHhP9ldJmEo3ErM2nCTJa8BIQOKK3ZpAZRZ" +
                    "UyiP+pcMJXLlOY+m+ejyEHqfdE2VJtTJh9aUaKjONScdviPYTZtFFkAFQs9F1rWoy" +
                    "/BtPqneOdRNqf5KW3WhvI4a9KbpSt/zJnKWproGwQACQ/j8SvlJEeCscLhs94V4="


    }

    private lateinit var statusText: TextView
    private lateinit var batchStatusText: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        statusText = findViewById(R.id.status_text)
        batchStatusText = findViewById(R.id.batch_status_text)
        val startButton = findViewById<Button>(R.id.btn_start_auth)
        val pidButton = findViewById<Button>(R.id.btn_pid_generation)

        val config = FaceAuthConfig.Builder()
            .setEnvironment(Environment.PROD)
            .build()

        startButton.isEnabled = false
        pidButton.isEnabled = false



        // STEP 1: Register device and sync with backend in one step
        statusText.text = "Registering device with UIDAI..."

        if (FaceAuthSDK.getState(this) == SDKState.NOT_REGISTERED) {
            FaceAuthSDK.initiateDeviceRegistration(this, "uidai_face_sdk_stg_l1") { result ->
                when (result) {
                    is SDKResult.Success -> {
                        val payload = result.data as RegistrationRequestPayload
                        val txnId = UUID.randomUUID().toString()
                        val approvalResponse = signJson(payload, txnId, AUA_PRIVATE_KEY)
                        FaceAuthSDK.completeDeviceRegistration(this, approvalResponse) { comp ->
                            if (comp is SDKResult.Success) {
                                initializeFaceAuthSdk(startButton, pidButton, "", config)
                            } else if (comp is SDKResult.Error) {
                                statusText.text = "Final Registration Failed: ${comp.message}"
                            }
                        }
                    }
                    is SDKResult.Error -> {
                        statusText.text = "Initiate Failed: ${result.message}"
                    }
                }
            }
        } else {
            initializeFaceAuthSdk(startButton, pidButton, "", config)
        }

        startButton.setOnClickListener { startFaceAuth() }
        pidButton.setOnClickListener { startDirectPidAuth() }
    }

    private fun startDirectPidAuth() {
        val params = AuthParams(
            returnCapturedImage = true,
            generatePid = true,
            skipFaceMatching = true,
            txnId = "abcd123",
            uid ="999906582675",
            language = "hi",
            pidOptions = PidOptions(
                opts = Opts(
                    wadh = "",
                 fType =  2,
                    fCount = 1,
                    pidVer = "5"
                ),


//                custOpts = CustOpts(
//                    params = listOf(
//                        CustParam(name = "language", value = "")
//                    ))

            )

        )
        FaceAuthSDK.startAuthentication(this, "", params, authCallback, )
    }

    private fun startFaceAuth() {
        val refImage = loadReferenceImageFromAssets()
        if (refImage.isEmpty()) return

        val params = AuthParams(
            enableLivenessCheck = true,
            returnCapturedImage = true,
            generatePid = true,
            txnId = "abcd123"
        )
        FaceAuthSDK.startAuthentication(this, refImage, params, authCallback)
    }

    private val authCallback = object : AuthenticationCallback {
        override fun onSuccess(result: AuthResult.Success) {
            runOnUiThread { statusText.text = "Match Success" }
        }
        override fun onFailure(error: AuthError, errorCode: Int, message: String) {
            Log.e("Pid Data-xml line 171", "Error: $message, Code: $errorCode")
            runOnUiThread { statusText.text = "Failed: $error - $message" }

        }
        override fun onCaptureProgress(progress: CaptureProgress) {
            runOnUiThread { statusText.text = "Status: ${progress.status}" }
        }
        override fun onReady() { FaceAuthSDK.generatePidBlock() }
        override fun onPIDReady(pidData: PidResult) {
            Log.e("Pid Data-xml",pidData.xml.toString())
            runOnUiThread { statusText.text = "PID Generation Complete" }
        }
        override fun onCancelled() { runOnUiThread { statusText.text = "Cancelled" } }
    }

    private fun loadReferenceImageFromAssets(): String {
        return try {
            val inputStream = assets.open("reference_image.png")
            Base64.encodeToString(inputStream.readBytes(), Base64.DEFAULT)
        } catch (e: Exception) { "" }
    }

    private fun initializeFaceAuthSdk(
        startButton: Button,
        pidButton: Button,
        cert: String?,
        config: FaceAuthConfig?
    ) {
        FaceAuthSDK.init(
            context = applicationContext,
            packageId = BuildConfig.APPLICATION_ID,
            serverCertificateBase64 = cert,
            config = config,
            onReady = {
                runOnUiThread {
                    statusText.text = "SDK Ready"
                    startButton.isEnabled = true
                    pidButton.isEnabled = true
                }
            },

            progressCallback = object : InitializationProgressCallback {
                override fun onLibraryDownloadStarted(architecture: String) {}
                override fun onLibraryDownloadProgress(percentComplete: Int, architecture: String) {}
                override fun onLibraryDownloadCompleted(architecture: String) {}
                override fun onDownloadStarted() {}
                override fun onDownloadProgress(percentComplete: Int) {}
                override fun onDownloadCompleted() {}
                override fun onStatus(message: String) {
                    runOnUiThread { statusText.text = message }
                }
            },
            onError = { _, message ->
                runOnUiThread {
                    statusText.text = "SDK Init Failed: $message"
                }
            }
        )
    }

    /**
     * MOCK AUA BACKEND - Constructing Approval and Signing
     */
    fun signJson(payload: RegistrationRequestPayload, txnId: String, privateKeyB64: String): String {
        val registrationInfo = payload.registrationBlock
        val gson = com.google.gson.Gson()
        val isDeviceSigValid = verifyDeviceSignature(payload)
        Log.d("AUA_BACKEND", "Incoming Device Signature Valid: $isDeviceSigValid")

        val timeStamp = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }.format(Date())

        // Constructed Approval Block
        val approvalBlock = RegistrationApprovalBlock(
            id =  UUID.randomUUID().toString(),
            ts = timeStamp,
            device = RegistrationApprovalDevice(
                dpId = registrationInfo.device.dpId,
                dc = registrationInfo.device.dc,
                auacode = registrationInfo.device.auacode,
                mi = registrationInfo.device.mi,
                idHash = registrationInfo.device.idHash,
                rdsId = registrationInfo.device.rdsId,
                rdsVer = registrationInfo.device.rdsVer,
                appPackageName = registrationInfo.device.appPackageName

            ),
            keyInfo = RegistrationApprovalKeyInfo(
                mc = registrationInfo.keyInfo.mc,
                keyAlgo = registrationInfo.keyInfo.keyAlgo,
                aChain = registrationInfo.keyInfo.aChain
            ),
            appCertHash = registrationInfo.appCertHash,
            uidaiNonce = registrationInfo.uidaiNonce
        )

        val approvalJson = gson.toJson(approvalBlock)
        val digest = java.security.MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(approvalJson.toByteArray(StandardCharsets.UTF_8))
        val base64Hash = Base64.encodeToString(hashBytes, Base64.NO_WRAP)

        // Sign the approval
        val signatureValue = signJsonPayload(approvalJson, privateKeyB64)


        // Wrap into final response
        val finalResponse = mapOf(
            "ver" to "1.0",
            "txn" to txnId,
            "approval" to approvalBlock,
            "signature" to mapOf(
                "signedRef" to approvalBlock.id,
                "algo" to "RS256",
                "hash" to mapOf(
                    "algo" to "SHA-256",
                    "value" to base64Hash
                ),
                "value" to signatureValue
            )
        )

        return gson.toJson(finalResponse)
    }

    //verify Device Signatures
    private fun verifyDeviceSignature(payload: RegistrationRequestPayload): Boolean {
        return try {
            val registrationBlock = payload.registrationBlock
            val signatureObject = payload.signature
            val gson = com.google.gson.Gson()

            val publicKeyBytes = Base64.decode(registrationBlock.keyInfo.mc, Base64.NO_WRAP)
            val keySpec = X509EncodedKeySpec(publicKeyBytes)

            // added fallback logic for Key Algorithm ---
            val rawAlgo = registrationBlock.keyInfo.keyAlgo
            val keyAlgo = when {
                rawAlgo.contains("RSA", ignoreCase = true) -> "RSA"
                rawAlgo.contains("EC", ignoreCase = true) -> "EC"
                rawAlgo == "Unknown" -> "RSA" // Fallback to RSA if unknown
                else -> "RSA" // Default to RSA
            }

            val keyFactory = KeyFactory.getInstance(keyAlgo)
            val publicKey = keyFactory.generatePublic(keySpec)

            val dataToVerify = gson.toJson(registrationBlock)

            val sigAlgo = if (signatureObject.algo == "RS256" || keyAlgo == "RSA") {
                "SHA256withRSA"
            } else {
                "SHA256withECDSA"
            }

            val verifier = Signature.getInstance(sigAlgo)
            verifier.initVerify(publicKey)
            verifier.update(dataToVerify.toByteArray(StandardCharsets.UTF_8))

            verifier.verify(Base64.decode(signatureObject.value, Base64.NO_WRAP))
        } catch (e: Exception) {
            Log.e("AUA_BACKEND", "Device signature verification failed: ${e.message}", e)
            false
        }
    }
    private fun signJsonPayload(json: String, privateKeyB64: String): String {
        val privateKeyBytes = Base64.decode(privateKeyB64, Base64.NO_WRAP)
        val keySpec = PKCS8EncodedKeySpec(privateKeyBytes)
        val privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpec)
        val sig = Signature.getInstance("SHA256withRSA")
        sig.initSign(privateKey)
        sig.update(json.toByteArray(StandardCharsets.UTF_8))
        return Base64.encodeToString(sig.sign(), Base64.NO_WRAP)
    }
}

