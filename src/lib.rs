#[allow(non_snake_case)]
pub mod android {
    use jni::{objects::JClass, sys::jstring, JNIEnv};

    #[no_mangle]
    pub extern "system" fn Java_com_test_withrust_RustCaller_00024Companion_hello(
        env: JNIEnv,
        _: JClass,
    ) -> jstring {
        let message = env
            .new_string("Hello from Rust")
            .expect("Failed to create Java string");

        message.into_raw()
    }
}