package net.imoran.auto.morwechat.utils;

import android.text.TextUtils;

/**
 * 通用的Event，尽量避免创建不必要的类，包含一个自定义的标识码和一个泛型对象
 * what：代表当前这个Event是干什么用的，要求必须唯一的，值可以声明在EventConstants类中，防止出现重复
 * data：是一个泛型，可以指定任意类型，只需要在接受时转换一下即可
 */
public class CommonEvent<T> {

    public String what; //区分标示
    public T data;  //数据载体
    public static final String TYPE_HISTORY = "type_history";
    public static final String TYPE_RECEIVER = "type_receiver";
    public static final String TYPE_CLEAR_HISTORY = "type_clear_history";
    public static final String TYPE_ADAPTER = "type_adapter";


    public CommonEvent(String what) {
        this(what, null);
    }

    public CommonEvent(String what, T data) {
        this.what = what;
        this.data = data;
        verifyWhat();
    }

    private void verifyWhat() {
        if (TextUtils.isEmpty(what.trim())) {
            throw new IllegalArgumentException("CommonEvent 中what不能为空");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (o instanceof String) {
            return this.what.equals(o);
        } else if (o instanceof CommonEvent) {
            return this.what.equals(((CommonEvent) o).what);
        } else
            return super.equals(o);
    }

}
