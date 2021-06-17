package com.card.script;

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction
import org.jetbrains.annotations.NotNull

import java.util.function.Consumer;

public class DiffDspTask extends DefaultTask {

    @Input
    public String path;

    @Input
    public String comparePath;

    public String BUILD_DIR = 'diff';
    //html,console
    public String outType = "html";

    @TaskAction
    public void action() {
        File newFile = project.file(path)
        File oldFile = project.file(comparePath)

        if (!newFile.exists() || !oldFile.exists()) {
            throw new RuntimeException("newVersionPath or oldVersionPath not exists")
        }

        File buildDir = getProject().file(getProject().buildDir.getAbsolutePath() + File.separator + BUILD_DIR)
        if (buildDir.exists()) {
            buildDir.deleteDir()
        }


        List<Model> newModels = getModes(newFile.getAbsolutePath())
        List<Model> oldModels = getModes(oldFile.getAbsolutePath())
        List<DiffModel> diffModels = new ArrayList<>();
        for (Model newModel : newModels) {
            if (!getProject().file(newModel.path).exists()) {
                continue
            }

            Model oldModel = getSameModel(oldModels, newModel)
            if (oldModel == null) {
                DiffModel newCreateModel = new DiffModel()
                newCreateModel.model = newModel
                newCreateModel.status = DiffModel.STATUS_NEW
                newCreateModel.doDiff()
                diffModels.add(newCreateModel)
                continue
            }
            println("----正在扫描：" + newModel.group + ":" + newModel.artifact)
            DiffModel diffModel = diff(newModel, oldModel)

            diffModels.add(diffModel)
        }


        for (Model oldModel : oldModels) {
            if (!getProject().file(oldModel.path).exists()) {
                continue
            }

            Model newModel = getSameModel(newModels, oldModel)

            if (newModel == null) {
                DiffModel deleteModel = new DiffModel();
                deleteModel.oldModel = oldModel
                deleteModel.status = DiffModel.STATUS_DELETE
                deleteModel.doDiff()
                diffModels.add(deleteModel)
            }
        }
        diffModels.sort()
        if (outType == 'console') {
            diffModels.forEach(new Consumer<DiffModel>() {
                @Override
                void accept(DiffModel diffModel) {
                    println('------------------------------------------------------------------------')
                    println(String.format("module : %s", diffModel.getModule()))
                    if (diffModel.status == DiffModel.STATUS_NEW) {
                        println(diffModel.getModelInfo())
                        println("添加")
                    } else if (diffModel.status == DiffModel.STATUS_DELETE) {
                        println(diffModel.getOldModelInfo())
                        println("删除")
                    } else {
                        println(diffModel.getModelInfo())
                        println(diffModel.getOldModelInfo())
                    }
                    println("diff size : " + diffModel.diff)
                    println('------------------------------------------------------------------------')
                    if (diffModel.diffFileModels != null) {
                        diffModel.diffFileModels.forEach(new Consumer<DiffFileModel>() {
                            @Override
                            void accept(DiffFileModel diffFileModel) {
                                if (diffFileModel.diff != 0) {
                                    println("    " + diffFileModel.getChange() + " : " + diffFileModel.shortPath + ", " + diffFileModel.diff)
                                }

                            }
                        })
                    }

                }
            })
        } else {
            File reportDir = getProject().file(buildDir.getAbsolutePath() + File.separator + 'report')
            if (reportDir.exists()) {
                reportDir.deleteDir()
            }
            reportDir.mkdirs()

            File indexFile = getProject().file(reportDir.getAbsolutePath() + File.separator + "index.html")
            FileWriter fileWriter = new FileWriter(indexFile)
            fileWriter.write("<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "<meta charset=\"utf-8\">\n" +
                    "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                    "    <link href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\" rel=\"stylesheet\">" +
                    "<title>Diff Index</title>\n" +
                    "</head>")
            fileWriter.write("<body>")
            fileWriter.write("<table class=\"table table-striped\">")

            fileWriter.write("<tr>")

            fileWriter.write("<td>")
            fileWriter.write("包")
            fileWriter.write("</td>")

            fileWriter.write("<td>")
            fileWriter.write("新版本")
            fileWriter.write("</td>")

            fileWriter.write("<td>")
            fileWriter.write("老版本")
            fileWriter.write("</td>")

            fileWriter.write("<td>")
            fileWriter.write("差异")
            fileWriter.write("</td>")


            fileWriter.write("</tr>")

            for (DiffModel diffModel : diffModels) {
                fileWriter.write("<a href=\"http://www.w3school.com.cn\">")
                String htmlPath = diffModel.module.replace(":", "_") + ".html"
                File htmlFile = getProject().file(reportDir.getAbsolutePath() + File.separator + htmlPath)
                if (htmlFile.exists()) {
                    htmlFile.delete()
                }

                if (diffModel.diffFileModels != null) {
                    htmlChild(htmlFile, diffModel.diffFileModels)
                }
                fileWriter.write("<tr> \n")

                fileWriter.write("<td>\n")
                fileWriter.write(String.format("<a href=\"%s\">\n", "./" + htmlPath))
                fileWriter.write(diffModel.getModule())
                fileWriter.write("</a>\n")
                fileWriter.write("</td>\n")

                fileWriter.write("<td>\n")
                fileWriter.write(diffModel.getModelInfoByHtml())
                fileWriter.write("</td>\n")

                fileWriter.write("<td>\n")
                fileWriter.write(diffModel.getOldModelInfoByHtml())
                fileWriter.write("</td>\n")

                fileWriter.write("<td>\n")
                fileWriter.write(diffModel.diff + "")
                fileWriter.write("</td>\n")

                fileWriter.write("</tr>\n")

            }
            fileWriter.write("</table>\n")

            fileWriter.write("</body>\n")
            fileWriter.write("</html>\n")
            fileWriter.close()
            println("点击查看")
            println("file://" + indexFile.getAbsolutePath())

        }
    }

