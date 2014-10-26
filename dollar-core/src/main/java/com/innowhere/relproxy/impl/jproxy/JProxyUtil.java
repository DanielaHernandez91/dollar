/*
 * Copyright (c) 2014 Neil Ellis
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.innowhere.relproxy.impl.jproxy;

import com.innowhere.relproxy.RelProxyException;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author jmarranz
 */
public class JProxyUtil {
    public static String getFileExtension(File file) {
        String path = file.getAbsolutePath();
        int pos = path.lastIndexOf('.');
        if (pos != -1)
            return path.substring(pos + 1);
        return "";
    }

    public static File getParentDir(File file) {
        return file.getParentFile();
        /*
        File file = new File(absFilePath);
        if (!file.isAbsolute() && !absFilePath.startsWith("\\") && !absFilePath.startsWith("/"))
            return null; // Las comprobaciones startsWith son para intentar soportar MSYS (y cygwin) en Windows para paths que empiezan por "/" o por "\" por ejemplo
        
        
        String absFilePath = file.getAbsolutePath(); // Nos devuelve el path normalizado, ej si empieza por "/" (MSYS) lo convierte bien en "C:\..."
        int pos = absFilePath.lastIndexOf(File.separatorChar);
        if (pos == -1)
            return null; // no nos esperamos esto
        return new File(absFilePath.substring(0,pos)); // Sin el terminador
        */
    }

    public static byte[] readURL(URL url) {
        URLConnection urlCon;
        try {
            urlCon = url.openConnection();
            return readInputStream(urlCon.getInputStream());
        } catch (IOException ex) {
            throw new RelProxyException(ex);
        }
    }

    public static byte[] readFile(File file) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException ex) {
            throw new RelProxyException(ex);
        }

        return readInputStream(fis);
    }

    public static byte[] readInputStream(InputStream is) {
        return readInputStream(is, 50); // 50Kb => unas 100 lecturas 5 Mb
    }

    public static byte[] readInputStream(InputStream is, int bufferSizeKb) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[bufferSizeKb * 1024];

            int size;
            while ((size = is.read(buffer)) != -1) {
                out.write(buffer, 0, size);
            }
        } catch (IOException ex) {
            throw new RelProxyException(ex);
        } finally {
            try {
                is.close();
            } catch (IOException ex2) {
                throw new RelProxyException(ex2);
            }
        }

        return out.toByteArray();
    }

    public static void saveFile(File file, byte[] content) {
        File parent = getParentDir(file);
        if (parent != null) parent.mkdirs();
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            out.write(content, 0, content.length);
        } catch (IOException ex) {
            throw new RelProxyException(ex);
        } finally {
            if (out != null) try {
                out.close();
            } catch (IOException ex2) {
                throw new RelProxyException(ex2);
            }
        }
    }

    public static String readTextFile(File file, String encoding) {
        Reader reader = null;
        try {
            reader = new InputStreamReader(new FileInputStream(file), encoding);   // FileReader no permite especificar el encoding y total no hace nada que no haga InputStreamReader
        } catch (IOException ex) {
            throw new RelProxyException(ex);
        }

        return readTextFile(reader);
    }

    public static String readTextFile(Reader reader) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(reader);   // FileReader no permite especificar el encoding y total no hace nada que no haga InputStreamReader       
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append('\n');
                line = br.readLine();
            }
            return sb.toString();
        } catch (IOException ex) {
            throw new RelProxyException(ex);
        } finally {
            if (br != null) try {
                br.close();
            } catch (IOException ex) {
                throw new RelProxyException(ex);
            }
        }
    }
}
