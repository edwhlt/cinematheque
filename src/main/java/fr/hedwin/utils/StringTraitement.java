/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Cinémathèque
 Author: Edwin HELET & Julien GUY
 Class: StringTraitement
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class StringTraitement {

    public static String parseHTML(String string, int sizeLineBreak, String[]... styles){
        return new StringTraitement(string, sizeLineBreak, new HTMLProperties(styles)).toString();
    }
    public static String parse(String string, int sizeLineBreak){
        return new StringTraitement(string, sizeLineBreak, null).toString();
    }

    private String string;
    private final int sizeLineBreak;
    private final HTMLProperties html;

    public static class HTMLProperties{
        public Map<String, String> styleMap = new HashMap<>();
        public HTMLProperties(String[]... styles) {
            for(String[] style : styles) styleMap.put(style[0], style[1]);
        }
        public Map<String, String> getStyle() {
            return styleMap;
        }
        public HTMLProperties addStyle(String key, String value){
            styleMap.put(key, value);
            return this;
        }
    }

    public StringTraitement(String string, int sizeLineBreak, HTMLProperties html){
        this.string = string;
        this.sizeLineBreak = sizeLineBreak;
        this.html = html;
    }

    @Override
    public String toString() {
        if(sizeLineBreak > 0) lineBreak();
        return string;
    }

    private void lineBreak(){
        StringTokenizer tok = new StringTokenizer(string, " ");
        StringBuilder output = new StringBuilder(string.length());
        int lineLen = 0;
        while (tok.hasMoreTokens()) {
            String word = tok.nextToken();

            if (lineLen + word.length() > sizeLineBreak) {
                if(html != null) output.append("<br>");
                else output.append("\n");
                lineLen = 0;
            }else{
                if(output.length() > 0) output.append(" ");
            }
            output.append(word);
            lineLen += word.length();
        }
        this.string = output.toString();
    }


}
