package company.android.documentmanager.HandleFile;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import company.android.documentmanager.Model.FileViewModel;
import company.android.documentmanager.Model.HomeDocument;
import company.android.documentmanager.R;
import company.android.documentmanager.Utility.Constant;

public class FileWorker  {

public static ArrayList<HomeDocument> loadFolder(String dir, String which, String [] pattern, ArrayList<HomeDocument> documents){

    if (which.equals(Constant.Others)){
        documents.clear();
    }

    if (pattern==null){
        pattern = Constant.getAllfile();
    }

    File root=new File(dir);

    File[] list = root.listFiles();
    for(File ff: list) {

        if (ff.isDirectory()){
            boolean isCorrespond = searchDirectory(ff.getAbsolutePath(),pattern);
            if (isCorrespond){
                long size =0;
                if (checkIfSubDirIsAFile(ff)){
                    size = CurrentFileSize(ff,pattern);
                }else{
                    size = dirSize(ff,pattern);
                }
                documents.add(new HomeDocument(R.drawable.folder,ff.getName(),String.valueOf(size),ff.getAbsolutePath(),ff));
            }
        }else{
            boolean isCorrespond=rawFileSearch(ff.getName(),pattern);
            if (isCorrespond){
                documents.add(new HomeDocument(R.drawable.folder,ff.getName(),"0",ff.getAbsolutePath(),ff));

            }

        }


    }
    return documents;
}

public  static ArrayList<HomeDocument> folderFiles(String fileLocation, String which,String [] pattern,ArrayList<HomeDocument> documents){
    if (which.equals(Constant.Others)){
        documents.clear();
    }

    File root = new File(fileLocation);
    File[] list = root.listFiles();

    for (File file :list){
        if(!file.isDirectory()){
            boolean check = rawFileSearch(file.getName(),Constant.getAllfile());
            if (check){
                documents.add(new HomeDocument(R.drawable.folder,file.getName(),"0",file.getAbsolutePath(),file));
            }
        }
    }

    return documents;
}

public static ArrayList<HomeDocument> collectAllFile(String fileLocation, String which,String [] pattern,ArrayList<HomeDocument> documents){

    ArrayList<String> saveParent = new ArrayList<>();
    if (which.equals(Constant.Others)){
        documents.clear();
    }

    if (pattern==null){
        pattern = Constant.getAllfile();
    }


    File dir = new File(fileLocation);

    List<File> files = (List<File>) FileUtils.listFiles(dir, pattern, true);

    for(File file :files){
        File parent = new File(file.getParent());
        if (!saveParent.contains(parent.getName())){
            saveParent.add(parent.getName());
            long size = folderDirectSearch(parent,Constant.getAllfile());
            documents.add(new HomeDocument(R.drawable.folder_view,parent.getName(),""+size,parent.getAbsolutePath(),parent));
        }

    }
    if (!saveParent.isEmpty()){
        saveParent.clear();
    }


    return documents;

}
public static ArrayList<HomeDocument> loadFileManager(String dir,String which,String [] pattern,ArrayList<HomeDocument> documents){


    if (which.equals(Constant.Others)){
        documents.clear();
    }

    if (pattern==null){
        pattern = Constant.getAllfile();
    }

    File root=new File(dir);

    File[] list = root.listFiles();
    for(File ff: list) {

        if (ff.isDirectory()){

                long size =0;
                if (checkIfSubDirIsAFile(ff)){
                    size = NormalFileSearch(ff);
                }else{
                    size = dirSize(ff,pattern);
                }

                documents.add(new HomeDocument(R.drawable.folder,ff.getName(),String.valueOf(size),ff.getAbsolutePath(),ff));
        }else{
            boolean isCorrespond=rawFileSearch(ff.getName(),pattern);
            if (isCorrespond){
                documents.add(new HomeDocument(R.drawable.folder,ff.getName(),"0",ff.getAbsolutePath(),ff));
            }
        }
    }
    return documents;
}



