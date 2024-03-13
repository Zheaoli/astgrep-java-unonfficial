use anyhow::Error;
use ast_grep_config::from_str;
use ast_grep_config::{DeserializeEnv, RuleCore, SerializableRule, SerializableRuleCore};
use ast_grep_core::{NodeMatch, StrDoc};
use ast_grep_language::SupportLang;

use jni::objects::JByteArray;
use jni::objects::JClass;
use jni::objects::JObject;
use jni::objects::JString;
use jni::objects::JValue;
use jni::objects::JValueOwned;
use jni::sys::jboolean;
use jni::sys::jlong;
use jni::sys::jobject;
use jni::sys::jobjectArray;
use jni::sys::jsize;
use jni::sys::jstring;
use jni::JNIEnv;

use super::utils;

pub struct Node {
    pub inner: NodeMatch<'static, StrDoc<SupportLang>>,
    // refcount SgRoot
    pub(crate) root: super::Root,
}

unsafe impl Send for Node {}

#[no_mangle]
pub unsafe extern "system" fn Java_io_github_zheaoli_astgrep_Node_disposeInternal(
    _: JNIEnv,
    _: JClass,
    root: *mut Node,
) {
    drop(Box::from_raw(root));
}

#[no_mangle]
pub unsafe extern "system" fn Java_io_github_zheaoli_astgrep_Node_isLeaf(
    _: JNIEnv,
    _: JClass,
    node: *mut Node,
) -> jboolean {
    let node = &mut *node;
    let result = node.inner.is_leaf();
    result as jboolean
}

#[no_mangle]
pub unsafe extern "system" fn Java_io_github_zheaoli_astgrep_Node_isNamed(
    _: JNIEnv,
    _: JClass,
    node: *mut Node,
) -> jboolean {
    let node = &mut *node;
    let result = node.inner.is_named();
    result as jboolean
}

#[no_mangle]
pub unsafe extern "system" fn Java_io_github_zheaoli_astgrep_Node_isNamedLeaf(
    _: JNIEnv,
    _: JClass,
    node: *mut Node,
) -> jboolean {
    let node = &mut *node;
    let result = node.inner.is_named_leaf();
    result as jboolean
}

#[no_mangle]
pub unsafe extern "system" fn Java_io_github_zheaoli_astgrep_Node_kind(
    mut env: JNIEnv,
    _: JClass,
    node: *mut Node,
) -> jobject {
    let node = &mut *node;
    let result = node.inner.kind().to_string();
    utils::string_to_jstring(&mut env, Some(result.as_str()))
        .unwrap()
        .into_raw()
}

#[no_mangle]
pub unsafe extern "system" fn Java_io_github_zheaoli_astgrep_Node_text(
    mut env: JNIEnv,
    _: JClass,
    node: *mut Node,
) -> jobject {
    let node = &mut *node;
    let result = node.inner.text().to_string();
    utils::string_to_jstring(&mut env, Some(result.as_str()))
        .unwrap()
        .into_raw()
}

#[no_mangle]
pub unsafe extern "system" fn Java_io_github_zheaoli_astgrep_Node_matches(
    mut env: JNIEnv,
    _: JClass,
    node: *mut Node,
    rule: JString,
) -> jboolean {
    let node = &mut *node;
    let rule = utils::jstring_to_string(&mut env, &rule).unwrap();
    let matcher = get_matcher_from_rule(node.inner.lang(), rule).unwrap();
    node.inner.matches(matcher) as jboolean
}

#[no_mangle]
pub unsafe extern "system" fn Java_io_github_zheaoli_astgrep_Node_has(
    mut env: JNIEnv,
    _: JClass,
    node: *mut Node,
    rule: JString,
) -> jboolean {
    let node = &mut *node;
    let rule = utils::jstring_to_string(&mut env, &rule).unwrap();
    let matcher = get_matcher_from_rule(node.inner.lang(), rule).unwrap();
    node.inner.has(matcher) as jboolean
}

#[no_mangle]
pub unsafe extern "system" fn Java_io_github_zheaoli_astgrep_Node_inside(
    mut env: JNIEnv,
    _: JClass,
    node: *mut Node,
    rule: JString,
) -> jboolean {
    let node = &mut *node;
    let rule = utils::jstring_to_string(&mut env, &rule).unwrap();
    let matcher = get_matcher_from_rule(node.inner.lang(), rule).unwrap();
    node.inner.inside(matcher) as jboolean
}

#[no_mangle]
pub unsafe extern "system" fn Java_io_github_zheaoli_astgrep_Node_precedes(
    mut env: JNIEnv,
    _: JClass,
    node: *mut Node,
    rule: JString,
) -> jboolean {
    let node = &mut *node;
    let rule = utils::jstring_to_string(&mut env, &rule).unwrap();
    let matcher = get_matcher_from_rule(node.inner.lang(), rule).unwrap();
    node.inner.precedes(matcher) as jboolean
}

