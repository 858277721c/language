package com.sd.lib.language;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class LanguageModel
{
    public static final LanguageModel SIMPLIFIED_CHINESE = new LanguageModel(Locale.SIMPLIFIED_CHINESE.toString(), "简体中文");
    public static final LanguageModel TRADITIONAL_CHINESE = new LanguageModel(Locale.TRADITIONAL_CHINESE.toString(), "繁體中文");
    public static final LanguageModel ENGLISH = new LanguageModel(Locale.ENGLISH.toString(), "English");

    private static final String PERSISTENT_KEY = LanguageModel.class.getName();
    private static final Locale HK = new Locale("zh", "HK");

    private final String tag;
    private final String name;

    public LanguageModel(String tag, String name)
    {
        if (TextUtils.isEmpty(tag))
            throw new IllegalArgumentException("language tag is empty");

        if (TextUtils.isEmpty(name))
            throw new IllegalArgumentException("language name is empty");

        this.tag = tag;
        this.name = name;
    }

    public String getTag()
    {
        return tag;
    }

    public String getName()
    {
        return name;
    }

    /**
     * 将Resources修改为当前语言
     *
     * @param resources
     */
    public void apply(Resources resources)
    {
        final Locale locale = toLocale();
        if (locale == null)
            return;

        final Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

    /**
     * 转{@link Locale}
     *
     * @return
     */
    public Locale toLocale()
    {
        if (Locale.SIMPLIFIED_CHINESE.toString().equals(tag))
        {
            return Locale.SIMPLIFIED_CHINESE;
        } else if (Locale.TRADITIONAL_CHINESE.toString().equals(tag))
        {
            return Locale.TRADITIONAL_CHINESE;
        } else if (Locale.ENGLISH.toString().equals(tag))
        {
            return Locale.ENGLISH;
        }
        return null;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof LanguageModel))
            return false;

        final LanguageModel other = (LanguageModel) obj;
        return getTag().equals(other.getTag()) && getName().equals(other.getName());
    }

    /**
     * 返回当前App的语言
     *
     * @return
     */
    public static LanguageModel getCurrent()
    {
        final Context context = LanguageManager.getInstance().getContext();
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        final String json = sharedPreferences.getString(PERSISTENT_KEY, null);

        LanguageModel model = fromJson(json);
        if (model == null)
        {
            model = from(context.getResources().getConfiguration().locale);
            setCurrent(model);
        }

        return model;
    }

    /**
     * 设置当前App的语言
     *
     * @param model
     * @return
     */
    public static boolean setCurrent(LanguageModel model)
    {
        if (model == null)
            return false;

        final String json = model.toJson();
        if (TextUtils.isEmpty(json))
            return false;

        final Context context = LanguageManager.getInstance().getContext();
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.edit().putString(PERSISTENT_KEY, json).commit();
    }

    private static LanguageModel from(Locale locale)
    {
        final String tag = String.valueOf(locale);
        if (Locale.SIMPLIFIED_CHINESE.toString().equals(tag) || Locale.CHINESE.toString().equals(tag))
        {
            return SIMPLIFIED_CHINESE;
        } else if (Locale.TRADITIONAL_CHINESE.toString().equals(tag) || HK.toString().equals(tag))
        {
            return TRADITIONAL_CHINESE;
        } else if (Locale.ENGLISH.toString().equals(tag)
                || Locale.US.toString().equals(tag)
                || Locale.UK.toString().equals(tag)
                || Locale.CANADA.toString().equals(tag))
        {
            return ENGLISH;
        } else
        {
            return SIMPLIFIED_CHINESE;
        }
    }

    private String toJson()
    {
        try
        {
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("tag", tag);
            jsonObject.put("name", name);
            return jsonObject.toString();
        } catch (JSONException e)
        {
            return null;
        }
    }

    private static LanguageModel fromJson(String json)
    {
        try
        {
            final JSONObject jsonObject = new JSONObject(json);
            final String tag = jsonObject.getString("tag");
            final String name = jsonObject.getString("name");
            return new LanguageModel(tag, name);
        } catch (Exception e)
        {
            return null;
        }
    }
}
