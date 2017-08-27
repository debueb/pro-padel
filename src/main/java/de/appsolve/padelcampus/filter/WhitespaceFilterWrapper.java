package de.appsolve.padelcampus.filter;

import org.apache.log4j.Logger;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;

public class WhitespaceFilterWrapper extends HttpServletResponseWrapper {

    // Constants ----------------------------------------------------------------------------------
    // Specify here where you'd like to start/stop the trimming.
    // You may want to replace this by init-param and initialize in init() instead.
    static final String[] START_TRIM_AFTER = {"<html", "</textarea", "</pre"};
    static final String[] STOP_TRIM_AFTER = {"</html", "<textarea", "<pre"};

    private static final Logger LOG = Logger.getLogger(WhitespaceFilter.class);
    private final ByteArrayOutputStream capture;
    private ServletOutputStream output;
    private PrintWriter writer;

    public WhitespaceFilterWrapper(HttpServletResponse response) {
        super(response);
        capture = new ByteArrayOutputStream(response.getBufferSize());
    }

    @Override
    public ServletOutputStream getOutputStream() {
        if (writer != null) {
            throw new IllegalStateException(
                    "getWriter() has already been called on this response.");
        }

        if (output == null) {
            output = new ServletOutputStream() {
                @Override
                public void write(int b) throws IOException {
                    capture.write(b);
                }

                @Override
                public void flush() throws IOException {
                    capture.flush();
                }

                @Override
                public void close() throws IOException {
                    capture.close();
                }

                @Override
                public boolean isReady() {
                    return false;
                }

                @Override
                public void setWriteListener(WriteListener arg0) {
                }
            };
        }
        return output;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (writer == null) {
            writer = new PrintWriter(new OutputStreamWriter(capture, getCharacterEncoding()), true) {
                private final Object lock = new Object();
                private StringBuilder builder = new StringBuilder();
                private boolean trim = false;

                @Override
                public void write(int c) {
                    builder.append((char) c); // It is actually a char, not an int.
                }

                @Override
                public void write(char[] chars, int offset, int length) {
                    builder.append(chars, offset, length);
                    this.flush(); // Preflush it.
                }

                @Override
                public void write(String string, int offset, int length) {
                    builder.append(string, offset, length);
                    this.flush(); // Preflush it.
                }

                // Finally override the flush method so that it trims whitespace.
                @Override
                public void flush() {
                    synchronized (lock) {
                        BufferedReader reader = new BufferedReader(new StringReader(builder.toString()));
                        String line = null;

                        try {
                            boolean firstLine = true;

                            while ((line = reader.readLine()) != null) {

                                if (firstLine && line.trim().startsWith("class")) {
                                    out.write(" ");
                                    firstLine = false;
                                }

                                if (startTrim(line)) {
                                    trim = true;
                                    out.write(line);
                                } else if (trim) {

                                    if (line.endsWith(" ")) {
                                        out.write(line.trim() + " ");
                                    } else if (line.startsWith(" ")) {
                                        out.write(" " + line.trim());
                                    } else {
                                        out.write(line.trim());
                                    }

                                    if (stopTrim(line)) {
                                        trim = false;
                                    }

                                } else {
                                    out.write(line + "\n");
                                }
                            }
                        } catch (IOException ex) {
                            setError();
                            LOG.warn("Page failed to render", ex);
                            // Log e or do e.printStackTrace() if necessary.
                        }

                        // Reset the local StringBuilder and issue real flush.
                        builder = new StringBuilder();
                        super.flush();
                    }
                }
            };
        }
        return writer;
    }

    private boolean startTrim(String line) {
        for (String match : START_TRIM_AFTER) {
            if (line.contains(match)) {
                return true;
            }
        }
        return false;
    }

    private boolean stopTrim(String line) {
        for (String match : STOP_TRIM_AFTER) {
            if (line.contains(match)) {
                return true;
            }
        }
        return false;
    }

    public byte[] getCaptureAsBytes() throws IOException {
        if (writer != null) {
            writer.close();
        } else if (output != null) {
            output.close();
        }

        return capture.toByteArray();
    }

    public String getCaptureAsString() throws IOException {
        return new String(getCaptureAsBytes(), getCharacterEncoding());
    }
}