#[no_mangle]
pub unsafe extern "system" fn Java_io_github_zheaoli_astgrep_Node_follows(
    mut env: JNIEnv,
    _: JClass,
    node: *mut Node,
    rule: JString,
) -> jboolean {
    let node = &mut *node;
    let rule = utils::jstring_to_string(&mut env, &rule).unwrap();
    let matcher = get_matcher_from_rule(node.inner.lang(), rule).unwrap();
    node.inner.follows(matcher) as jboolean
}

#[no_mangle]
pub unsafe extern "system" fn Java_io_github_zheaoli_astgrep_Node_find(
    mut env: JNIEnv,
    _: JClass,
    node: *mut Node,
    core: JString,
) -> jobject {
    let node = &mut *node;
    let config_origin_data = utils::jstring_to_string(&mut env, &core).unwrap().clone();
    let config: SerializableRuleCore = serde_json::from_str(config_origin_data.as_str()).unwrap();
    // let config :SerializableRuleCore= from_str("{rule: {pattern: let $A = $B}, fix: yjsnp}").unwrap();
    let lang = node.inner.lang();
    let ast_env = DeserializeEnv::new(*lang);
    let matcher = config.get_matcher(ast_env).unwrap();
    if let Some(inner) = node.inner.find(matcher) {
        let native_node = Box::into_raw(Box::new(Node {
            inner,
            root: node.root.clone(),
        })) as jlong;
        env.new_object(
            "io/github/zheaoli/astgrep/Node",
            "(J)V",
            &[JValue::Long(native_node)],
        )
        .unwrap()
        .into_raw()
    } else {
        JObject::null().into_raw()
    }
}

#[no_mangle]
pub unsafe extern "system" fn Java_io_github_zheaoli_astgrep_Node_getMatch(
    mut env: JNIEnv,
    _: JClass,
    node: *mut Node,
    meta: JString,
) -> jobject {
    let node = &mut *node;
    let metadata = utils::jstring_to_string(&mut env, &meta).unwrap().clone();
    let new_node = node
        .inner
        .get_env()
        .get_match(metadata.as_str())
        .cloned()
        .map(|n| Node {
            inner: NodeMatch::from(n),
            root: node.root.clone(),
        });
    if let Some(new_node) = new_node {
        let native_node = Box::into_raw(Box::new(new_node)) as jlong;
        env.new_object(
            "io/github/zheaoli/astgrep/Node",
            "(J)V",
            &[JValue::Long(native_node)],
        )
        .unwrap()
        .into_raw()
    } else {
        JObject::null().into_raw()
    }
}

#[no_mangle]
pub unsafe extern "system" fn Java_io_github_zheaoli_astgrep_Node_getMultipleMatches(
    mut env: JNIEnv,
    _: JClass,
    node: *mut Node,
    meta: JString,
) -> jobjectArray {
    let node = &mut *node;
    let metadata = utils::jstring_to_string(&mut env, &meta).unwrap().clone();
    let nodes: Vec<Node> = node
        .inner
        .get_env()
        .get_multiple_matches(&metadata)
        .into_iter()
        .map(|n| Node {
            inner: NodeMatch::from(n),
            root: node.root.clone(),
        })
        .collect();
    let array_object = env
        .new_object_array(
            nodes.len() as jsize,
            "io/github/zheaoli/astgrep/Node",
            JObject::null(),
        )
        .unwrap();
    for (i, node) in nodes.iter().enumerate() {
        let native_node = Box::into_raw(Box::new(node)) as jlong;
        let value = env
            .new_object(
                "io/github/zheaoli/astgrep/Node",
                "(J)V",
                &[JValue::Long(native_node)],
            )
            .unwrap();
        env.set_object_array_element(&array_object, i as jsize, value)
            .unwrap();
    }
    array_object.into_raw()
}

#[no_mangle]
pub unsafe extern "system" fn Java_io_github_zheaoli_astgrep_Node_getTransformed(
    mut env: JNIEnv,
    _: JClass,
    node: *mut Node,
    meta: JString,
) -> jobject {
    let node = &mut *node;
    let metadata = utils::jstring_to_string(&mut env, &meta).unwrap().clone();
    let result = node
        .inner
        .get_env()
        .get_transformed(&metadata)
        .map(|n| String::from_utf8_lossy(n).to_string());
    if let Some(new_node) = result {
        utils::string_to_jstring(&mut env, Some(new_node.as_str()))
            .unwrap()
            .into_raw()
    } else {
        JObject::null().into_raw()
    }
}

fn get_matcher_from_rule(lang: &SupportLang, rule: String) -> Result<RuleCore<SupportLang>, Error> {
    let rule: SerializableRule = serde_json::from_str(&rule)?;
    let env = DeserializeEnv::new(*lang);
    let core = SerializableRuleCore {
        rule,
        constraints: None,
        utils: None,
        transform: None,
        fix: None,
    };
    Ok(core.get_matcher(env)?)
}
