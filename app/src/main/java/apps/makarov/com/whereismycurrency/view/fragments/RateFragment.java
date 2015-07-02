package apps.makarov.com.whereismycurrency.view.fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import apps.makarov.com.whereismycurrency.R;
import apps.makarov.com.whereismycurrency.models.CurrencyPair;
import apps.makarov.com.whereismycurrency.models.Rate;
import apps.makarov.com.whereismycurrency.modules.RateModule;
import apps.makarov.com.whereismycurrency.presenters.RatePresenter;
import apps.makarov.com.whereismycurrency.view.base.BaseFragment;
import apps.makarov.com.whereismycurrency.view.iviews.MainView;
import apps.makarov.com.whereismycurrency.view.iviews.RateView;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by makarov on 26/06/15.
 */

public class RateFragment extends BaseFragment implements RateView {

    public static final String TAG = RateFragment.class.getSimpleName();

    @Inject
    RatePresenter mRatePresenter;

    @InjectView(R.id.value)
    EditText valueTextView;
    @InjectView(R.id.rate)
    EditText rateTextView;
    @InjectView(R.id.enter)
    Button enterButton;
    @InjectView(R.id.date)
    TextView dateTextView;
    @InjectView(R.id.pair)
    TextView pairTextView;
    @InjectView(R.id.history_list)
    RecyclerView mRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View hotView = LayoutInflater.from(getContext()).inflate(R.layout.rate_fragment, container, false);

        ButterKnife.inject(this, hotView);

        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double value = Double.parseDouble(valueTextView.getEditableText().toString());
                double rate = Double.parseDouble(rateTextView.getEditableText().toString());
//
                mRatePresenter.onEnterOperation(Rate.RUB_CODE, Rate.EUR_CODE, value, rate);
            }
        });

        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });

        pairTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCurrencyListView();
            }
        });

        return hotView;
    }

    @Override
    public void showProgressDialog(int idRes) {

    }

    @Override
    public void hideProgressDialog() {

    }

    @Override
    public void showResultOperation(String resultKey) {
        Bundle bundle = new Bundle();
        bundle.putString(ResultFragment.RESULT_KEY_EXTRA, resultKey);
        ((MainView) getActivity()).showResultFragment(bundle);
    }

    @Override
    public void setAdapterForRecyclerView(RecyclerView.Adapter adapter) {
        mRecyclerView.setAdapter(adapter);
    }

    private void openCurrencyListView(){
        mRatePresenter.onProcessLoadCurrencyPairs();
    }

    @Override
    public void setCurrencyPairList(BaseAdapter adapter) {
        new MaterialDialog.Builder(getContext())
                .title(R.string.currency_dialog_title)
                .adapter(adapter, new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        CurrencyPair pair = Rate.getPairCodesList().get(which);
                        mRatePresenter.onEnterCurrencyPair(pair);
                        pairTextView.setText(pair.getBaseCurrency() + "_" + pair.getCompareCurrency());
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void setOldRate(String rateValue) {
        rateTextView.setText("" + rateValue);
    }

    @Override
    public void setBaseCurrency(String currency, Drawable icon) {

    }

    @Override
    public void setCompareCurrency(String currency, Drawable icon) {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initializeHistoryRecyclerView();
    }

    @Override
    public void onResume() {
        super.onResume();

        mRatePresenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mRatePresenter.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRatePresenter.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    @Override
    public Context getContext() {
        return this.getActivity();
    }

    @Override
    protected List<Object> getModules() {
        return Arrays.<Object>asList(
                new RateModule(this)
        );
    }

    private void initializeHistoryRecyclerView() {
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    DatePickerFragment.Callback mFragmentCallback = new DatePickerFragment.Callback() {
        @Override
        public void onCancelled() {
        }

        @Override
        public void onDateTimeRecurrenceSet(Date date) {
            Log.d(TAG, "date -" + date.toString());
            dateTextView.setText(date.toString());
            onEnterDate(date);
        }
    };

    private Pair<Boolean, SublimeOptions> getDatePickerOptions() {
        SublimeOptions options = new SublimeOptions();
        int displayOptions = 0;

        displayOptions |= SublimeOptions.ACTIVATE_DATE_PICKER;
        options.setPickerToShow(SublimeOptions.Picker.DATE_PICKER);
        options.setDisplayOptions(displayOptions);

        return new Pair<>(displayOptions != 0 ? Boolean.TRUE : Boolean.FALSE, options);
    }

    private void openDatePicker() {
        DatePickerFragment pickerFrag = new DatePickerFragment();
        pickerFrag.setCallback(mFragmentCallback);
        Pair<Boolean, SublimeOptions> optionsPair = getDatePickerOptions();
        Bundle bundle = new Bundle();
        bundle.putParcelable(DatePickerFragment.OPTIONS_EXTRA, optionsPair.second);
        pickerFrag.setArguments(bundle);
        pickerFrag.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        pickerFrag.show(getFragmentManager(), DatePickerFragment.DATE_PICKER_TAG);
    }

    private void onEnterDate(Date date) {
        mRatePresenter.onEnterDateOperation(date);
    }


}
