package com.geekhome.http.jetty;

import org.eclipse.jetty.server.handler.ErrorHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Writer;

public class CustomErrorHandler extends ErrorHandler {
    @Override
    protected void handleErrorPage(HttpServletRequest request, Writer writer, int code, String message) throws IOException {
        writer.write("<html>\n<head>\n");
        writer.write("</head>401<body>");
        writer.write("\n</body>\n</html>\n");
    }
}
