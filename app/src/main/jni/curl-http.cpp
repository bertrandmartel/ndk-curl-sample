#include <string.h>
#include <jni.h>
#include <iostream>
#include <string>
#include <curl/curl.h>
#include <android/log.h> 

using namespace std;

string response;

size_t writeCallback(char* buf, size_t size, size_t nmemb, void* up) {

	for (int c = 0; c<size*nmemb; c++) {
		response.push_back(buf[c]);
	}

	return size*nmemb;
}


extern "C" {

	JNIEXPORT jstring JNICALL 
	Java_fr_bmartel_android_curlndk_CurlActivity_getCurlResponse(JNIEnv *env, jobject thiz) {

		CURL* curl;
		
		curl_global_init(CURL_GLOBAL_ALL);

		curl=curl_easy_init();

		curl_easy_setopt(curl, CURLOPT_URL, "http://jsonplaceholder.typicode.com/posts/1");
		curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, &writeCallback);

		curl_easy_perform(curl);

		curl_easy_cleanup(curl);
		curl_global_cleanup();

		return env->NewStringUTF(response.c_str());
	}
}