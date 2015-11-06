package com.lidroid.xutils.db.table;

import android.database.Cursor;

import com.lidroid.xutils.db.sqlite.ColumnDbType;
import com.lidroid.xutils.util.EncryptUtils;
import com.lidroid.xutils.util.LogUtils;

import java.lang.reflect.Field;

import javax.crypto.BadPaddingException;

/**
 * Created by HelloCsl(cslgogogo@gmail.com) on 2015/10/28 .
 */
public class EntryptColumn extends Column {

    EntryptColumn(Class<?> entityType, Field field) {
        super(entityType, field);
    }

    /**
     * 解密并设置到实体
     *
     * @param entity
     * @param cursor
     * @param index
     */
    @Override
    public void setValue2Entity(Object entity, Cursor cursor, int index) {
        if (getColumnDbType() != ColumnDbType.TEXT) {
            throw new IllegalStateException(getColumnDbType() + " is not support for encrypting!");
        }
        this.index = index;
        Object value = columnConverter.getFieldValue(cursor, index);
        if (value == null && defaultValue == null) return;
        try {
            String realValue = EncryptUtils.decryptAESAndroid((String) value, EncryptUtils.AES_PASS);
            if (setMethod != null) {
                setMethod.invoke(entity, realValue == null ? defaultValue : realValue);
            } else {
                this.columnField.setAccessible(true);
                this.columnField.set(entity, realValue == null ? defaultValue : realValue);
            }
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (Throwable e) {
            LogUtils.e(e.getMessage(), e);
        }

    }

    /**
     * 从实体获取字段值，加密后返回用于存储，再次取出需要解密！
     *
     * @param entity
     * @return 加密后的字段值
     */
    @Override
    public Object getColumnValue(Object entity) {
        if (getColumnDbType() != ColumnDbType.TEXT) {
            throw new IllegalStateException(getColumnDbType() + " is not support for encrypting!");
        }
        String columnValue = (String) super.getColumnValue(entity);
        return EncryptUtils.encryptAESAndroid(columnValue, EncryptUtils.AES_PASS);
    }

    //XXX 这个方法还会在外键约束的时候用到，并未验证在使用外键约束的情况下是否有错误！！！不过外键你用个JB加密啊！！！
    @Override
    public Object getFieldValue(Object entity) {
        return super.getFieldValue(entity);
    }
}
