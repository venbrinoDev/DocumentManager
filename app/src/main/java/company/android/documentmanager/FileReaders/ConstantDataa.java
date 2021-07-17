package company.android.documentmanager.FileReaders;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

import java.io.File;

import company.android.documentmanager.R;

public class ConstantDataa {
    public static String ACTION = "";
    public static int adsCounter;

    public static void openPDFDocument(final Context context, String str) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addFlags( Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        Uri filePath = FileProvider.getUriForFile(context,"company.android.documentmanager.Fileprovider",new File(str));
        intent.setDataAndType(filePath, "application/pdf");
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException unused) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle((CharSequence) "No Application Found");
            builder.setMessage((int) R.string.app_not_found);
            builder.setPositiveButton((CharSequence) "Go to Play Store", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent("android.intent.action.VIEW");
                    intent.setData(Uri.parse("market://details?id=com.mobisystems.office"));
                    context.startActivity(intent);
                    dialogInterface.dismiss();
                }
            });
            builder.create().show();
        }
    }

    public static void openPDFDocument(final Context context, String str, Boolean bool) {
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                StrictMode.class.getMethod("disableDeathOnFileUriExposure", new Class[0]).invoke(null, new Object[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags( Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        Uri uriForFile = FileProvider.getUriForFile(context, "com.avp.document.viewer.reader.provider", new File(str));
        if (appInstalledOrNot(context, "com.google.android.apps.pdfviewer")) {
            intent.setDataAndType(uriForFile, "application/pdf");
            intent.setPackage("com.google.android.apps.pdfviewer");
            context.startActivity(intent);
            ((Activity) context).finish();
        } else if (appInstalledOrNot(context, "com.mobisystems.office")) {
            intent.setDataAndType(uriForFile, "application/pdf");
            intent.setPackage("com.mobisystems.office");
            context.startActivity(intent);
            ((Activity) context).finish();
        } else if (appInstalledOrNot(context, "cn.wps.moffice_eng")) {
            intent.setDataAndType(uriForFile, "application/pdf");
            intent.setPackage("cn.wps.moffice_eng");
            context.startActivity(intent);
            ((Activity) context).finish();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle((CharSequence) "No Application Found");
            builder.setMessage((int) R.string.app_not_found);
            builder.setPositiveButton((CharSequence) "Go to Play Store", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent("android.intent.action.VIEW");
                    intent.setData(Uri.parse("market://details?id=" + context.getResources().getString(R.string.generalapp)));
                    context.startActivity(intent);
                    dialogInterface.dismiss();
                }
            });
            builder.create().show();
        }
    }

    public static void openDOCDocument(final Context context, String str) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addFlags( Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        Uri filePath = FileProvider.getUriForFile(context,"company.android.documentmanager.Fileprovider",new File(str));
        intent.setDataAndType(filePath, "application/msword");
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException unused) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle((CharSequence) "No Application Found");
            builder.setMessage((int) R.string.app_not_found);
            builder.setPositiveButton((CharSequence) "Go to Play Store", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent("android.intent.action.VIEW");
                    intent.setData(Uri.parse("market://details?id=com.mobisystems.office"));
                    context.startActivity(intent);
                    dialogInterface.dismiss();
                }
            });
            builder.create().show();
        }
    }

    public static void openDOCDocument(final Context context, String str, Boolean bool) {
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                StrictMode.class.getMethod("disableDeathOnFileUriExposure", new Class[0]).invoke(null, new Object[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags( Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        Uri uriForFile = FileProvider.getUriForFile(context, "com.avp.document.viewer.reader.provider", new File(str));
        if (appInstalledOrNot(context, "com.google.android.apps.docs.editors.docs")) {
            intent.setDataAndType(uriForFile, "application/msword");
            intent.setPackage("com.google.android.apps.docs.editors.docs");
            context.startActivity(intent);
            ((Activity) context).finish();
        } else if (appInstalledOrNot(context, "com.microsoft.office.word")) {
            intent.setDataAndType(uriForFile, "application/msword");
            intent.setPackage("com.microsoft.office.word");
            context.startActivity(intent);
            ((Activity) context).finish();
        } else if (appInstalledOrNot(context, "com.mobisystems.office")) {
            intent.setDataAndType(uriForFile, "application/msword");
            intent.setPackage("com.mobisystems.office");
            context.startActivity(intent);
            ((Activity) context).finish();
        } else if (appInstalledOrNot(context, "cn.wps.moffice_eng")) {
            intent.setDataAndType(uriForFile, "application/msword");
            intent.setPackage("cn.wps.moffice_eng");
            context.startActivity(intent);
            ((Activity) context).finish();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle((CharSequence) "No Application Found");
            builder.setMessage((int) R.string.app_not_found);
            builder.setPositiveButton((CharSequence) "Go to Play Store", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent("android.intent.action.VIEW");
                    intent.setData(Uri.parse("market://details?id=" + context.getResources().getString(R.string.generalapp)));
                    context.startActivity(intent);
                    dialogInterface.dismiss();
                }
            });
            builder.create().show();
        }
    }

    public static void openExcelDocument(final Context context, String str) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addFlags( Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        Uri filePath = FileProvider.getUriForFile(context,"company.android.documentmanager.Fileprovider",new File(str));
        intent.setDataAndType(filePath, "application/vnd.ms-excel");
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException unused) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle((CharSequence) "No Application Found");
            builder.setMessage((int) R.string.app_not_found);
            builder.setPositiveButton((CharSequence) "Go to Play Store", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent("android.intent.action.VIEW");
                    intent.setData(Uri.parse("market://details?id=com.mobisystems.office"));
                    context.startActivity(intent);
                    dialogInterface.dismiss();
                }
            });
            builder.create().show();
        }
    }

    public static void openExcelDocument(final Context context, String str, Boolean bool) {
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                StrictMode.class.getMethod("disableDeathOnFileUriExposure", new Class[0]).invoke(null, new Object[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags( Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        Uri uriForFile = FileProvider.getUriForFile(context, "com.avp.document.viewer.reader.provider", new File(str));
        if (appInstalledOrNot(context, "com.google.android.apps.docs.editors.sheets")) {
            intent.setDataAndType(uriForFile, "application/vnd.ms-excel");
            intent.setPackage("com.google.android.apps.docs.editors.sheets");
            context.startActivity(intent);
            ((Activity) context).finish();
        } else if (appInstalledOrNot(context, "com.microsoft.office.excel")) {
            intent.setDataAndType(uriForFile, "application/vnd.ms-excel");
            intent.setPackage("com.microsoft.office.excel");
            context.startActivity(intent);
            ((Activity) context).finish();
        } else if (appInstalledOrNot(context, "com.mobisystems.office")) {
            intent.setDataAndType(uriForFile, "application/vnd.ms-excel");
            intent.setPackage("com.mobisystems.office");
            context.startActivity(intent);
            ((Activity) context).finish();
        } else if (appInstalledOrNot(context, "cn.wps.moffice_eng")) {
            intent.setDataAndType(uriForFile, "application/vnd.ms-excel");
            intent.setPackage("cn.wps.moffice_eng");
            context.startActivity(intent);
            ((Activity) context).finish();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle((CharSequence) "No Application Found");
            builder.setMessage((int) R.string.app_not_found);
            builder.setPositiveButton((CharSequence) "Go to Play Store", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent("android.intent.action.VIEW");
                    intent.setData(Uri.parse("market://details?id=" + context.getResources().getString(R.string.generalapp)));
                    context.startActivity(intent);
                    dialogInterface.dismiss();
                }
            });
            builder.create().show();
        }
    }

    public static void openPPTDocument(final Context context, String str) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addFlags( Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        Uri filePath = FileProvider.getUriForFile(context,"company.android.documentmanager.Fileprovider",new File(str));
        intent.setDataAndType(filePath, "application/vnd.ms-powerpoint");
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException unused) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle((CharSequence) "No Application Found");
            builder.setMessage((int) R.string.app_not_found);
            builder.setPositiveButton((CharSequence) "Go to Play Store", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent("android.intent.action.VIEW");
                    intent.setData(Uri.parse("market://details?id=com.mobisystems.office"));
                    context.startActivity(intent);
                    dialogInterface.dismiss();
                }
            });
            builder.create().show();
        }
    }

    public static void openPPTDocument(final Context context, String str, Boolean bool) {
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                StrictMode.class.getMethod("disableDeathOnFileUriExposure", new Class[0]).invoke(null, new Object[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags( Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        Uri uriForFile = FileProvider.getUriForFile(context, "com.avp.document.viewer.reader.provider", new File(str));
        if (appInstalledOrNot(context, "com.google.android.apps.docs.editors.slides")) {
            intent.setDataAndType(uriForFile, "application/vnd.ms-powerpoint");
            intent.setPackage("com.google.android.apps.docs.editors.slides");
            context.startActivity(intent);
            ((Activity) context).finish();
        } else if (appInstalledOrNot(context, "com.microsoft.office.powerpoint")) {
            intent.setDataAndType(uriForFile, "application/vnd.ms-powerpoint");
            intent.setPackage("com.microsoft.office.powerpoint");
            context.startActivity(intent);
            ((Activity) context).finish();
        } else if (appInstalledOrNot(context, "com.mobisystems.office")) {
            intent.setDataAndType(uriForFile, "application/vnd.ms-powerpoint");
            intent.setPackage("com.mobisystems.office");
            context.startActivity(intent);
            ((Activity) context).finish();
        } else if (appInstalledOrNot(context, "cn.wps.moffice_eng")) {
            intent.setDataAndType(uriForFile, "application/vnd.ms-powerpoint");
            intent.setPackage("cn.wps.moffice_eng");
            context.startActivity(intent);
            ((Activity) context).finish();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle((CharSequence) "No Application Found");
            builder.setMessage((int) R.string.app_not_found);
            builder.setPositiveButton((CharSequence) "Go to Play Store", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent("android.intent.action.VIEW");
                    intent.setData(Uri.parse("market://details?id=" + context.getResources().getString(R.string.generalapp)));
                    context.startActivity(intent);
                    dialogInterface.dismiss();
                }
            });
            builder.create().show();
        }
    }

    public static void openTextDocument(final Context context, String str) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addFlags( Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        Uri filePath = FileProvider.getUriForFile(context,"company.android.documentmanager.Fileprovider",new File(str));
        intent.setDataAndType(filePath, "text/plain");
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException unused) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle((CharSequence) "No Application Found");
            builder.setMessage((int) R.string.app_not_found);
            builder.setPositiveButton((CharSequence) "Go to Play Store", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent("android.intent.action.VIEW");
                    intent.setData(Uri.parse("market://details?id=com.mobisystems.office"));
                    context.startActivity(intent);
                    dialogInterface.dismiss();
                }
            });
            builder.create().show();
        }
    }

    public static void openTextDocument(final Context context, String str, Boolean bool) {
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                StrictMode.class.getMethod("disableDeathOnFileUriExposure", new Class[0]).invoke(null, new Object[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags( Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        Uri uriForFile = FileProvider.getUriForFile(context, "com.avp.document.viewer.reader.provider", new File(str));
        if (appInstalledOrNot(context, "com.android.chrome")) {
            intent.setDataAndType(uriForFile, "text/plain");
            intent.setPackage("com.android.chrome");
            context.startActivity(intent);
            ((Activity) context).finish();
        } else if (appInstalledOrNot(context, "com.socialnmobile.dictapps.notepad.color.note")) {
            intent.setDataAndType(uriForFile, "text/plain");
            intent.setPackage("com.socialnmobile.dictapps.notepad.color.note");
            context.startActivity(intent);
            ((Activity) context).finish();
        } else if (appInstalledOrNot(context, "com.onto.notepad")) {
            intent.setDataAndType(uriForFile, "text/plain");
            intent.setPackage("com.onto.notepad");
            context.startActivity(intent);
            ((Activity) context).finish();
        } else if (appInstalledOrNot(context, "com.mobisystems.office")) {
            intent.setDataAndType(uriForFile, "text/plain");
            intent.setPackage("com.mobisystems.office");
            context.startActivity(intent);
            ((Activity) context).finish();
        } else if (appInstalledOrNot(context, "cn.wps.moffice_eng")) {
            intent.setDataAndType(uriForFile, "text/plain");
            intent.setPackage("cn.wps.moffice_eng");
            context.startActivity(intent);
            ((Activity) context).finish();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle((CharSequence) "No Application Found");
            builder.setMessage((int) R.string.app_not_found);
            builder.setPositiveButton((CharSequence) "Go to Play Store", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent("android.intent.action.VIEW");
                    intent.setData(Uri.parse("market://details?id=" + context.getResources().getString(R.string.generalapp)));
                    context.startActivity(intent);
                    dialogInterface.dismiss();
                }
            });
            builder.create().show();
        }
    }

    public static boolean appInstalledOrNot(Context context, String str) {
        try {
            context.getPackageManager().getPackageInfo(str, 1);
            return context.getPackageManager().getApplicationInfo(str, 0).enabled;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }
}