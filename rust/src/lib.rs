mod utils;
use jni::objects::JByteArray;
use jni::objects::JClass;
use jni::objects::JObject;
use jni::objects::JString;
use jni::objects::JValue;
use jni::objects::JValueOwned;
use jni::sys::jlong;
use jni::sys::jobject;
use jni::sys::jsize;
use jni::sys::jstring;
use jni::JNIEnv;


use ast_grep_core::{AstGrep, Language, NodeMatch, StrDoc};
use ast_grep_language::SupportLang;

pub struct SgRoot {
    inner: AstGrep<StrDoc<SupportLang>>,
    filename: String,
  }

#[no_mangle]
pub extern "system" fn Java_io_github_zheaoli_astgrep_Root_constructor(
    mut env: JNIEnv,
    _: JClass,
    sourceCode: JString,
    language: JString,
) -> jlong {
    let lang: SupportLang = utils::jstring_to_string(&mut env,&language).unwrap().parse().unwrap();
    let inner = lang.ast_grep(utils::jstring_to_string(&mut env,&language).unwrap());
    let result=SgRoot {
      inner,
      filename: "anonymous".into(),
    };
    Box::into_raw(Box::new(result)) as jlong

}

#[no_mangle]
pub unsafe extern "system" fn Java_io_github_zheaoli_astgrep_Root_print(
  mut env: JNIEnv,
  _: JClass,
  root: *mut SgRoot,
) -> jobject {
    let root=&mut *root;
    utils::string_to_jstring(&mut env, Some(root.filename.as_str())).unwrap().into_raw()
}