    def htmlChild(File htmlFile, List<DiffFileModel> diffFileModels) {

        FileWriter fileWriter = new FileWriter(htmlFile)
        fileWriter.write("<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"utf-8\">\n" +
                "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "    <link href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\" rel=\"stylesheet\">" +
                "<title>" + htmlFile.getName() + "</title>\n" +
                "</head>")
        fileWriter.write("<body>")
        fileWriter.write("<table class=\"table table-striped\">")

        fileWriter.write("<tr>")

        fileWriter.write("<td>")
        fileWriter.write("状态")
        fileWriter.write("</td>")

        fileWriter.write("<td>")
        fileWriter.write("文件")
        fileWriter.write("</td>")

        fileWriter.write("<td>")
        fileWriter.write("差异")
        fileWriter.write("</td>")

        fileWriter.write("</tr>")

        for (DiffFileModel diffFileModel : diffFileModels) {

            if (diffFileModel.diff != 0) {
                //println("    " + diffFileModel.getChange() + " : " + diffFileModel.shortPath + ", " + diffFileModel.diff)

                fileWriter.write("<tr>")

                fileWriter.write("<td>")
                fileWriter.write(diffFileModel.getChange())
                fileWriter.write("</td>")

                fileWriter.write("<td>")
                fileWriter.write(diffFileModel.shortPath)
                fileWriter.write("</td>")

                fileWriter.write("<td>")
                fileWriter.write(diffFileModel.diff+"")
                fileWriter.write("</td>")

                fileWriter.write("</tr>")
            }
        }

        fileWriter.write("</table>")

        fileWriter.write("</body>")
        fileWriter.write("</html>")
        fileWriter.close()
    }


    class DiffModel implements Comparable<DiffModel> {
        public static final STATUS_NORMAL = 0
        public static final STATUS_NEW = 1
        public static final STATUS_DELETE = 1
        public int status = STATUS_NORMAL
        public List<DiffFileModel> diffFileModels;
        public Model model
        public Model oldModel

