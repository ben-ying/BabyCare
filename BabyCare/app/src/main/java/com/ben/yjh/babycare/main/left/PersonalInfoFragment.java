package com.ben.yjh.babycare.main.left;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.base.BaseFragment;
import com.ben.yjh.babycare.http.HttpResponseInterface;
import com.ben.yjh.babycare.http.UserTaskHandler;
import com.ben.yjh.babycare.model.Event;
import com.ben.yjh.babycare.model.HttpBaseResult;
import com.ben.yjh.babycare.model.User;
import com.ben.yjh.babycare.model.UserHistory;
import com.ben.yjh.babycare.util.Constants;
import com.ben.yjh.babycare.widget.ItemDetail;
import com.ben.yjh.babycare.widget.ItemInfo;

import java.util.Calendar;
import java.util.List;


public class PersonalInfoFragment extends BaseFragment {

    private ItemInfo mUsernameItem;
    private ItemInfo mBabyNameItem;
    private ItemInfo mEmailItem;
    private ItemInfo mPhoneItem;
    private ItemInfo mGenderItem;
    private ItemInfo mBirthItem;
    private ItemInfo mHobbiesItem;

    public static PersonalInfoFragment newInstance() {
        Bundle args = new Bundle();
        PersonalInfoFragment fragment = new PersonalInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_info, container, false);
        mUsernameItem = (ItemInfo) view.findViewById(R.id.item_username);
        mBabyNameItem = (ItemInfo) view.findViewById(R.id.item_baby_name);
        mEmailItem = (ItemInfo) view.findViewById(R.id.item_email);
        mPhoneItem = (ItemInfo) view.findViewById(R.id.item_phone);
        mGenderItem = (ItemInfo) view.findViewById(R.id.item_gender);
        mBirthItem = (ItemInfo) view.findViewById(R.id.item_birth);
        mHobbiesItem = (ItemInfo) view.findViewById(R.id.item_hobbies);
        mBabyNameItem.setOnClickListener(this);
        mEmailItem.setOnClickListener(this);
        mPhoneItem.setOnClickListener(this);
        mGenderItem.setOnClickListener(this);
        mBirthItem.setOnClickListener(this);
        mHobbiesItem.setOnClickListener(this);

