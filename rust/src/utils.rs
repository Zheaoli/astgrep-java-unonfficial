use jni::objects::JByteArray;
use jni::objects::JClass;
use jni::objects::JObject;
use jni::objects::JString;
use jni::objects::JValue;
use jni::objects::JValueOwned;
use jni::sys::jlong;
use jni::sys::jobject;
use jni::sys::jsize;
use jni::JNIEnv;

use jni::errors::Error;

pub(crate) fn jstring_to_string(env: &mut JNIEnv, s: &JString) -> Result<String,Error> {
    let res = unsafe { env.get_string_unchecked(s)? };
    Ok(res.into())
}

pub(crate) fn string_to_jstring<'a>(
    env: &mut JNIEnv<'a>,
    s: Option<&str>,
) -> Result<JObject<'a>, Error> {
    s.map_or_else(
        || Ok(JObject::null()),
        |v| Ok(env.new_string(v.to_string())?.into()),
    )
}
