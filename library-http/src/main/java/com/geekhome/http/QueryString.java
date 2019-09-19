package com.geekhome.http;

import java.net.URLDecoder;
import java.util.ArrayList;

public class QueryString {
    private ArrayList<Character> _forbiddenChars = new ArrayList<>();
    private INameValueSet _values;

    public INameValueSet getValues() {
        return _values;
    }

    private void setValues(INameValueSet value) {
        _values = value;
    }

    public QueryString(String urlOriginalString) {
        _forbiddenChars.add('"');
        _forbiddenChars.add('<');
        _forbiddenChars.add('>' );

        setValues(new NameValueSet());

        if (urlOriginalString.lastIndexOf('?') >= 0)
        {
            String baseStringUntrimmed = urlOriginalString.substring(urlOriginalString.lastIndexOf('?'));
            String baseStringTrimmed = StringExt.trimStart(baseStringUntrimmed,'?');
            String baseString;
            try {
                baseString = URLDecoder.decode(baseStringTrimmed, "UTF-8");
            }
            catch (Exception ex) {
                baseString = baseStringTrimmed;
            }
            if (!baseString.equals(""))
            {
                for (String queryPair : baseString.split("&"))
                {
                    String[] queryPairSplitted = queryPair.split("=");
                    String key = queryPairSplitted[0];
                    String value;
                    if (queryPairSplitted.length == 1) {
                        value="";
                    } else {
                        try {
                            value = UrlDecode(queryPairSplitted[1]);
                        } catch (Exception e) {
                            value = queryPairSplitted[1];
                        }
                    }
                    getValues().add(key, value);
                }
            }
        }
    }

    private boolean charIsValid(char ch)
    {
        return  !_forbiddenChars.contains(ch);
    }

    private String UrlDecode(String text) throws Exception {
        if (text == null) return null;
        String result = "";
        int hexSize = 0;
        String hexValue = "";
        byte firstUtf8Mark = 0;
        for (char c : text.toCharArray())
        {
            if (hexSize > 0)
            {
                if ((hexSize == 2) && (c == 'u'))
                {
                    hexSize = 4;
                    continue;
                }
                hexValue += c;
                hexSize--;
                if (hexSize == 0)
                {
                    short value = 0;
                    for (char v : hexValue.toCharArray())
                    {
                        value <<= 4;
                        if (((v >= '0') && (v <= '9')) ||
                                ((v >= 'A') && (v <= 'F')) ||
                                ((v >= 'a') && (v <= 'f')))
                        {
                            byte b = (byte) (v - '0');
                            if (b > 9) b -= 7;
                            if (b > 15) b -= 32;
                            value += b;
                        }
                        else
                            throw new Exception("Error in url encoded text.");
                    }
                    if ((value > 194) && (value < 198))
                    {
                        firstUtf8Mark = (byte)value;
                    }
                    else
                    {
                        if (firstUtf8Mark != 0)
                        {
                            if ((firstUtf8Mark == 196) && ((byte)value == 132)) { result += "Ą"; }
                            else if ((firstUtf8Mark == 196) && ((byte)value == 133)) { result += "ą"; }
                            else if ((firstUtf8Mark == 196) && ((byte)value == 135)) { result += "ć"; }
                            else if ((firstUtf8Mark == 196) && ((byte)value == 153)) { result += "ę"; }
                            else if ((firstUtf8Mark == 197) && ((byte)value == 130)) { result += "ł"; }
                            else if ((firstUtf8Mark == 197) && ((byte)value == 132)) { result += "ń"; }
                            else if ((firstUtf8Mark == 195) && ((byte)value == 179)) { result += "ó"; }
                            else if ((firstUtf8Mark == 197) && ((byte)value == 155)) { result += "ś"; }
                            else if ((firstUtf8Mark == 197) && ((byte)value == 186)) { result += "ź"; }
                            else if ((firstUtf8Mark == 197) && ((byte)value == 188)) { result += "ż"; }
                            else if ((firstUtf8Mark == 196) && ((byte)value == 134)) { result += "Ć"; }
                            else if ((firstUtf8Mark == 196) && ((byte)value == 152)) { result += "Ę"; }
                            else if ((firstUtf8Mark == 197) && ((byte)value == 129)) { result += "Ł"; }
                            else if ((firstUtf8Mark == 197) && ((byte)value == 131)) { result += "Ń"; }
                            else if ((firstUtf8Mark == 195) && ((byte)value == 147)) { result += "Ó"; }
                            else if ((firstUtf8Mark == 197) && ((byte)value == 154)) { result += "Ś"; }
                            else if ((firstUtf8Mark == 197) && ((byte)value == 185)) { result += "Ź"; }
                            else if ((firstUtf8Mark == 197) && ((byte)value == 187)) { result += "Ż"; }
                            firstUtf8Mark = 0;
                        }
                        else if (charIsValid((char) value))
                        {
                            result += (char) value;
                        }
                        hexValue = "";
                    }
                }
            }
            else
            {
                switch (c)
                {
                    case '+':
                        result += ' ';
                        break;
                    case '%':
                        hexSize = 2;
                        break;
                    default:
                        if (charIsValid(c))
                        {
                            result += c;
                        }
                        break;
                }
            }
        }
        return result;
    }
}
