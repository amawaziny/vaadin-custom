/*
 * Copyright 2014 freeOut Ahmed El-mawaziny.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.qfast.vaadin.addon.util;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.UI;

/**
 * @author Ahmed El-mawaziny
 */
public final class UiUtil {

    public static void setLocation(String uri) {
        JavaScript.getCurrent()
                .execute("window.location."
                        + "replace(\"" + uri + "#"
                        + UI.getCurrent().getPage().getUriFragment() + "\")");
    }

    public static Resource getResource(String fileName) {
        String extention = getExtention(fileName);
        switch (extention.toLowerCase()) {
            case ".pdf":
                return FontAwesome.FILE_PDF_O;
            case ".doc":
            case ".docx":
                return FontAwesome.FILE_WORD_O;
            case ".xls":
            case ".xlsx":
                return FontAwesome.FILE_EXCEL_O;
            case ".bmp":
            case ".dib":
            case ".rle":
            case ".jpg":
            case ".jpeg":
            case ".jpe":
            case ".jfif":
            case ".gif":
            case ".tif":
            case ".tiff":
            case ".png":
                return FontAwesome.FILE_IMAGE_O;
            case ".pptx":
            case ".ppt":
                return FontAwesome.FILE_POWERPOINT_O;
            case ".mp3":
            case ".wav":
            case ".ogg":
                return FontAwesome.FILE_AUDIO_O;
            case ".mp4":
                return FontAwesome.FILE_MOVIE_O;
            case ".rar":
            case ".tar.gz":
            case ".tar":
            case ".7z":
            case ".gz":
            case ".zip":
            case ".jar":
                return FontAwesome.FILE_ARCHIVE_O;
            case ".java":
            case ".js":
                return FontAwesome.FILE_CODE_O;
            default:
                return FontAwesome.FILE_TEXT_O;
        }
    }

    private static String getExtention(String fileName) {
        String ext = fileName;
        if (ext.lastIndexOf('.') != -1) {
            ext = ext.substring(ext.lastIndexOf('.'));
        } else {
            ext = "";
        }
        return ext;
    }

    public static void setButtonsEnable(AbstractLayout layout, boolean enable) {
        for (Component c : layout) {
            if (c instanceof Button) {
                c.setEnabled(enable);
            }
        }
    }

    public static void setPlaceholder(AbstractField field, String placeholder) {
        Page.getCurrent().getJavaScript().execute("document.getElementById('"
                + field.getId() + "').placeholder = '" + placeholder+"';");
    }
}
