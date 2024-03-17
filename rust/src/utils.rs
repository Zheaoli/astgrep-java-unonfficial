use jni::errors::Error;
use jni::JNIEnv;
use jni::objects::JObject;
use jni::objects::JString;

pub(crate) fn jstring_to_string(env: &mut JNIEnv, s: &JString) -> Result<String, Error> {
    let res = env.get_string(s)?;
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
