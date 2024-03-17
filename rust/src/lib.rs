use ast_grep_core::{AstGrep, Language, NodeMatch, StrDoc};
use ast_grep_language::SupportLang;
use jni::JNIEnv;
use jni::objects::JClass;
use jni::objects::JString;
use jni::objects::JValue;
use jni::sys::jlong;
use jni::sys::jobject;

mod node;
mod utils;

#[derive(Clone)]
pub struct Root {
    inner: AstGrep<StrDoc<SupportLang>>,
    filename: String,
}
unsafe impl Send for Root {}

#[no_mangle]
pub unsafe extern "system" fn Java_io_github_zheaoli_astgrep_Root_disposeInternal(
    _: JNIEnv,
    _: JClass,
    root: *mut Root,
) {
    drop(Box::from_raw(root));
}

#[no_mangle]
pub extern "system" fn Java_io_github_zheaoli_astgrep_Root_constructor(
    mut env: JNIEnv,
    _: JClass,
    code: JString,
    language: JString,
) -> jlong {
    let lang: SupportLang = utils::jstring_to_string(&mut env, &language)
        .unwrap()
        .parse()
        .unwrap();
    let inner = lang.ast_grep(utils::jstring_to_string(&mut env, &code).unwrap());
    let result = Root {
        inner,
        filename: "anonymous".into(),
    };
    Box::into_raw(Box::new(result)) as jlong
}

#[no_mangle]
pub unsafe extern "system" fn Java_io_github_zheaoli_astgrep_Root_root(
    mut env: JNIEnv,
    _: JClass,
    root: *mut Root,
) -> jobject {
    let root = &mut *root;
    let tree = unsafe { &*(&root.inner as *const AstGrep<_>) } as &'static AstGrep<_>;
    let inner = NodeMatch::from(tree.root());
    let node = node::Node {
        inner,
        root: root.clone(),
    };
    let native_node = Box::into_raw(Box::new(node)) as jlong;
    let object = env
        .new_object(
            "io/github/zheaoli/astgrep/Node",
            "(J)V",
            &[JValue::Long(native_node)],
        )
        .unwrap();
    object.into_raw()
}

#[no_mangle]
pub unsafe extern "system" fn Java_io_github_zheaoli_astgrep_Root_filename(
    mut env: JNIEnv,
    _: JClass,
    root: *mut Root,
) -> jobject {
    let root = &mut *root;
    utils::string_to_jstring(&mut env, Some(root.filename.as_str()))
        .unwrap()
        .into_raw()
}