        return view;
    }

    @Override
    public void init() {
        setData();
        getUserDetail();
    }

    private void setData() {
        user = activity.user;
        mUsernameItem.setValue(R.string.username, user.getUsername(), 0);
        mBabyNameItem.setValue(R.string.baby_name, user.getBabyName(), R.string.edit_baby_name);
        mEmailItem.setValue(R.string.email, user.getEmail(), R.string.edit_email);
        mPhoneItem.setValue(R.string.phone, user.getPhone(), R.string.edit_phone);
        mGenderItem.setValue(R.string.gender, user.getGenderValue(activity), R.string.male);
        mBirthItem.setValue(R.string.birth, user.getBirth(), R.string.edit_birth);
        mHobbiesItem.setValue(R.string.hobbies, user.getHobbies(), R.string.edit_hobbies);
        mBabyNameItem.setValue(R.string.baby_name, user.getBabyName(), R.string.edit_baby_name);
        mEmailItem.setValue(R.string.email, user.getEmail(), R.string.edit_email);
        mPhoneItem.setValue(R.string.phone, user.getPhone(), R.string.edit_phone);
        mGenderItem.setValue(R.string.gender, user.getGenderValue(activity), R.string.male);
        mBirthItem.setValue(R.string.birth, user.getBirth(), R.string.edit_birth);
        mHobbiesItem.setValue(R.string.hobbies, user.getHobbies(), R.string.edit_hobbies);
    }

    private void getUserDetail() {
        new UserTaskHandler(activity).getUserDetail(user.getToken(), user.getUserId(),
                new HttpResponseInterface<User>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(User classOfT) {
                        user = classOfT;
                        setData();
                    }

                    @Override
                    public void onFailure(HttpBaseResult result) {

                    }

                    @Override
                    public void onHttpError(String error) {

                    }
                });
    }

    protected void editPersonalInfoTask(final int id, final String value) {
        String key = null;
        switch (id) {
            case R.id.item_baby_name:
                key = Constants.BABY_NAME;
                break;
            case R.id.item_email:
                key = Constants.EMAIL;
                break;
            case R.id.item_phone:
                key = Constants.PHONE;
                break;
            case R.id.item_gender:
                key = Constants.GENDER;
                break;
            case R.id.item_birth:
                key = Constants.BIRTHDAY;
                break;
            case R.id.item_hobbies:
                key = Constants.HOBBIES;
                break;
            case R.id.img_profile:
                key = Constants.BASE64;
                break;
        }

        if (key != null) {
            new UserTaskHandler(activity).editUserInfo(user.getUserId(), key, value, user.getToken(),
                    new HttpResponseInterface<User>() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onSuccess(User classOfT) {
                            activity.user = classOfT;
                            activity.user.save();
                            UserHistory.saveUserHistory(classOfT);
                            setData();
                            if (id == R.id.item_baby_name || id == R.id.img_profile) {
                                List<Event> eventList = Event.find(
                                        Event.class, "user_id = ?", String.valueOf(user.getUserId()));
                                for (Event event : eventList) {
                                    event.setUsername(user.getBabyName());
                                    event.setUserProfile(user.getProfile());
                                    event.save();
                                }
                            }
                        }

                        @Override
                        public void onFailure(HttpBaseResult result) {

                        }

                        @Override
                        public void onHttpError(String error) {

                        }
                    });
        }
    }


    @Override
    public void onClick(final View v) {
        final int id = v.getId();

        switch (id) {
            case R.id.item_baby_name:
            case R.id.item_email:
            case R.id.item_phone:
            case R.id.item_hobbies:
                ItemInfo itemInfo = (ItemInfo) v;
                final View view = LayoutInflater.from(activity).inflate(R.layout.dialog_edit_text, null);
                final EditText editText = (EditText) view.findViewById(R.id.et_value);
                editText.setText(itemInfo.getValue());
                if (id == R.id.item_email) {
                    editText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                } else if (id == R.id.item_phone) {
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
                editText.setSelection(editText.getText().length());
                final AlertDialog dialog = new AlertDialog.Builder(activity, R.style.MyDialogTheme)
                        .setTitle(itemInfo.getTitle())
                        .setView(view)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton(R.string.cancel, null)
                        .create();
                dialog.setCancelable(true);
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String value = editText.getText().toString().trim();
                        if (id == R.id.item_email || id == R.id.item_baby_name) {
                            if (value.isEmpty()) {
                                editText.setError(getString(R.string.empty_email));
                            } else if (id == R.id.item_email &&
                                    !Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
                                editText.setError(getString(R.string.invalid_email));
                            } else {
                                dialog.dismiss();
                                editPersonalInfoTask(id, value);
                            }
                        } else {
                            dialog.dismiss();
                            editPersonalInfoTask(id, value);
                        }
                    }
                });
                break;
            case R.id.item_gender:
                String[] array = getResources().getStringArray(R.array.gender_choices);
                new AlertDialog.Builder(activity)
                        .setTitle(R.string.gender)
                        .setItems(array, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = null;
                                switch (which) {
                                    case 0:
                                        editPersonalInfoTask(id, "0");
                                        break;
                                    case 1:
                                        editPersonalInfoTask(id, "1");
                                        break;
                                    default:
                                        break;
                                }
                            }
                        })
                        .setNegativeButton(R.string.cancel, null)
                        .show();
                break;
            case R.id.item_birth:
                Calendar c = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(activity,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                editPersonalInfoTask(id, year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }
                        , c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
                        .get(Calendar.DAY_OF_MONTH));
                c.add(Calendar.YEAR, -30);
                datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
                c.add(Calendar.YEAR, 31);
                datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
                datePickerDialog.show();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constants.SHOW_EVENT_IMAGE_DETAIL_REQUEST_CODE:
                    break;
                case Constants.COMMENT_REQUEST_CODE:
                    break;
            }
        }
    }

    @Override
    public String getTitle(Context context) {
        return context.getResources().getString(R.string.basic_info);
    }
}
