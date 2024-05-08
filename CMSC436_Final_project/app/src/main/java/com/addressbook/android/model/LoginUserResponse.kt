package com.addressbook.android.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable
//A data class representing the response structure of a login request.
// It includes several nested classes and annotations for JSON serialization.
data class LoginUserResponse(
    @SerializedName("args")
    var args: Args,
    @SerializedName("data")
    var `data`: Data,
    @SerializedName("files")
    var files: Files,
    @SerializedName("form")
    var form: Form,
    @SerializedName("headers")
    var headers: Headers,
    @SerializedName("json")
    var json: Json,
    @SerializedName("url")
    var url: String
): Serializable {
    class Args

    data class Data(
        @SerializedName("Email")
        var email: String,
        @SerializedName("Password")
        var password: String
    ): Serializable

    class Files

    class Form

    data class Headers(
        @SerializedName("accept-encoding")
        var acceptEncoding: String,
        @SerializedName("content-length")
        var contentLength: String,
        @SerializedName("content-type")
        var contentType: String,
        @SerializedName("host")
        var host: String,
        @SerializedName("user-agent")
        var userAgent: String,
        @SerializedName("x-amzn-trace-id")
        var xAmznTraceId: String,
        @SerializedName("x-forwarded-port")
        var xForwardedPort: String,
        @SerializedName("x-forwarded-proto")
        var xForwardedProto: String
    ): Serializable

    data class Json(
        @SerializedName("Email")
        var email: String,
        @SerializedName("Password")
        var password: String
    ): Serializable
}