    //Check if a directory Has a sub Directory or Just a file
    private static boolean checkIfSubDirIsAFile(File file){
        for (File check :file.listFiles()){
            if (check.isDirectory()){
                return true;
            }
        }
        return false;
    }
    //Search into every Directory In the Device Using a dependency Libary
    private static boolean searchDirectory(String fileLocation,String [] pattern){
        File dir = new File(fileLocation);
        String[] extensions =pattern;


        List<File> files = (List<File>) FileUtils.listFiles(dir, extensions, true);
        if (!files.isEmpty()){
            return true;
        }

        return false;
    }


    private static long NormalFileSearch(File dir){
    if (dir.exists()){
        if (dir.isDirectory()){

            long result = 0;
            File [] files = dir.listFiles();

            for (File single:files){
               result +=1;
            }


            return result;
        }
        return 0;
    }
    return 0;
    }

    //Check the Current Folder Size
    private static long CurrentFileSize(File dir,String [] pattern){
        if (dir.exists()) {

            if (dir.isDirectory()) {

                long result = 0;
                File [] files = dir.listFiles();

                for (File single:files){
                    if (single.isDirectory()){
                        boolean isCorrespond = searchDirectory(single.getAbsolutePath(),pattern);
                        if (isCorrespond){
                            result +=1;
                        }
                    }else {
                        boolean isCorrespond = rawFileSearch(single.getName(),pattern);
                        if (isCorrespond){
                            result +=1;
                        }
                    }
                }


                return result;
            }
            return 0;
        }
        return 0;
    }

    //Search For raw File Contain in a Folder
    private static boolean rawFileSearch(String filename,String [] pattern){

        String pat[] = pattern;
        for (String ends:pat){
            if (filename.endsWith(ends)){
                return true;
            }
        }
        return false;
    }
    public  static  long folderDirectSearch(File dir,String [] pattern){
    if (dir.exists()){
        long result = 0;
        File [] fileList = dir.listFiles();
        if (fileList ==null){
            return 0;
        }
        for (File currentFile:fileList){
            if (!currentFile.isDirectory()){
                boolean checkFile = rawFileSearch(currentFile.getName(),pattern);
                if (checkFile){
                    result +=1;
                }
            }
        }
        return  result;
    }
    return 0;
    }

    //Check Current Folder And File Size
    private static long dirSize(File dir,String [] pattern) {

        String [] followpat = pattern;

        if (dir.exists()){
            long result = 0;
            File[] fileList = dir.listFiles();
            if (fileList != null) {
                for(int i = 0; i < fileList.length; i++) {
                    // Recursive call if it's a directory
                    if(fileList[i].isDirectory()) {
                        result += dirSize(fileList[i],followpat);
                    } else {
                        // Sum the file size in bytes
                        boolean isCorrespond=rawFileSearch(fileList[i].getName(),followpat);
                        if (isCorrespond){
                            result += 1;
                        }

                    }
                }
            }
            return result; // return the file size
        }
        return 0;
    }
    public static ArrayList<HomeDocument> searchAllFile(String fileLocation,String [] pattern,ArrayList<HomeDocument> documents){


        File dir = new File(fileLocation);

        List<File> files = (List<File>) FileUtils.listFiles(dir, pattern, true);

        if (!files.isEmpty()){
            for(File ff: files) {
                documents.add(new HomeDocument(R.drawable.folder,ff.getName(),"0",ff.getAbsolutePath(),ff));
            }
        }

        return documents;


    }
    public static ArrayList<FileViewModel>  searchDirectory(String fileLocation, String [] pattern, boolean
            clear, ArrayList<FileViewModel> documents){




        File dir = new File(fileLocation);
        String[] extensions =pattern;

        List<File> files = (List<File>) FileUtils.listFiles(dir, extensions, true);

        ArrayList<FileViewModel> fileArrayList = new ArrayList<>();
        if (files.isEmpty()){

        }else{
            for (File presentFile : files){
                fileArrayList.add(new FileViewModel(presentFile,presentFile.length(),presentFile.getName(),presentFile.lastModified()));
            }

        }

        return fileArrayList;

    }
}