        public long diff;

        def doDiff() {
            if (model != null && oldModel != null) {
                diff = model.size - oldModel.size
            } else if (model != null) {
                diff = model.size
            } else if (oldModel != null) {
                diff = -oldModel.size
            }

        }

        String getModule() {
            if (model != null) {
                return model.group + ":" + model.artifact
            } else if (oldModel != null) {
                return oldModel.group + ":" + oldModel.artifact
            } else {
                return "unknow"
            }
        }

        String getModelInfo() {
            if (model != null) {
                return getSpace(30, "version : " + model.version) + ", size " + model.size
            }
            return "";
        }

        String getModelInfoByHtml() {
            if (model != null) {
                return "version : " + model.version + "<br />size : " + model.size
            }
            return "";
        }

        String getOldModelInfo() {
            if (oldModel != null) {
                return getSpace(30, "version : " + oldModel.version) + ", size " + oldModel.size
            }
            return "";
        }

        String getOldModelInfoByHtml() {
            if (oldModel != null) {
                return "version : " + oldModel.version + "<br />size : " + oldModel.size
            }
            return "";
        }

        String getSpace(int totalSpace, String displayName) {
            StringBuilder stringBuilder = new StringBuilder();
            int displayTotal = totalSpace;
            stringBuilder.append(displayName)
            int gap = displayTotal - displayName.length();
            if (gap > 0) {
                for (i in 0..<gap) {
                    stringBuilder.append(" ")
                }
            }
            return stringBuilder.toString()
        }


        @Override
        int compareTo(@NotNull DiffModel o) {
            return Long.compare(o.diff, diff)
        }
    }

    class DiffFileModel implements Comparable<DiffFileModel> {
        public static final int CHANGE_NEW = 1;//新文件
        public static final int CHANGE_HIGH = 2;//增大
        public static final int CHANGE_EQ = 3;//相等
        public static final int CHANGE_LOW = 4;//减小
        public static final int CHANGE_DEL = 5;//删除
        public int change = 0;
        public long size = 0;
        public long oldSize = 0;
        public long diff = 0;

        public static final int TYPE_CLASS = 1;
        public static final int TYPE_SO = 2;
        public static final int TYPE_XML = 3;
        public static final int TYPE_UNKNOW = 4;

        public int type = TYPE_UNKNOW;

        public String path
        public String shortPath


        @Override
        int compareTo(@NotNull DiffFileModel o) {
            return o.diff - diff
        }

        void doDiff() {
            diff = size - oldSize
        }


        String getChange() {
            if (change == CHANGE_NEW) {
                return "新文件"
            } else if (change == CHANGE_HIGH) {
                return "增  大"
            } else if (change == CHANGE_EQ) {
                return "相  等"
            } else if (change == CHANGE_LOW) {
                return "减  小"
            } else if (change == CHANGE_DEL) {
                return "删  除"
            }
        }
    }

