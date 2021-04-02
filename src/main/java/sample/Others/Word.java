package sample.Others;

import JaxbTests.Bibliotheque;
import javafx.collections.ObservableList;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URL;

public class Word {
    /**
     * Ajout de Style pour le sommaire Word.
     * @param docxDocument
     * @param strStyleId
     * @param headingLevel
     */
    private static void addCustomHeadingStyle(XWPFDocument docxDocument, String strStyleId, int headingLevel) {

        CTStyle ctStyle = CTStyle.Factory.newInstance();
        ctStyle.setStyleId(strStyleId);

        CTString styleName = CTString.Factory.newInstance();
        styleName.setVal(strStyleId);
        ctStyle.setName(styleName);

        CTDecimalNumber indentNumber = CTDecimalNumber.Factory.newInstance();
        indentNumber.setVal(BigInteger.valueOf(headingLevel));

        // lower number > style is more prominent in the formats bar
        ctStyle.setUiPriority(indentNumber);

        CTOnOff onoffnull = CTOnOff.Factory.newInstance();
        ctStyle.setUnhideWhenUsed(onoffnull);

        // style shows up in the formats bar
        ctStyle.setQFormat(onoffnull);

        // style defines a heading of the given level
        CTPPr ppr = CTPPr.Factory.newInstance();
        ppr.setOutlineLvl(indentNumber);
        ctStyle.setPPr(ppr);

        XWPFStyle style = new XWPFStyle(ctStyle);

        // is a null op if already defined
        XWPFStyles styles = docxDocument.createStyles();

        style.setType(STStyleType.PARAGRAPH);
        styles.addStyle(style);

    }

    /**
     * Création du fichier Word recensant les livres du tableau choisi pour l'export.
     * Ce fichier est composé d'une page de garde, d'un sommaire, des livres et un tableau affichant les livres empruntés.
     * @param wordLivre
     */
    public void exportWord(File wordLivre, ObservableList<Bibliotheque.Livre> livres) {
        try{
            XWPFDocument doc = new XWPFDocument();
            FileOutputStream out = new FileOutputStream(wordLivre);
            XWPFParagraph paragraph = doc.createParagraph();
            paragraph.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun ru = paragraph.createRun();
            ru.setItalic(true);
            ru.setBold(true);
            ru.setFontSize(50);
            ru.setTextPosition(100);
            ru.setText("Gestionnaire d'une Bibliothèque");
            ru.addBreak(BreakType.PAGE);

            // create header start
            // create header-footer
            XWPFHeaderFooterPolicy headerFooterPolicy = doc.getHeaderFooterPolicy();
            if (headerFooterPolicy == null) headerFooterPolicy = doc.createHeaderFooterPolicy();

            XWPFHeader header = headerFooterPolicy.createHeader(XWPFHeaderFooterPolicy.DEFAULT);

            paragraph.setAlignment(ParagraphAlignment.CENTER);

            ru.setText("Sommaire");
            // create footer start
            XWPFFooter footer = headerFooterPolicy.createFooter(XWPFHeaderFooterPolicy.DEFAULT);
            //XWPFFooter footer = doc.createFooter(HeaderFooterType.DEFAULT);

            paragraph = footer.getParagraphArray(0);
            if (paragraph == null) paragraph = footer.createParagraph();
            paragraph.setAlignment(ParagraphAlignment.CENTER);
            ru = paragraph.createRun();
            ru.setText("Page ");
            paragraph.getCTP().addNewFldSimple().setInstr("PAGE \\* MERGEFORMAT");
            ru = paragraph.createRun();
            ru.setText(" of ");
            paragraph.getCTP().addNewFldSimple().setInstr("NUMPAGES \\* MERGEFORMAT");

            // the body content
            doc.createTOC();
            addCustomHeadingStyle(doc, "heading 1", 1);
            addCustomHeadingStyle(doc, "heading 2", 2);
            addCustomHeadingStyle(doc, "heading 3", 3);

            XWPFParagraph sommaire = doc.createParagraph();
            CTP ctP = sommaire.getCTP();
            CTSimpleField toc = ((CTP)ctP).addNewFldSimple();
            toc.setInstr("TOC \\h");
            toc.setDirty(STOnOff.TRUE);
            XWPFRun srun = sommaire.createRun();
            srun.addBreak(BreakType.PAGE);
            for(int i=0;i<livres.size();i++){
                XWPFParagraph title = doc.createParagraph();
                XWPFParagraph book = doc.createParagraph();
                title.setAlignment(ParagraphAlignment.CENTER);
                title.setStyle("heading 1");
                book.setAlignment(ParagraphAlignment.CENTER);
                XWPFRun titleRun = title.createRun();
                XWPFRun bookRun = book.createRun();
                titleRun.setFontSize(20);
                titleRun.setBold(true);
                titleRun.setUnderline(UnderlinePatterns.SINGLE);
                titleRun.setText(livres.get(i).getTitre());
                titleRun.addBreak();
                InputStream is;
                is = new URL(livres.get(i).getURL()).openStream();
                bookRun.addPicture(is, XWPFDocument.PICTURE_TYPE_JPEG, livres.get(i).getURL(), Units.toEMU(150), Units.toEMU(150)); // 150x150 pixels
                bookRun.addBreak();
                bookRun.setFontSize(14);
                bookRun.setTextPosition(20);
                bookRun.setText("Titre : "+ livres.get(i).getTitre());
                bookRun.addBreak();
                bookRun.setText("Auteur : "+ livres.get(i).getAuteur().getPrenom()+" ");
                bookRun.setText(livres.get(i).getAuteur().getNom());
                bookRun.addBreak();
                bookRun.setText("Parution : "+ livres.get(i).getParution());
                bookRun.addBreak();
                bookRun.setText("Résumé : "+ livres.get(i).getPresentation());
                if(i!=livres.size()-1) bookRun.addBreak(BreakType.PAGE);

            }

            XWPFParagraph p = doc.createParagraph();
            XWPFTable table = doc.createTable();
            p.setStyle("Title");
            String string1 = "Tableau des livres empruntés";
            XWPFRun run = p.createRun();
            run.addBreak(BreakType.PAGE);
            run.setFontSize(20);
            run.setColor("260f72");
            run.setText(string1);
            table.setWidth("100%");
            //create first row

            XWPFTableRow tableRowOne = table.getRow(0);
            tableRowOne.getCell(0).setText("Titre");
            tableRowOne.addNewTableCell().setText("Editeur");
            tableRowOne.addNewTableCell().setText("Format");
            tableRowOne.addNewTableCell().setText("Etat");

            // create row
            for(int i=0; i<livres.size();i++) {
                //System.out.println(livres.get(i).getAuteur());
                if (livres.get(i).getEtat().equals("En Prêt")) {
                    XWPFTableRow tableRowTwo = table.createRow();
                    tableRowTwo.getCell(0).setText(livres.get(i).getTitre());
                    tableRowTwo.getCell(1).setText(livres.get(i).getEditeur());
                    tableRowTwo.getCell(2).setText(livres.get(i).getFormat());
                    tableRowTwo.getCell(3).setText(livres.get(i).getEtat());
                    System.out.println(livres.get(i).getEtat());
                }
            }
            // fermeture
            doc.write(out);
            out.close();

        }catch (Exception e){
            System.out.println(e);
        }
        System.out.println("ok");
    }
}
