package com.dec04.geoshareapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

class PermissionUtils {

    private Context context;
    private String TAG = "DEV";

    // Код ответа для запроса разрешений
    private int PERMISSION_REQUEST_CODE = 1;

    // Флаг сообщает о том, что после запроса разрешений - запрос отклонили\
    boolean flagPermissionDenied = false;

    // Обьяснение, зачем нужны разрешения
//    private String explanation;

    // Конструкторы
    PermissionUtils(Context context) {
        this.context = context;
//        this.explanation = explanation;
    }

    // Проверяем, есть ли у пользователя необзодимые разрешения
    boolean check(final String[] permissions) {
        int statement = 0;

        for (String permission : permissions) {
            statement += ContextCompat.checkSelfPermission(context, permission);
        }

        // Access to the location has been granted to the app.
        return statement == PackageManager.PERMISSION_GRANTED;
    }

    // Запрашиваем разрешение и если необходимо - показываем обьяснение
    void requestPermission(final String[] permissions) {
//        this.explanation = context.getResources().getString(R.string.request_permission_explanation);

        // Permission is missing.
        // Permission is not granted
        // Should we show an explanation?

        for (final String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                    permission)) {

                // Находим ресурс по имени
                String[] parts = (permission + "_EXPLANATION").split("\\.");
                String stringResourcesName = parts[parts.length - 1];
                int resourceId = context.getResources().getIdentifier(stringResourcesName, "string", context.getPackageName());
                String message = context.getString(resourceId);

                int buttonStringResourceId = permission.equals(permissions[0]) ? R.string.get : R.string.next ;

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(message)
                        .setPositiveButton(buttonStringResourceId, new DialogInterface.OnClickListener() {
                            boolean firstItem = permission.equals(permissions[0]);

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (firstItem) {
                                    ActivityCompat.requestPermissions((Activity) context,
                                        permissions,
                                        PERMISSION_REQUEST_CODE);
                                }
                            }
                        })
                        .create().show();
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions((Activity) context,
                        permissions,
                        PERMISSION_REQUEST_CODE);

                // LOCATION_PERMISSION_REQUEST_CODE is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    /**
     * Показываем диалог, обьясняющий что разрешения не получены.
     */
    void showMissingPermissionError(final String[] permissions) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.missing_permission_explanation)
                .setNeutralButton(R.string.get_again, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                permissions,
                                PERMISSION_REQUEST_CODE);
                    }
                })
                .setPositiveButton(R.string.got_it, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create().show();
    }
}
