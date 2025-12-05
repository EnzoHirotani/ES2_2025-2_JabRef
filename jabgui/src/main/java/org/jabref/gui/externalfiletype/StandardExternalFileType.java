package org.jabref.gui.externalfiletype;

import org.jabref.gui.icon.IconTheme;
import org.jabref.gui.icon.JabRefIcon;
import org.jabref.logic.l10n.Localization;

public enum StandardExternalFileType implements ExternalFileType {

    PDF("PDF", "pdf", "application/pdf", FileTypeConstants.EVINCE, "pdfSmall", IconTheme.JabRefIcons.PDF_FILE),
    PostScript("PostScript", "ps", "application/postscript", FileTypeConstants.EVINCE, "psSmall", IconTheme.JabRefIcons.FILE),
    Word("Word", "doc", "application/msword", FileTypeConstants.OO_WRITER, FileTypeConstants.OPENOFFICE, IconTheme.JabRefIcons.FILE_WORD),
    Word_NEW("Word 2007+", "docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", FileTypeConstants.OO_WRITER, FileTypeConstants.OPENOFFICE, IconTheme.JabRefIcons.FILE_WORD),
    OpenDocument_TEXT(Localization.lang("OpenDocument text"), "odt", "application/vnd.oasis.opendocument.text", FileTypeConstants.OO_WRITER, FileTypeConstants.OPENOFFICE, IconTheme.JabRefIcons.FILE_OPENOFFICE),
    Excel("Excel", "xls", "application/excel", FileTypeConstants.OO_CALC, FileTypeConstants.OPENOFFICE, IconTheme.JabRefIcons.FILE_EXCEL),
    Excel_NEW("Excel 2007+", "xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", FileTypeConstants.OO_CALC, FileTypeConstants.OPENOFFICE, IconTheme.JabRefIcons.FILE_EXCEL),
    OpenDocumentSpreadsheet(Localization.lang("OpenDocument spreadsheet"), "ods", "application/vnd.oasis.opendocument.spreadsheet", FileTypeConstants.OO_CALC, FileTypeConstants.OPENOFFICE, IconTheme.JabRefIcons.FILE_OPENOFFICE),
    PowerPoint("PowerPoint", "ppt", "application/vnd.ms-powerpoint", FileTypeConstants.OO_IMPRESS, FileTypeConstants.OPENOFFICE, IconTheme.JabRefIcons.FILE_POWERPOINT),
    PowerPoint_NEW("PowerPoint 2007+", "pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation", FileTypeConstants.OO_IMPRESS, FileTypeConstants.OPENOFFICE, IconTheme.JabRefIcons.FILE_POWERPOINT),
    OpenDocumentPresentation(Localization.lang("OpenDocument presentation"), "odp", "application/vnd.oasis.opendocument.presentation", FileTypeConstants.OO_IMPRESS, FileTypeConstants.OPENOFFICE, IconTheme.JabRefIcons.FILE_OPENOFFICE),
    RTF("Rich Text Format", "rtf", "application/rtf", FileTypeConstants.OO_WRITER, FileTypeConstants.OPENOFFICE, IconTheme.JabRefIcons.FILE_TEXT),
    PNG(Localization.lang(FileTypeConstants.IMAGE_PARAM, "PNG"), "png", "image/png", "gimp", FileTypeConstants.PICTURE, IconTheme.JabRefIcons.PICTURE),
    GIF(Localization.lang(FileTypeConstants.IMAGE_PARAM, "GIF"), "gif", "image/gif", "gimp", FileTypeConstants.PICTURE, IconTheme.JabRefIcons.PICTURE),
    JPG(Localization.lang(FileTypeConstants.IMAGE_PARAM, "JPG"), "jpg", "image/jpeg", "gimp", FileTypeConstants.PICTURE, IconTheme.JabRefIcons.PICTURE),
    Djvu("Djvu", "djvu", "image/vnd.djvu", FileTypeConstants.EVINCE, "psSmall", IconTheme.JabRefIcons.FILE),
    TXT("Text", "txt", "text/plain", FileTypeConstants.EMACS, FileTypeConstants.EMACS, IconTheme.JabRefIcons.FILE_TEXT),
    TEX("LaTeX", "tex", "application/x-latex", FileTypeConstants.EMACS, FileTypeConstants.EMACS, IconTheme.JabRefIcons.FILE_TEXT),
    CHM("CHM", "chm", "application/mshelp", "gnochm", "www", IconTheme.JabRefIcons.WWW),
    TIFF(Localization.lang(FileTypeConstants.IMAGE_PARAM, "TIFF"), "tiff", "image/tiff", "gimp", FileTypeConstants.PICTURE, IconTheme.JabRefIcons.PICTURE),
    URL("URL", "html", "text/html", FileTypeConstants.FIREFOX, "www", IconTheme.JabRefIcons.WWW),
    MHT("MHT", "mht", "multipart/related", FileTypeConstants.FIREFOX, "www", IconTheme.JabRefIcons.WWW),
    ePUB("ePUB", "epub", "application/epub+zip", FileTypeConstants.FIREFOX, "www", IconTheme.JabRefIcons.WWW),
    MARKDOWN("Markdown", "md", "text/markdown", FileTypeConstants.EMACS, FileTypeConstants.EMACS, IconTheme.JabRefIcons.FILE_TEXT);

    private final String name;
    private final String extension;
    private final String mimeType;
    private final String openWith;
    private final String iconName;
    private final JabRefIcon icon;

    StandardExternalFileType(String name, String extension, String mimeType,
                             String openWith, String iconName, JabRefIcon icon) {
        this.name = name;
        this.extension = extension;
        this.mimeType = mimeType;
        this.openWith = openWith;
        this.iconName = iconName;
        this.icon = icon;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getExtension() {
        return extension;
    }

    @Override
    public String getMimeType() {
        return mimeType;
    }

    @Override
    public String getOpenWithApplication() {
        return "";
    }

    @Override
    public JabRefIcon getIcon() {
        return icon;
    }

    private static class FileTypeConstants {
        private static final String OPENOFFICE = "openoffice";
        private static final String EVINCE = "evince";
        private static final String OO_WRITER = "oowriter";
        private static final String OO_CALC = "oocalc";
        private static final String OO_IMPRESS = "ooimpress";
        private static final String IMAGE_PARAM = "%0 image";
        private static final String PICTURE = "picture";
        private static final String EMACS = "emacs";
        private static final String FIREFOX = "firefox";
    }
}
