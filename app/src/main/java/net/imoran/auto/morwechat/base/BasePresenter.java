package net.imoran.auto.morwechat.base;

/**
 * Created by xinhua.shi on 2018/12/4.
 */

public abstract class BasePresenter<M extends BaseModel, V extends BaseView> {
    protected M model;
    protected V view;

    public BasePresenter(V view) {
        this.view = view;
        model = createModel();
    }

    protected abstract M createModel();

    protected void onDestroy() {
        model.onDestroy();
    }
}