    DiffModel diff(Model newModel, Model oldModel) {
        DiffModel diffModel = new DiffModel();
        String moduleShotName = newModel.group + ":" + newModel.artifact

        File file = new File(getProject().buildDir.getAbsolutePath() + File.separator + BUILD_DIR + File.separator + moduleShotName)

        if (file.exists()) {
            file.deleteDir()
        }
        file.mkdirs()
        String outNewPath = file.getAbsolutePath() + File.separator + newModel.module

        String outOldPath = file.getAbsolutePath() + File.separator + oldModel.module

        copy(newModel.path, outNewPath)
        copy(oldModel.path, outOldPath)
        // compare
        List<DiffFileModel> diffFileModels = new ArrayList<>();
        int newStartIndex = outNewPath.length()
        getProject().fileTree(outNewPath).forEach(new Consumer<File>() {
            @Override
            void accept(File child) {
                String absPath = child.getAbsolutePath()
                File oldFile = getProject().file(absPath.replace(newModel.module, oldModel.module))
                DiffFileModel diffFileModel = new DiffFileModel();

                String shortPath = absPath.substring(newStartIndex)

                long size = child.length()
                diffFileModel.size = size
                if (!oldFile.exists()) {
                    diffFileModel.change = DiffFileModel.CHANGE_NEW
                } else {
                    long oldSize = oldFile.size()
                    diffFileModel.oldSize = oldSize
                    if (size > oldSize) {
                        diffFileModel.change = DiffFileModel.CHANGE_HIGH
                    } else if (size < oldSize) {
                        diffFileModel.change = DiffFileModel.CHANGE_LOW
                    } else {
                        diffFileModel.change = DiffFileModel.CHANGE_EQ
                    }
                }
                diffFileModel.doDiff()
                diffFileModel.path = absPath
                diffFileModel.shortPath = shortPath
                diffFileModel.handleFileType(child.getName(), diffFileModel)
                diffFileModels.add(diffFileModel)
            }
        })

        int oldStartIndex = outOldPath.length()
        getProject().fileTree(outOldPath).forEach(new Consumer<File>() {
            @Override
            void accept(File child) {
                String absPath = child.getAbsolutePath()
                File newFile = getProject().file(absPath.replace(oldModel.module, newModel.module))
                if (!newFile.exists()) {
                    String shortPath = absPath.substring(oldStartIndex)

                    DiffFileModel diffFileModel = new DiffFileModel()
                    diffFileModel.oldSize = child.length()
                    diffFileModel.doDiff()

                    diffFileModel.path = absPath
                    diffFileModel.shortPath = shortPath
                    diffFileModel.change = DiffFileModel.CHANGE_DEL
                    handleFileType(child.getName(), diffFileModel)
                    diffFileModels.add(diffFileModel)
                }

            }
        })

        diffFileModels.sort()

        diffModel.model = newModel
        diffModel.oldModel = oldModel
        diffModel.doDiff()
        diffModel.diffFileModels = diffFileModels;

        return diffModel;
    }

    def handleFileType(String fileName, DiffFileModel diffFileModel) {

        if (fileName.contains(".class")) {
            diffFileModel.type = DiffFileModel.TYPE_CLASS
        } else if (fileName.contains(".xml")) {
            diffFileModel.type = DiffFileModel.TYPE_XML
        } else if (fileName.contains(".so")) {
            diffFileModel.type = DiffFileModel.TYPE_SO
        } else {
            diffFileModel.type = DiffFileModel.TYPE_UNKNOW
        }
    }

    def copy(String f, String i) {
        getProject().copy {
            from(getProject().zipTree(f))
            into(i)
        }

        File file = getProject().file(i)
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles()
            for (File childFile : files) {
                if (childFile.isFile() && childFile.getName().contains("classes.jar")) {
                    copy(childFile.getAbsolutePath(), childFile.getAbsolutePath() + "_dir")
                    childFile.delete()
                }
            }
        }

    }


    class Model {
        //module,group,artifact,version,path
        public String module
        public String group
        public String artifact
        public String version
        public String path
        public long size;
    }

    Model getSameModel(List<Model> models, model) {
        for (Model m : models) {
            if (m.group == model.group && m.artifact == model.artifact) {
                return m
            }
        }
        return null
    }


    List<Model> getModes(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return null
        }
        List<Model> models = new ArrayList<>()
        FileReader fileReader = new FileReader(file)
        try {
            List<String> strings = fileReader.readLines();
            for (String str : strings) {
                String[] arrays = str.split(",")

                if (arrays.length == 5) {
                    if (!getProject().file(arrays[4]).exists()) {
                        continue
                    }
                    Model model = new Model()
                    model.module = arrays[0]
                    model.group = arrays[1]
                    model.artifact = arrays[2]
                    model.version = arrays[3]
                    model.path = arrays[4]
                    model.size = getProject().file(arrays[4]).length()
                    models.add(model)
                }
            }
        } finally {
            fileReader.close()
        }

        return models
    }


}
