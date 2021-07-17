package company.android.documentmanager.FileReaders;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

import company.android.documentmanager.R;

public class FileExplodataa extends Fragment implements OnClickListener {
    private String PATH = "";
    private String TAG = "DM_FragmentFileExplore";
    private ArrayList<GSHeaderDataset> arrayHeader;
    private ArrayList<ExpolreModdelaa> arraylistFile;
    private DataAdapter dataAdapter;
    private HeaderDataAdapter headerDataAdapter;
    private RecyclerView recyclerview;
    private RecyclerView recyclerviewHeader;
    RelativeLayout relnofiles;

    public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

        public ArrayList<ExpolreModdelaa> data;

        public class ViewHolder extends RecyclerView.ViewHolder {

            public ImageView imgFileThumb;

            public LinearLayout llClickable;

            public TextView txtFileName;

            public TextView txtFileSize;

            public ViewHolder(View view) {
                super(view);
                this.txtFileName = (TextView) view.findViewById(R.id.txtFileName);
                this.imgFileThumb = (ImageView) view.findViewById(R.id.imgFileThumb);
                this.txtFileSize = (TextView) view.findViewById(R.id.txtFileSize);
                this.llClickable = (LinearLayout) view.findViewById(R.id.llClickable);
            }
        }

        public DataAdapter(ArrayList<ExpolreModdelaa> arrayList) {
            this.data = arrayList;
        }

        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_zip_rar_file_explore_fragment, viewGroup, false));
        }

        public void onBindViewHolder(ViewHolder viewHolder, final int i) {
            ExpolreModdelaa dM_ModelExploreFile = (ExpolreModdelaa) this.data.get(i);
            if (dM_ModelExploreFile.getFileName().length() > 25) {
                viewHolder.txtFileName.setText(FileExplodataa.this.fileNameShortForm(dM_ModelExploreFile.getFileName()));
            } else {
                viewHolder.txtFileName.setText(dM_ModelExploreFile.getFileName());
            }
            if (dM_ModelExploreFile.getExtension().equals("")) {
                viewHolder.txtFileSize.setText("Directory");
            } else {
                TextView access$200 = viewHolder.txtFileSize;
                StringBuilder sb = new StringBuilder();
                sb.append("Size : ");
                sb.append(FileExplodataa.this.readableFileSize(dM_ModelExploreFile.getSize()));
                access$200.setText(sb.toString());
            }
            viewHolder.llClickable.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    final ExpolreModdelaa dM_ModelExploreFile = (ExpolreModdelaa) DataAdapter.this.data.get(i);
                    String lowerCase = dM_ModelExploreFile.getExtension().toLowerCase();
                    String str = "";
                    if (lowerCase.equals(str)) {
                        FragmentTransaction beginTransaction = FileExplodataa.this.getActivity().getSupportFragmentManager().beginTransaction();
                        FileExplodataa dM_FragmentFileExplore = new FileExplodataa();
                        Bundle bundle = new Bundle();
                        zipviewActttt.childParentDirectory.add(dM_ModelExploreFile.getPath());
                        bundle.putString("PATH", dM_ModelExploreFile.getPath());
                        dM_FragmentFileExplore.setArguments(bundle);
                        beginTransaction.replace(R.id.fragment, dM_FragmentFileExplore);
                        beginTransaction.commit();
                        return;
                    }
                    String str2 = "disableDeathOnFileUriExposure";
                    String str3 = "android.intent.action.VIEW";
                    if (lowerCase.equals("jpg") || lowerCase.equals("JPG") || lowerCase.equals("png") || lowerCase.equals("PNG") || lowerCase.equals("ANI") || lowerCase.equals("ani") || lowerCase.equals("BMP") || lowerCase.equals("bmp") || lowerCase.equals("CAL") || lowerCase.equals("cal") || lowerCase.equals("FAX") || lowerCase.equals("fax") || lowerCase.equals("GIF") || lowerCase.equals("gif") || lowerCase.equals("IMG") || lowerCase.equals("img") || lowerCase.equals("JBG") || lowerCase.equals("jbg") || lowerCase.equals("JPE") || lowerCase.equals("jpe") || lowerCase.equals("JPEG") || lowerCase.equals("jpeg") || lowerCase.equals("MAC") || lowerCase.equals("mac") || lowerCase.equals("PBM") || lowerCase.equals("pbm") || lowerCase.equals("PCD") || lowerCase.equals("pcd") || lowerCase.equals("PCX") || lowerCase.equals("pcx") || lowerCase.equals("PCT") || lowerCase.equals("pct") || lowerCase.equals("PGM") || lowerCase.equals("pgm") || lowerCase.equals("PPM") || lowerCase.equals("ppm") || lowerCase.equals("PSD") || lowerCase.equals("psd") || lowerCase.equals("RAS") || lowerCase.equals("ras") || lowerCase.equals("TGA") || lowerCase.equals("tga") || lowerCase.equals("TIFF") || lowerCase.equals("tiff") || lowerCase.equals("WMF") || lowerCase.equals("wmf")) {
                        if (VERSION.SDK_INT >= 24) {
                            try {
                                StrictMode.class.getMethod(str2, new Class[0]).invoke(null, new Object[0]);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        Intent intent = new Intent();
                        intent.setAction(str3);
                        intent.setDataAndType(Uri.fromFile(new File(dM_ModelExploreFile.getPath())), "image/*");
                        FileExplodataa.this.startActivity(intent);
                        return;
                    }
                    String str4 = "a";
                    String str5 = "filepath";
                    String str6 = "filename";
                    if (lowerCase.endsWith("doc") || lowerCase.endsWith("DOC") || lowerCase.endsWith("docx") || lowerCase.endsWith("DOCX")) {
                        Intent intent2 = new Intent(FileExplodataa.this.getActivity(), WordViewAndroid.class);
                        intent2.putExtra(str6, dM_ModelExploreFile.getFileName());
                        intent2.putExtra(str5, dM_ModelExploreFile.getPath());
                        intent2.setAction(str4);
                        FileExplodataa.this.startActivity(intent2);
                    } else if (lowerCase.endsWith("pdf") || lowerCase.endsWith("PDF")) {
                        Intent intent3 = new Intent(FileExplodataa.this.getActivity(), PdfViewActtt.class);
                        intent3.putExtra(str6, dM_ModelExploreFile.getFileName());
                        intent3.putExtra(str5, dM_ModelExploreFile.getPath());
                        intent3.setAction(str4);
                        FileExplodataa.this.startActivity(intent3);
                    } else if (lowerCase.endsWith("ppt") || lowerCase.endsWith("PPT") || lowerCase.endsWith("pptx") || lowerCase.endsWith("PPTX")) {
                        Intent intent4 = new Intent(FileExplodataa.this.getActivity(), PPTViewData.class);
                        intent4.putExtra(str6, dM_ModelExploreFile.getFileName());
                        intent4.putExtra(str5, dM_ModelExploreFile.getPath());
                        intent4.setAction(str4);
                        FileExplodataa.this.startActivity(intent4);
                    } else if (lowerCase.endsWith("txt") || lowerCase.endsWith("TXT") || lowerCase.endsWith("JAVA") || lowerCase.endsWith("java") || lowerCase.endsWith("xml") || lowerCase.endsWith("XML")) {
                        Intent intent5 = new Intent(FileExplodataa.this.getActivity(), Texttt.class);
                        intent5.putExtra(str6, dM_ModelExploreFile.getFileName());
                        intent5.putExtra(str5, dM_ModelExploreFile.getPath());
                        intent5.setAction(str4);
                        FileExplodataa.this.startActivity(intent5);
                    } else if (lowerCase.endsWith("xls") || lowerCase.endsWith("XLS") || lowerCase.endsWith("xlsx") || lowerCase.endsWith("XLSX")) {
                        Intent intent6 = new Intent(FileExplodataa.this.getActivity(), ExcelActivity.class);
                        intent6.putExtra(str6, dM_ModelExploreFile.getFileName());
                        intent6.putExtra(str5, dM_ModelExploreFile.getPath());
                        intent6.putExtra("authority", str);
                        intent6.putExtra("filetype", str);
                        intent6.setAction(str4);
                        FileExplodataa.this.startActivity(intent6);
                    } else if (lowerCase.equals("3GP") || lowerCase.equals("3gp") || lowerCase.equals("MP4") || lowerCase.equals("mp4") || lowerCase.equals("ts") || lowerCase.equals("TS") || lowerCase.equals("webm") || lowerCase.equals("WEBM") || lowerCase.equals("mkv") || lowerCase.equals("MKV")) {
                        Intent intent7 = new Intent(str3, Uri.parse(dM_ModelExploreFile.getPath()));
                        intent7.setDataAndType(Uri.parse(dM_ModelExploreFile.getPath()), "video/*");
                        FileExplodataa.this.startActivity(intent7);
                    } else {
                        if (!lowerCase.equals("PCM") && !lowerCase.equals("pcm") && !lowerCase.equals("WAV") && !lowerCase.equals("wav") && !lowerCase.equals("AIFF") && !lowerCase.equals("aiff") && !lowerCase.equals("mp3") && !lowerCase.equals("MP3")) {
                            String str7 = "ARM";
                            if (!lowerCase.equals(str7)) {
                                String str8 = "arm";
                                if (!lowerCase.equals(str8) && !lowerCase.equals(str7) && !lowerCase.equals(str8) && !lowerCase.equals(".AMR") && !lowerCase.equals("amr") && !lowerCase.equals("AAC") && !lowerCase.equals("aac") && !lowerCase.equals("OGC") && !lowerCase.equals("ogc")) {
                                    String str9 = "WMA";
                                    if (!lowerCase.equals(str9)) {
                                        String str10 = "wma";
                                        if (!lowerCase.equals(str10) && !lowerCase.equals("FLAC") && !lowerCase.equals("flac") && !lowerCase.equals("ALAC") && !lowerCase.equals("alac") && !lowerCase.equals(str9) && !lowerCase.equals(str10) && !lowerCase.equals("M4A") && !lowerCase.equals("m4a")) {
                                            if (lowerCase.equals("apk")) {
                                                if (VERSION.SDK_INT >= 24) {
                                                    try {
                                                        StrictMode.class.getMethod(str2, new Class[0]).invoke(null, new Object[0]);
                                                    } catch (Exception e2) {
                                                        e2.printStackTrace();
                                                    }
                                                }
                                                Intent intent8 = new Intent();
                                                intent8.setAction(str3);
                                                intent8.setDataAndType(Uri.fromFile(new File(dM_ModelExploreFile.getPath())), "application/vnd.android.package-archive");
                                                FileExplodataa.this.startActivity(intent8);
                                                return;
                                            } else if (lowerCase.equals("ZIP") || lowerCase.equals("zip") || lowerCase.equals("RAR") || lowerCase.equals("rar")) {
                                                if (VERSION.SDK_INT >= 24) {
                                                    try {
                                                        StrictMode.class.getMethod(str2, new Class[0]).invoke(null, new Object[0]);
                                                    } catch (Exception e3) {
                                                        e3.printStackTrace();
                                                    }
                                                }
                                                try {
                                                    File file = new File(dM_ModelExploreFile.getPath());
                                                    Intent intent9 = new Intent(str3);
                                                    intent9.setDataAndType(Uri.fromFile(file), "application/zip");
                                                    FileExplodataa.this.startActivity(intent9);
                                                    return;
                                                } catch (ActivityNotFoundException unused) {
                                                    try {
                                                        FileExplodataa.this.startActivity(new Intent(str3, Uri.parse("market://search?q=application/zip")));
                                                        return;
                                                    } catch (ActivityNotFoundException unused2) {
                                                        Toast.makeText(FileExplodataa.this.getActivity(), "You don't have Google Play installed", Toast.LENGTH_LONG).show();
                                                        return;
                                                    }
                                                }
                                            } else {
                                                Builder builder = new Builder(FileExplodataa.this.getActivity());
                                                View inflate = FileExplodataa.this.getLayoutInflater().inflate(R.layout.alert_item_zip_rar, null);
                                                builder.setView(inflate);
                                                if (VERSION.SDK_INT >= 24) {
                                                    try {
                                                        StrictMode.class.getMethod(str2, new Class[0]).invoke(null, new Object[0]);
                                                    } catch (Exception e4) {
                                                        e4.printStackTrace();
                                                    }
                                                }
                                                LinearLayout linearLayout = (LinearLayout) inflate.findViewById(R.id.LLtext);
                                                LinearLayout linearLayout2 = (LinearLayout) inflate.findViewById(R.id.LLaudio);
                                                LinearLayout linearLayout3 = (LinearLayout) inflate.findViewById(R.id.LLvideo);
                                                LinearLayout linearLayout4 = (LinearLayout) inflate.findViewById(R.id.LLimage);
                                                LinearLayout linearLayout5 = (LinearLayout) inflate.findViewById(R.id.LLother);
                                                final AlertDialog create = builder.create();
                                                linearLayout.setOnClickListener(new OnClickListener() {
                                                    public void onClick(View view) {
                                                        Intent intent = new Intent();
                                                        intent.setAction("android.intent.action.VIEW");
                                                        intent.setDataAndType(Uri.fromFile(new File(dM_ModelExploreFile.getPath())), "text/plain");
                                                        FileExplodataa.this.startActivity(intent);
                                                        create.dismiss();
                                                    }
                                                });
                                                linearLayout2.setOnClickListener(new OnClickListener() {
                                                    public void onClick(View view) {
                                                        Intent intent = new Intent();
                                                        intent.setAction("android.intent.action.VIEW");
                                                        intent.setDataAndType(Uri.fromFile(new File(dM_ModelExploreFile.getPath())), "audio/*");
                                                        FileExplodataa.this.startActivity(intent);
                                                        create.dismiss();
                                                    }
                                                });
                                                linearLayout3.setOnClickListener(new OnClickListener() {
                                                    public void onClick(View view) {
                                                        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(dM_ModelExploreFile.getPath()));
                                                        intent.setDataAndType(Uri.parse(dM_ModelExploreFile.getPath()), "video/*");
                                                        FileExplodataa.this.startActivity(intent);
                                                        create.dismiss();
                                                    }
                                                });
                                                linearLayout4.setOnClickListener(new OnClickListener() {
                                                    public void onClick(View view) {
                                                        Intent intent = new Intent();
                                                        intent.setAction("android.intent.action.VIEW");
                                                        intent.setDataAndType(Uri.fromFile(new File(dM_ModelExploreFile.getPath())), "image/*");
                                                        FileExplodataa.this.startActivity(intent);
                                                        create.dismiss();
                                                    }
                                                });
                                                linearLayout5.setOnClickListener(new OnClickListener() {
                                                    public void onClick(View view) {
                                                        Intent intent = new Intent();
                                                        intent.setAction("android.intent.action.VIEW");
                                                        intent.setDataAndType(Uri.fromFile(new File(dM_ModelExploreFile.getPath())), "*/*");
                                                        FileExplodataa.this.startActivity(intent);
                                                        create.dismiss();
                                                    }
                                                });
                                                create.show();
                                                return;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (VERSION.SDK_INT >= 24) {
                            try {
                                StrictMode.class.getMethod(str2, new Class[0]).invoke(null, new Object[0]);
                            } catch (Exception e5) {
                                e5.printStackTrace();
                            }
                        }
                        Intent intent10 = new Intent();
                        intent10.setAction(str3);
                        intent10.setDataAndType(Uri.fromFile(new File(dM_ModelExploreFile.getPath())), "audio/*");
                        FileExplodataa.this.startActivity(intent10);
                    }
                }
            });
            FileExplodataa.this.setImage(viewHolder.imgFileThumb, dM_ModelExploreFile.getExtension());
        }

        public int getItemCount() {
            return this.data.size();
        }
    }

    private class GSHeaderDataset {
        String HeaderName;
        String HeaderPath;

        private GSHeaderDataset() {
        }

        public String getHeaderName() {
            return this.HeaderName;
        }

        public void setHeaderName(String str) {
            this.HeaderName = str;
        }

        public String getHeaderPath() {
            return this.HeaderPath;
        }

        public void setHeaderPath(String str) {
            this.HeaderPath = str;
        }
    }

    public class HeaderDataAdapter extends RecyclerView.Adapter<HeaderDataAdapter.ViewHolder> {
        private ArrayList<GSHeaderDataset> data;

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView headerName;

            public LinearLayout llClickable;

            public ViewHolder(View view) {
                super(view);
                this.headerName = (TextView) view.findViewById(R.id.headerName);
                this.llClickable = (LinearLayout) view.findViewById(R.id.llClickable);
            }
        }

        public HeaderDataAdapter(ArrayList<GSHeaderDataset> arrayList) {
            this.data = arrayList;
        }

        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_zip_rar_file_explore_header, viewGroup, false));
        }

        public void onBindViewHolder(ViewHolder viewHolder, final int i) {
            viewHolder.headerName.setText(((GSHeaderDataset) this.data.get(i)).getHeaderName());
            viewHolder.llClickable.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    int size = zipviewActttt.childParentDirectory.size();
                    while (true) {
                        size--;
                        if (i < size) {
                            zipviewActttt.childParentDirectory.remove(size);
                        } else {
                            FragmentTransaction beginTransaction = FileExplodataa.this.getActivity().getSupportFragmentManager().beginTransaction();
                            FileExplodataa dM_FragmentFileExplore = new FileExplodataa();
                            Bundle bundle = new Bundle();
                            bundle.putString("PATH", (String) zipviewActttt.childParentDirectory.get(zipviewActttt.childParentDirectory.size() - 1));
                            dM_FragmentFileExplore.setArguments(bundle);
                            beginTransaction.replace(R.id.fragment, dM_FragmentFileExplore);
                            beginTransaction.commit();
                            return;
                        }
                    }
                }
            });
        }

        public int getItemCount() {
            return this.data.size();
        }
    }

    public void onClick(View view) {
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.zip_rar_file_explore_fragment, viewGroup, false);
        setHasOptionsMenu(true);
        String str = "";
        this.PATH = getArguments().getString("PATH", str);
        String str2 = this.PATH;
        if (str2 == null || str2 == str) {
            Toast.makeText(getActivity(), "Error", 0).show();
            return inflate;
        }
        getActivity().setTitle(new File(str2).getName());
        inflate.setFocusableInTouchMode(true);
        inflate.requestFocus();
        inflate.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() != 0 || i != 4) {
                    return false;
                }
                if (keyEvent.getKeyCode() == 4) {
                    FileExplodataa.this.getActivity().onBackPressed();
                }
                return true;
            }
        });
        getFiles();
        getHeaderFile();
        this.recyclerview = (RecyclerView) inflate.findViewById(R.id.recyclerview);
        this.recyclerviewHeader = (RecyclerView) inflate.findViewById(R.id.recyclerviewHeader);
        this.dataAdapter = new DataAdapter(this.arraylistFile);
        this.headerDataAdapter = new HeaderDataAdapter(this.arrayHeader);
        this.relnofiles = (RelativeLayout) inflate.findViewById(R.id.relnofiles);
        if (this.arraylistFile.isEmpty()) {
            this.relnofiles.setVisibility(0);
        } else {
            this.relnofiles.setVisibility(8);
        }
        this.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.recyclerview.setAdapter(this.dataAdapter);
        this.recyclerview.setItemAnimator(new DefaultItemAnimator());
        this.recyclerviewHeader.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        this.recyclerviewHeader.setAdapter(this.headerDataAdapter);
        this.recyclerviewHeader.scrollToPosition(zipviewActttt.childParentDirectory.size() - 1);
        return inflate;
    }

    private void getHeaderFile() {
        this.arrayHeader = new ArrayList<>();
        if (zipviewActttt.childParentDirectory.size() > 0) {
            for (int i = 0; i < zipviewActttt.childParentDirectory.size(); i++) {
                GSHeaderDataset gSHeaderDataset = new GSHeaderDataset();
                File file = new File((String) zipviewActttt.childParentDirectory.get(i));
                gSHeaderDataset.setHeaderName(file.getName());
                gSHeaderDataset.setHeaderPath(file.getAbsolutePath());
                this.arrayHeader.add(gSHeaderDataset);
            }
        }
    }


    public void getFiles() {
        this.arraylistFile = new ArrayList<>();
        File[] listFiles = new File(this.PATH).listFiles();
        if (listFiles != null) {
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < listFiles.length; i++) {
                ExpolreModdelaa dM_ModelExploreFile = new ExpolreModdelaa(listFiles[i]);
                if (listFiles[i].isFile()) {
                    arrayList.add(dM_ModelExploreFile);
                } else {
                    this.arraylistFile.add(dM_ModelExploreFile);
                }
            }
            this.arraylistFile.addAll(arrayList);
        }
    }

    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() != 1 || i != 4) {
                    return false;
                }
                if (zipviewActttt.childParentDirectory.size() > 1) {
                    FragmentTransaction beginTransaction = FileExplodataa.this.getActivity().getSupportFragmentManager().beginTransaction();
                    FileExplodataa dM_FragmentFileExplore = new FileExplodataa();
                    Bundle bundle = new Bundle();
                    zipviewActttt.childParentDirectory.remove(zipviewActttt.childParentDirectory.size() - 1);
                    bundle.putString("PATH", (String) zipviewActttt.childParentDirectory.get(zipviewActttt.childParentDirectory.size() - 1));
                    dM_FragmentFileExplore.setArguments(bundle);
                    beginTransaction.replace(R.id.fragment, dM_FragmentFileExplore);
                    beginTransaction.commit();
                } else {
                    FragmentActivity activity = FileExplodataa.this.getActivity();
                    StringBuilder sb = new StringBuilder();
                    sb.append(Environment.getExternalStorageDirectory());
                    sb.append("/Android/data/");
                    sb.append(FileExplodataa.this.getActivity().getPackageName());
                    sb.append("/.temp");
                    FilOperationns.deleteTemp(activity, sb.toString());
                    FileExplodataa.this.getActivity().finish();
                }
                return true;
            }
        });
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return false;
        }
        if (zipviewActttt.childParentDirectory.size() > 1) {
            FragmentTransaction beginTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            FileExplodataa dM_FragmentFileExplore = new FileExplodataa();
            Bundle bundle = new Bundle();
            zipviewActttt.childParentDirectory.remove(zipviewActttt.childParentDirectory.size() - 1);
            bundle.putString("PATH", (String) zipviewActttt.childParentDirectory.get(zipviewActttt.childParentDirectory.size() - 1));
            dM_FragmentFileExplore.setArguments(bundle);
            beginTransaction.replace(R.id.fragment, dM_FragmentFileExplore);
            beginTransaction.commit();
        } else {
            getActivity().finish();
        }
        return true;
    }

    public String readableFileSize(long j) {
        if (j <= 0) {
            return "0";
        }
        String[] strArr = {"B", "KB", "MB", "GB", "TB"};
        double d = (double) j;
        int log10 = (int) (Math.log10(d) / Math.log10(1024.0d));
        StringBuilder sb = new StringBuilder();
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.#");
        double pow = Math.pow(1024.0d, (double) log10);
        Double.isNaN(d);
        sb.append(decimalFormat.format(d / pow));
        sb.append(" ");
        sb.append(strArr[log10]);
        return sb.toString();
    }


    public void setImage(ImageView imageView, String str) {
        char c;
        String lowerCase = str.toLowerCase();
        switch (lowerCase.hashCode()) {
            case 0:
                if (lowerCase.equals("")) {
                    c = '5';
                    break;
                }
            case 3711:
                if (lowerCase.equals("ts")) {
                    c = '1';
                    break;
                }
            case 52316:
                if (lowerCase.equals("3gp")) {
                    c = '/';
                    break;
                }
            case 96323:
                if (lowerCase.equals("aac")) {
                    c = ')';
                    break;
                }
            case 96710:
                if (lowerCase.equals("amr")) {
                    c = '(';
                    break;
                }
            case 96732:
                if (lowerCase.equals("ani")) {
                    c = '\u000e';
                    break;
                }
            case 96796:
                if (lowerCase.equals("apk")) {
                    c = '4';
                    break;
                }
            case 96860:
                if (lowerCase.equals("arm")) {
                    c = '\'';
                    break;
                }
            case 97669:
                if (lowerCase.equals("bmp")) {
                    c = 15;
                    break;
                }
            case 98254:
                if (lowerCase.equals("cal")) {
                    c = 16;
                    break;
                }
            case 98822:
                if (lowerCase.equals("csv")) {
                    c = 8;
                    break;
                }
            case 99640:
                if (lowerCase.equals("doc")) {
                    c = 6;
                    break;
                }
            case 101149:
                if (lowerCase.equals("fax")) {
                    c = 17;
                    break;
                }
            case 102340:
                if (lowerCase.equals("gif")) {
                    c = 18;
                    break;
                }
            case 104387:
                if (lowerCase.equals("img")) {
                    c = '\u0013';
                    break;
                }
            case 105007:
                if (lowerCase.equals("jbg")) {
                    c = '\u0014';
                    break;
                }
            case 105439:
                if (lowerCase.equals("jpe")) {
                    c = '\u0015';
                    break;
                }
            case 105441:
                if (lowerCase.equals("jpg")) {
                    c = 12;
                    break;
                }
            case 106458:
                if (lowerCase.equals("m4a")) {
                    c = '.';
                    break;
                }
            case 107855:
                if (lowerCase.equals("mac")) {
                    c = 23;
                    break;
                }
            case 108184:
                if (lowerCase.equals("mkv")) {
                    c = '3';
                    break;
                }
            case 108272:
                if (lowerCase.equals("mp3")) {
                    c = '&';
                    break;
                }
            case 108273:
                if (lowerCase.equals("mp4")) {
                    c = '0';
                    break;
                }
            case 109963:
                if (lowerCase.equals("ogc")) {
                    c = '*';
                    break;
                }
            case 110779:
                if (lowerCase.equals("pbm")) {
                    c = 24;
                    break;
                }
            case 110801:
                if (lowerCase.equals("pcd")) {
                    c = 25;
                    break;
                }
            case 110810:
                if (lowerCase.equals("pcm")) {
                    c = '#';
                    break;
                }
            case 110817:
                if (lowerCase.equals("pct")) {
                    c = 27;
                    break;
                }
            case 110821:
                if (lowerCase.equals("pcx")) {
                    c = 26;
                    break;
                }
            case 110834:
                if (lowerCase.equals("pdf")) {
                    c = 0;
                    break;
                }
            case 110934:
                if (lowerCase.equals("pgm")) {
                    c = 28;
                    break;
                }
            case 111145:
                if (lowerCase.equals("png")) {
                    c = '\r';
                    break;
                }
            case 111213:
                if (lowerCase.equals("ppm")) {
                    c = 29;
                    break;
                }
            case 111220:
                if (lowerCase.equals("ppt")) {
                    c = 1;
                    break;
                }
            case 111297:
                if (lowerCase.equals("psd")) {
                    c = '\u001e';
                    break;
                }
            case 112675:
                if (lowerCase.equals("rar")) {
                    c = 9;
                    break;
                }
            case 112676:
                if (lowerCase.equals("ras")) {
                    c = '\u001f';
                    break;
                }
            case 112680:
                if (lowerCase.equals("raw")) {
                    c = 10;
                    break;
                }
            case 114766:
                if (lowerCase.equals("tga")) {
                    c = ' ';
                    break;
                }
            case 115312:
                if (lowerCase.equals("txt")) {
                    c = 5;
                    break;
                }
            case 117484:
                if (lowerCase.equals("wav")) {
                    c = '$';
                    break;
                }
            case 117835:
                if (lowerCase.equals("wma")) {
                    c = '+';
                    break;
                }
            case 117840:
                if (lowerCase.equals("wmf")) {
                    c = '\"';
                    break;
                }
            case 118783:
                if (lowerCase.equals("xls")) {
                    c = 3;
                    break;
                }
            case 120609:
                if (lowerCase.equals("zip")) {
                    c = '\u000b';
                    break;
                }
            case 2993896:
                if (lowerCase.equals("aiff")) {
                    c = '%';
                    break;
                }
            case 2996621:
                if (lowerCase.equals("alac")) {
                    c = '-';
                    break;
                }
            case 3088960:
                if (lowerCase.equals("docx")) {
                    c = 7;
                    break;
                }
            case 3145576:
                if (lowerCase.equals("flac")) {
                    c = ',';
                    break;
                }
            case 3268712:
                if (lowerCase.equals("jpeg")) {
                    c = 22;
                    break;
                }
            case 3447940:
                if (lowerCase.equals("pptx")) {
                    c = 2;
                    break;
                }
            case 3559925:
                if (lowerCase.equals("tiff")) {
                    c = '!';
                    break;
                }
            case 3645337:
                if (lowerCase.equals("webm")) {
                    c = '2';
                    break;
                }
            case 3682393:
                if (lowerCase.equals("xlsx")) {
                    c = 4;
                    break;
                }
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                imageView.setImageResource(R.drawable.pdf_file);
                return;
            case 1:
                imageView.setImageResource(R.drawable.all_doc);
                return;
            case 2:
                imageView.setImageResource(R.drawable.all_doc);
                return;
            case 3:
                imageView.setImageResource(R.drawable.excel_file);
                return;
            case 4:
                imageView.setImageResource(R.drawable.excel_file);
                return;
            case 5:
                imageView.setImageResource(R.drawable.text_file);
                return;
            case 6:
                imageView.setImageResource(R.drawable.word_file);
                return;
            case 7:
                imageView.setImageResource(R.drawable.word_file);
                return;
            case 8:
                imageView.setImageResource(R.drawable.all_doc);
                return;
            case 9:
                imageView.setImageResource(R.drawable.rar_file);
                return;
            case 10:
                imageView.setImageResource(R.drawable.rar_file);
                return;
            case 11:
                imageView.setImageResource(R.drawable.zip_file);
                return;
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case ' ':
            case '!':
            case '\"':
                imageView.setImageResource(R.drawable.all_doc);
                return;
            case '#':
            case '$':
            case '%':
            case '&':
            case '\'':
            case '(':
            case ')':
            case '*':
            case '+':
            case ',':
            case '-':
            case '.':
                imageView.setImageResource(R.drawable.all_doc);
                return;
            case '/':
            case '0':
            case '1':
            case '2':
            case '3':
                imageView.setImageResource(R.drawable.all_doc);
                return;
            case '4':
                imageView.setImageResource(R.drawable.all_doc);
                return;
            case '5':
                imageView.setImageResource(R.drawable.folder);
                return;
            default:
                imageView.setImageResource(R.drawable.all_doc);
                return;
        }
    }


    public String fileNameShortForm(String str) {
        try {
            String substring = str.substring(0, 10);
            String substring2 = str.substring(str.length() - 10);
            StringBuilder sb = new StringBuilder();
            sb.append(substring);
            sb.append(".....");
            sb.append(substring2);
            return sb.toString();
        } catch (Exception unused) {
            return str;
        }
    }
}
