package com.github.hurshi.clickedwordslib;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public abstract class WordDetailDialog extends AppCompatDialogFragment {
    private OnBottomDialogDismissListener listener;
    private String words;
    private FragmentManager manager;

    public WordDetailDialog() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.BottomDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(true);
        return inflater.inflate(getLayoutRes(), container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Window window = getDialog().getWindow();
        window.setAttributes(getDialogLayoutParams(window.getAttributes()));

        getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
//                FragmentTransaction ft = manager.beginTransaction();
//                ft.hide(WordDetailDialog.this);
//                ft.commitAllowingStateLoss();
//                dismiss();
                onDialogDismiss();
                if (null != listener) {
                    listener.onDismiss();
                }
            }
        });
        setUpView(getView(), words);
    }


    public void setListener(OnBottomDialogDismissListener listener) {
        this.listener = listener;
    }

    private Disposable disposable;

    public boolean setWords(String w) {
        this.words = w;
        if (null != getView()) {
            if (null != disposable && !disposable.isDisposed()) {
                disposable.dispose();
                if (null != listener) {
                    listener.onDismiss();
                }
            }
            disposable = Observable.just(words)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String string) throws Exception {
                            setUpView(getView(), words);
                            disposable = null;
                        }
                    });
        }
        return true;
    }

    public void show(FragmentManager manager) {
        this.manager = manager;
        show(manager, getFragmentTag());
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        if (null == manager) {
            return;
        }
        Fragment fragment = manager.findFragmentByTag(tag);
        FragmentTransaction ft = manager.beginTransaction();
        if (null == fragment) {
            ft.add(this, tag);
        } else {
            ft.show(this);
            getDialog().show();
        }
        ft.commitAllowingStateLoss();
    }

    public abstract int getLayoutRes();

    public abstract String getFragmentTag();

    public abstract void setUpView(View v, String word);

    protected void onDialogDismiss() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    protected WindowManager.LayoutParams getDialogLayoutParams(WindowManager.LayoutParams params) {
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        return params;
    }

    public interface OnBottomDialogDismissListener {
        void onDismiss();
    }
}
