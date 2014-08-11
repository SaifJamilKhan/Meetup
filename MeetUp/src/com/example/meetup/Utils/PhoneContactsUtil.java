package com.example.meetup.Utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.ArrayList;

import meetup_objects.MeetUpUser;

public class PhoneContactsUtil {

    public static ArrayList<MeetUpUser> getContacts(Context context) {
        ArrayList<MeetUpUser> users = new ArrayList<MeetUpUser>();
        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (Integer.parseInt(cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    ArrayList phoneNumbers = new ArrayList();
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
                        phoneNo = PhoneNumberUtils.stripSeparators(phoneNo);
                        Phonenumber.PhoneNumber phoneNumber = new Phonenumber.PhoneNumber();
                        phoneNumber.setRawInput(phoneNo);
                        try {
                            phoneNumber = phoneUtil.parse(phoneNo, "CA");
                        } catch (NumberParseException e) {
                            e.printStackTrace();
                        }

                        phoneNumbers.add(String.valueOf(phoneNumber.getNationalNumber()));
                    }
                    pCur.close();
                    if(phoneNumbers.size() > 0) {
                        users.add(new MeetUpUser(name, (String) phoneNumbers.get(0)));
                    }
                }
            }
        }
        return users;
    }
}
